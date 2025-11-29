#!/bin/bash

# Script de configuração de autenticação Kong com ACL (Access Control List)
# Este script configura autenticação baseada em roles:
# - Médico: Acessa /scheduling e /notification
# - Paciente: Acessa apenas /notification

set -e

KONG_ADMIN_URL="http://localhost:8001"
KONG_PROXY_URL="http://localhost:8000"

# Cores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo "========================================="
echo "Kong API Gateway - Autenticação com ACL"
echo "========================================="
echo ""

# Função para limpar configurações anteriores
cleanup_previous_config() {
    echo -e "${YELLOW}[0/6] Limpando configurações anteriores...${NC}"

    # Remover plugins
    PLUGINS=$(curl -s "${KONG_ADMIN_URL}/plugins" | jq -r '.data[] | .id')
    for plugin_id in $PLUGINS; do
        curl -s -X DELETE "${KONG_ADMIN_URL}/plugins/${plugin_id}" > /dev/null
        echo "  - Plugin ${plugin_id} removido"
    done

    # Remover consumers
    CONSUMERS=$(curl -s "${KONG_ADMIN_URL}/consumers" | jq -r '.data[] | .username')
    for consumer in $CONSUMERS; do
        curl -s -X DELETE "${KONG_ADMIN_URL}/consumers/${consumer}" > /dev/null
        echo "  - Consumer ${consumer} removido"
    done

    echo -e "${GREEN}✓ Limpeza concluída${NC}"
    echo ""
}

# Executar limpeza
cleanup_previous_config

echo -e "${YELLOW}[1/6] Habilitando plugin Key Auth nos serviços...${NC}"

# Key Auth no scheduling-service
SCHEDULING_KEY_AUTH=$(curl -s -X POST "${KONG_ADMIN_URL}/services/scheduling-service/plugins" \
  --data "name=key-auth" \
  --data "config.key_names=apikey" | jq -r '.id')
echo "  ✓ Key Auth habilitado no scheduling-service (${SCHEDULING_KEY_AUTH})"

# Key Auth no notification-service
NOTIFICATION_KEY_AUTH=$(curl -s -X POST "${KONG_ADMIN_URL}/services/notification-service/plugins" \
  --data "name=key-auth" \
  --data "config.key_names=apikey" | jq -r '.id')
echo "  ✓ Key Auth habilitado no notification-service (${NOTIFICATION_KEY_AUTH})"

echo ""
echo -e "${YELLOW}[2/6] Configurando ACL (Access Control List)...${NC}"

# ACL no scheduling-service - APENAS médicos
SCHEDULING_ACL=$(curl -s -X POST "${KONG_ADMIN_URL}/services/scheduling-service/plugins" \
  --data "name=acl" \
  --data "config.allow=medico" \
  --data "config.hide_groups_header=true" | jq -r '.id')
echo "  ✓ ACL configurado no scheduling-service (apenas grupo 'medico')"

# ACL no notification-service - médicos E pacientes
NOTIFICATION_ACL=$(curl -s -X POST "${KONG_ADMIN_URL}/services/notification-service/plugins" \
  --data "name=acl" \
  --data "config.allow=medico" \
  --data "config.allow=paciente" \
  --data "config.hide_groups_header=true" | jq -r '.id')
echo "  ✓ ACL configurado no notification-service (grupos 'medico' e 'paciente')"

echo ""
echo -e "${YELLOW}[3/6] Criando consumers (usuários)...${NC}"

# Consumer 1: Dr. João (Médico)
curl -s -X POST "${KONG_ADMIN_URL}/consumers" \
  --data "username=dr.joao" \
  --data "custom_id=medico-001" > /dev/null
echo "  ✓ Consumer criado: dr.joao (médico)"

# Consumer 2: Maria (Paciente)
curl -s -X POST "${KONG_ADMIN_URL}/consumers" \
  --data "username=maria.paciente" \
  --data "custom_id=paciente-001" > /dev/null
echo "  ✓ Consumer criado: maria.paciente (paciente)"

