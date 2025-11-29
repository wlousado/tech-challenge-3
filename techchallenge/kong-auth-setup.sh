#!/bin/bash

# Script de configuração de autenticação Kong com Key Auth Plugin
# Este script configura autenticação via API Key para os serviços no Kong Gateway

set -e

KONG_ADMIN_URL="http://localhost:8001"
KONG_PROXY_URL="http://localhost:8000"

echo "========================================="
echo "Kong API Gateway - Configuração de Autenticação"
echo "========================================="
echo ""

# Cores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}[1/4] Habilitando plugin Key Auth no serviço scheduling-service...${NC}"
curl -s -X POST "${KONG_ADMIN_URL}/services/scheduling-service/plugins" \
  --data "name=key-auth" \
  --data "config.key_names=apikey" | jq '{id: .id, name: .name, service: .service.id}'

echo ""
echo -e "${YELLOW}[2/4] Habilitando plugin Key Auth no serviço notification-service...${NC}"
curl -s -X POST "${KONG_ADMIN_URL}/services/notification-service/plugins" \
  --data "name=key-auth" \
  --data "config.key_names=apikey" | jq '{id: .id, name: .name, service: .service.id}'

echo ""
echo -e "${YELLOW}[3/4] Criando consumers (usuários)...${NC}"

# Consumer 1: Admin
curl -s -X POST "${KONG_ADMIN_URL}/consumers" \
  --data "username=admin" \
  --data "custom_id=admin-001" | jq '{id: .id, username: .username}'

# Consumer 2: App Mobile
curl -s -X POST "${KONG_ADMIN_URL}/consumers" \
  --data "username=mobile-app" \
  --data "custom_id=mobile-app-001" | jq '{id: .id, username: .username}'

# Consumer 3: App Web
curl -s -X POST "${KONG_ADMIN_URL}/consumers" \
  --data "username=web-app" \
  --data "custom_id=web-app-001" | jq '{id: .id, username: .username}'

echo ""
echo -e "${YELLOW}[4/4] Gerando API Keys para os consumers...${NC}"

# API Key para Admin
ADMIN_KEY=$(curl -s -X POST "${KONG_ADMIN_URL}/consumers/admin/key-auth" \
  --data "key=admin-api-key-12345" | jq -r '.key')

# API Key para Mobile App
MOBILE_KEY=$(curl -s -X POST "${KONG_ADMIN_URL}/consumers/mobile-app/key-auth" \
  --data "key=mobile-api-key-67890" | jq -r '.key')

# API Key para Web App
WEB_KEY=$(curl -s -X POST "${KONG_ADMIN_URL}/consumers/web-app/key-auth" | jq -r '.key')

echo ""
echo -e "${GREEN}✓ Configuração concluída com sucesso!${NC}"
echo ""
echo "========================================="
echo "API KEYS GERADAS"
echo "========================================="
echo ""
echo "1. Admin:"
echo "   Username: admin"
echo "   API Key: ${ADMIN_KEY}"
echo ""
echo "2. Mobile App:"
echo "   Username: mobile-app"
echo "   API Key: ${MOBILE_KEY}"
echo ""
echo "3. Web App:"
echo "   Username: web-app"
echo "   API Key: ${WEB_KEY} (gerada automaticamente)"
echo ""
echo "========================================="
echo "COMO USAR"
echo "========================================="
echo ""
echo "Agora todos os endpoints requerem autenticação via API Key."
echo ""
echo "Exemplo 1 - Scheduling Service:"
echo "  curl -H \"apikey: ${ADMIN_KEY}\" ${KONG_PROXY_URL}/scheduling"
echo ""
echo "Exemplo 2 - Notification Service:"
echo "  curl -H \"apikey: ${MOBILE_KEY}\" ${KONG_PROXY_URL}/notification"
echo ""
echo "Sem API Key (retornará 401 Unauthorized):"
echo "  curl ${KONG_PROXY_URL}/scheduling"
echo ""
echo "========================================="
echo "ADICIONAR NOVOS USUÁRIOS"
echo "========================================="
echo ""
echo "1. Criar novo consumer:"
echo "   curl -X POST ${KONG_ADMIN_URL}/consumers \\"
echo "     --data \"username=novo-usuario\""
echo ""
echo "2. Gerar API Key para o consumer:"
echo "   curl -X POST ${KONG_ADMIN_URL}/consumers/novo-usuario/key-auth"
echo ""
echo "Ou com chave customizada:"
echo "   curl -X POST ${KONG_ADMIN_URL}/consumers/novo-usuario/key-auth \\"
echo "     --data \"key=minha-chave-customizada\""
echo ""
