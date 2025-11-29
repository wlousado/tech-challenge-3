# Guia de Configuração do Kong API Gateway

## Visão Geral

Este projeto utiliza Kong API Gateway para gerenciar o acesso aos microserviços com autenticação baseada em roles (médico/paciente).

## Arquitetura

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────┐
│      Kong API Gateway               │
│  - Roteamento                       │
│  - Autenticação (Key Auth)          │
│  - Controle de Acesso (ACL)         │
└──────┬──────────────────────┬───────┘
       │                      │
       ▼                      ▼
┌─────────────────┐   ┌─────────────────┐
│ Scheduling MS   │   │ Notification MS │
│ :8080           │   │ :8080           │
└─────────────────┘   └─────────────────┘
```

## URLs Disponíveis

### Kong Gateway (Porta 8000)
- **Scheduling Service**: `http://localhost:8000/scheduling`
- **Notification Service**: `http://localhost:8000/notification`

### Kong Admin (Porta 8001)
- **Admin API**: `http://localhost:8001`

### Kong Admin GUI (Porta 8002)
- **Admin GUI**: `http://localhost:8002`

### Acesso Direto aos Microserviços (desenvolvimento)
- **Scheduling MS**: `http://localhost:3001/scheduling`
- **Notification MS**: `http://localhost:3002/notification`

## Configuração Atual

### Services
| Nome | Host | Port | Path |
|------|------|------|------|
| scheduling-service | scheduling-ms | 8080 | / |
| notification-service | notification-ms | 8080 | / |

### Routes
| Nome | Path | Strip Path | Service |
|------|------|------------|---------|
| scheduling-route | /scheduling | false | scheduling-service |
| notification-route | /notification | false | notification-service |

### Controle de Acesso (ACL)

| Role | /scheduling | /notification |
|------|-------------|---------------|
| **médico** | ✅ Permitido | ✅ Permitido |
| **paciente** | ❌ Bloqueado | ✅ Permitido |

## Configurando o Projeto

### 1. Subir os Serviços

```bash
docker compose up -d
```

Aguarde ~30 segundos para todos os serviços iniciarem.

### 2. Configurar Autenticação

```bash
cd techchallenge
./kong-auth-acl-setup.sh
```

Este script irá:
1. ✅ Habilitar plugins Key Auth e ACL
2. ✅ Criar usuários de teste (Dr. João, Dra. Ana, Maria)
3. ✅ Definir roles (médico/paciente)
4. ✅ Gerar API Keys
5. ✅ Executar testes de validação

### 3. Testar

```bash
# Médico acessando scheduling
curl -H "apikey: medico-drjoao-key-123" http://localhost:8000/scheduling

# Paciente tentando acessar scheduling (será bloqueado)
curl -H "apikey: paciente-maria-key-789" http://localhost:8000/scheduling
```

## Usuários de Teste

| Usuário | Role | API Key | Permissões |
|---------|------|---------|------------|
| Dr. João | médico | `medico-drjoao-key-123` | `/scheduling`, `/notification` |
| Dra. Ana | médico | `medico-draana-key-456` | `/scheduling`, `/notification` |
| Maria | paciente | `paciente-maria-key-789` | `/notification` (apenas) |

## Exemplos de Uso

### Acessar como Médico

```bash
# Scheduling
curl -H "apikey: medico-drjoao-key-123" http://localhost:8000/scheduling

# Notification
curl -H "apikey: medico-drjoao-key-123" http://localhost:8000/notification
```

### Acessar como Paciente

```bash
# Notification (permitido)
curl -H "apikey: paciente-maria-key-789" http://localhost:8000/notification

# Scheduling (bloqueado - retorna 403)
curl -H "apikey: paciente-maria-key-789" http://localhost:8000/scheduling
```

## Gerenciamento de Usuários

### Criar Novo Médico

```bash
# 1. Criar consumer
curl -X POST http://localhost:8001/consumers \
  --data "username=dr.pedro"

# 2. Adicionar ao grupo 'medico'
curl -X POST http://localhost:8001/consumers/dr.pedro/acls \
  --data "group=medico"

# 3. Gerar API key
curl -X POST http://localhost:8001/consumers/dr.pedro/key-auth
```

### Criar Novo Paciente

```bash
# 1. Criar consumer
curl -X POST http://localhost:8001/consumers \
  --data "username=joao.silva"

# 2. Adicionar ao grupo 'paciente'
curl -X POST http://localhost:8001/consumers/joao.silva/acls \
  --data "group=paciente"

# 3. Gerar API key
curl -X POST http://localhost:8001/consumers/joao.silva/key-auth
```

### Listar Usuários

```bash
curl http://localhost:8001/consumers | jq '.data[] | {username: .username}'
```

### Verificar Role de um Usuário

```bash
curl http://localhost:8001/consumers/dr.joao/acls | jq '.data[] | .group'
```

## Troubleshooting

### Erro: "No API key found in request"

**Causa**: Header `apikey` não foi enviado.

**Solução**:
```bash
curl -H "apikey: sua-chave-aqui" http://localhost:8000/scheduling
```

### Erro: "Invalid authentication credentials"

**Causa**: API key incorreta ou não existe.

**Solução**: Verifique as keys disponíveis:
```bash
curl http://localhost:8001/key-auths | jq '.data[] | {key: .key, consumer: .consumer.username}'
```

### Erro: "You cannot consume this service"

**Causa**: Usuário não tem permissão (role incorreta).

**Solução**: Verifique a role:
```bash
curl http://localhost:8001/consumers/maria.paciente/acls | jq '.data[] | .group'
```

### Kong não responde

**Causa**: Containers podem estar parados.

**Solução**:
```bash
docker compose ps
docker compose up -d
```

### Serviço retorna 404

**Causa**: Endpoint não existe no microserviço.

**Solução**: Teste o acesso direto:
```bash
curl http://localhost:3001/scheduling
curl http://localhost:3002/notification
```

## Comandos Úteis

```bash
# Ver logs do Kong
docker logs kong

# Ver logs dos microserviços
docker logs scheduling-ms
docker logs notification-ms

# Reiniciar serviços
docker compose restart

# Parar tudo
docker compose down

# Parar e remover dados
docker compose down -v
```

## Documentação Adicional

Para informações detalhadas sobre autenticação, exemplos de código e gerenciamento avançado, consulte:

- **[AUTHENTICATION_GUIDE.md](AUTHENTICATION_GUIDE.md)** - Guia completo de autenticação com ACL
- **[README.md](README.md)** - Guia rápido do projeto

## Referências

- [Kong Gateway Documentation](https://docs.konghq.com/gateway/latest/)
- [Key Auth Plugin](https://docs.konghq.com/hub/kong-inc/key-auth/)
- [ACL Plugin](https://docs.konghq.com/hub/kong-inc/acl/)
- [Kong Admin API](https://docs.konghq.com/gateway/latest/admin-api/)