# Consumer 3: Dr. Ana (Médica) - Para testes adicionais
curl -s -X POST "${KONG_ADMIN_URL}/consumers" \
  --data "username=dra.ana" \
  --data "custom_id=medico-002" > /dev/null
echo "  ✓ Consumer criado: dra.ana (médico)"

echo ""
echo -e "${YELLOW}[4/6] Associando consumers aos grupos (roles)...${NC}"

# Adicionar dr.joao ao grupo 'medico'
curl -s -X POST "${KONG_ADMIN_URL}/consumers/dr.joao/acls" \
  --data "group=medico" > /dev/null
echo "  ✓ dr.joao adicionado ao grupo 'medico'"

# Adicionar dra.ana ao grupo 'medico'
curl -s -X POST "${KONG_ADMIN_URL}/consumers/dra.ana/acls" \
  --data "group=medico" > /dev/null
echo "  ✓ dra.ana adicionada ao grupo 'medico'"

# Adicionar maria.paciente ao grupo 'paciente'
curl -s -X POST "${KONG_ADMIN_URL}/consumers/maria.paciente/acls" \
  --data "group=paciente" > /dev/null
echo "  ✓ maria.paciente adicionada ao grupo 'paciente'"

echo ""
echo -e "${YELLOW}[5/6] Gerando API Keys para os usuários...${NC}"

# API Key para Dr. João
DR_JOAO_KEY=$(curl -s -X POST "${KONG_ADMIN_URL}/consumers/dr.joao/key-auth" \
  --data "key=medico-drjoao-key-123" | jq -r '.key')
echo "  ✓ API Key gerada para dr.joao"

# API Key para Dra. Ana
DRA_ANA_KEY=$(curl -s -X POST "${KONG_ADMIN_URL}/consumers/dra.ana/key-auth" \
  --data "key=medico-draana-key-456" | jq -r '.key')
echo "  ✓ API Key gerada para dra.ana"

# API Key para Maria (paciente)
MARIA_KEY=$(curl -s -X POST "${KONG_ADMIN_URL}/consumers/maria.paciente/key-auth" \
  --data "key=paciente-maria-key-789" | jq -r '.key')
echo "  ✓ API Key gerada para maria.paciente"

echo ""
echo -e "${YELLOW}[6/6] Testando acessos...${NC}"

# Teste 1: Médico acessando scheduling
TEST1=$(curl -s -o /dev/null -w "%{http_code}" -H "apikey: ${DR_JOAO_KEY}" "${KONG_PROXY_URL}/scheduling")
if [ "$TEST1" == "200" ]; then
    echo -e "  ${GREEN}✓${NC} Médico → /scheduling: ${GREEN}SUCESSO (200)${NC}"
else
    echo -e "  ${RED}✗${NC} Médico → /scheduling: ${RED}FALHOU (${TEST1})${NC}"
fi

# Teste 2: Médico acessando notification
TEST2=$(curl -s -o /dev/null -w "%{http_code}" -H "apikey: ${DR_JOAO_KEY}" "${KONG_PROXY_URL}/notification")
if [ "$TEST2" == "200" ]; then
    echo -e "  ${GREEN}✓${NC} Médico → /notification: ${GREEN}SUCESSO (200)${NC}"
else
    echo -e "  ${RED}✗${NC} Médico → /notification: ${RED}FALHOU (${TEST2})${NC}"
fi

# Teste 3: Paciente acessando scheduling (DEVE FALHAR)
TEST3=$(curl -s -o /dev/null -w "%{http_code}" -H "apikey: ${MARIA_KEY}" "${KONG_PROXY_URL}/scheduling")
if [ "$TEST3" == "403" ] || [ "$TEST3" == "401" ]; then
    echo -e "  ${GREEN}✓${NC} Paciente → /scheduling: ${GREEN}BLOQUEADO (${TEST3})${NC} - Esperado!"
else
    echo -e "  ${RED}✗${NC} Paciente → /scheduling: ${RED}INESPERADO (${TEST3})${NC}"
fi

# Teste 4: Paciente acessando notification
TEST4=$(curl -s -o /dev/null -w "%{http_code}" -H "apikey: ${MARIA_KEY}" "${KONG_PROXY_URL}/notification")
if [ "$TEST4" == "200" ]; then
    echo -e "  ${GREEN}✓${NC} Paciente → /notification: ${GREEN}SUCESSO (200)${NC}"
else
    echo -e "  ${RED}✗${NC} Paciente → /notification: ${RED}FALHOU (${TEST4})${NC}"
fi

echo ""
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}✓ Configuração concluída com sucesso!${NC}"
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

echo "========================================="
echo "CREDENCIAIS DE ACESSO"
echo "========================================="
echo ""
echo -e "${BLUE}1. Dr. João (MÉDICO)${NC}"
echo "   Username: dr.joao"
echo "   Role: medico"
echo "   API Key: ${DR_JOAO_KEY}"
echo "   Pode acessar: /scheduling, /notification"
echo ""
echo -e "${BLUE}2. Dra. Ana (MÉDICO)${NC}"
echo "   Username: dra.ana"
echo "   Role: medico"
echo "   API Key: ${DRA_ANA_KEY}"
echo "   Pode acessar: /scheduling, /notification"
echo ""
echo -e "${BLUE}3. Maria (PACIENTE)${NC}"
echo "   Username: maria.paciente"
echo "   Role: paciente"
echo "   API Key: ${MARIA_KEY}"
echo "   Pode acessar: /notification (apenas)"
echo ""

echo "========================================="
echo "EXEMPLOS DE USO"
echo "========================================="
echo ""
echo -e "${GREEN}✓ Médico acessando Scheduling:${NC}"
echo "  curl -H \"apikey: ${DR_JOAO_KEY}\" ${KONG_PROXY_URL}/scheduling"
echo ""
echo -e "${GREEN}✓ Médico acessando Notification:${NC}"
echo "  curl -H \"apikey: ${DR_JOAO_KEY}\" ${KONG_PROXY_URL}/notification"
echo ""
echo -e "${RED}✗ Paciente tentando acessar Scheduling (BLOQUEADO):${NC}"
echo "  curl -H \"apikey: ${MARIA_KEY}\" ${KONG_PROXY_URL}/scheduling"
echo "  # Retorna: {\"message\":\"You cannot consume this service\"}"
echo ""
echo -e "${GREEN}✓ Paciente acessando Notification:${NC}"
echo "  curl -H \"apikey: ${MARIA_KEY}\" ${KONG_PROXY_URL}/notification"
echo ""

echo "========================================="
echo "GERENCIAMENTO DE USUÁRIOS"
echo "========================================="
echo ""
echo "Criar novo MÉDICO:"
echo "  1. curl -X POST ${KONG_ADMIN_URL}/consumers --data \"username=novo.medico\""
echo "  2. curl -X POST ${KONG_ADMIN_URL}/consumers/novo.medico/acls --data \"group=medico\""
echo "  3. curl -X POST ${KONG_ADMIN_URL}/consumers/novo.medico/key-auth"
echo ""
echo "Criar novo PACIENTE:"
echo "  1. curl -X POST ${KONG_ADMIN_URL}/consumers --data \"username=novo.paciente\""
echo "  2. curl -X POST ${KONG_ADMIN_URL}/consumers/novo.paciente/acls --data \"group=paciente\""
echo "  3. curl -X POST ${KONG_ADMIN_URL}/consumers/novo.paciente/key-auth"
echo ""
echo "Listar todos os usuários e suas roles:"
echo "  curl ${KONG_ADMIN_URL}/consumers | jq '.data[] | {username: .username}'"
echo ""
echo "Ver grupo de um usuário específico:"
echo "  curl ${KONG_ADMIN_URL}/consumers/dr.joao/acls | jq '.data[] | .group'"
echo ""

echo "========================================="
echo "MATRIZ DE ACESSO"
echo "========================================="
echo ""
echo "┌──────────────┬─────────────┬──────────────┐"
echo "│ Role/Endpoint│ /scheduling │ /notification│"
echo "├──────────────┼─────────────┼──────────────┤"
echo "│ medico       │     ✅      │      ✅      │"
echo "│ paciente     │     ❌      │      ✅      │"
echo "└──────────────┴─────────────┴──────────────┘"
echo ""
