# Tech Challenge 3 - Sistema de Clínica Médica com Kong API Gateway

Sistema de microserviços para gerenciamento de clínica médica com autenticação e controle de acesso baseado em roles.

## 🚀 Quick Start

### 1. Subir os Serviços

```bash
docker compose up -d
```

Aguarde ~30 segundos para todos os serviços iniciarem.

### 2. Configurar Autenticação

```bash
./kong-auth-acl-setup.sh
```

### 3. Testar

```bash
# Médico acessando scheduling
curl -H "apikey: medico-drjoao-key-123" http://localhost:8000/scheduling

# Paciente tentando acessar scheduling (será bloqueado)
curl -H "apikey: paciente-maria-key-789" http://localhost:8000/scheduling
```

## 📋 Arquitetura

```
┌──────────────────────────────────────────────┐
│            Cliente (Usuário)                 │
│     Envia header: apikey: sua-chave          │
└────────────────┬─────────────────────────────┘
                 │
                 ▼
┌────────────────────────────────────────────────┐
│          Kong API Gateway :8000                │
│  ┌──────────────────────────────────────────┐  │
│  │  Plugins:                                │  │
│  │  - Key Auth (autenticação)               │  │
│  │  - ACL (controle de acesso por role)     │  │
│  └──────────────────────────────────────────┘  │
└────────────┬───────────────────┬───────────────┘
             │                   │
             ▼                   ▼
  ┌──────────────────┐  ┌─────────────────┐
  │  Scheduling MS   │  │ Notification MS │
  │  :8080           │  │ :8080           │
  └──────────────────┘  └─────────────────┘
```

## 🔑 Usuários de Teste

| Usuário | Role | API Key | Acesso |
|---------|------|---------|--------|
| Dr. João | Médico | `medico-drjoao-key-123` | `/scheduling`, `/notification` |
| Dra. Ana | Médico | `medico-draana-key-456` | `/scheduling`, `/notification` |
| Maria | Paciente | `paciente-maria-key-789` | `/notification` (apenas) |

## 🌐 Endpoints

### Kong Gateway (Produção)
- **Scheduling**: `http://localhost:8000/scheduling`
- **Notification**: `http://localhost:8000/notification`

### Kong Admin
- **Admin API**: `http://localhost:8001`
- **Admin GUI**: `http://localhost:8002`

### Acesso Direto (Desenvolvimento)
- **Scheduling MS**: `http://localhost:3001/scheduling`
- **Notification MS**: `http://localhost:3002/notification`

## 🔐 Controle de Acesso (ACL)

| Role | /scheduling | /notification |
|------|-------------|---------------|
| **médico** | ✅ | ✅ |
| **paciente** | ❌ | ✅ |

## 📖 Documentação

- **[AUTHENTICATION_GUIDE.md](AUTHENTICATION_GUIDE.md)** - Guia completo de autenticação com exemplos de código
- **[KONG_SETUP_GUIDE.md](KONG_SETUP_GUIDE.md)** - Configuração avançada do Kong
- **[kong-auth-acl-setup.sh](kong-auth-acl-setup.sh)** - Script de configuração automática

## 💻 Exemplos de Uso

### cURL

```bash
# Médico acessando scheduling
curl -H "apikey: medico-drjoao-key-123" http://localhost:8000/scheduling

# Paciente acessando notification
curl -H "apikey: paciente-maria-key-789" http://localhost:8000/notification
```

### JavaScript/TypeScript

```javascript
const response = await fetch('http://localhost:8000/scheduling', {
  headers: {
    'apikey': 'medico-drjoao-key-123'
  }
});
const data = await response.text();
```

### Python

```python
import requests

response = requests.get(
    'http://localhost:8000/scheduling',
    headers={'apikey': 'medico-drjoao-key-123'}
)
print(response.text)
```

## 🛠️ Gerenciamento

### Criar Novo Médico

```bash
# 1. Criar usuário
curl -X POST http://localhost:8001/consumers \
  --data "username=dr.pedro"

# 2. Adicionar role de médico
curl -X POST http://localhost:8001/consumers/dr.pedro/acls \
  --data "group=medico"

# 3. Gerar API key
curl -X POST http://localhost:8001/consumers/dr.pedro/key-auth
```

### Criar Novo Paciente

```bash
# 1. Criar usuário
curl -X POST http://localhost:8001/consumers \
  --data "username=joao.silva"

# 2. Adicionar role de paciente
curl -X POST http://localhost:8001/consumers/joao.silva/acls \
  --data "group=paciente"

# 3. Gerar API key
curl -X POST http://localhost:8001/consumers/joao.silva/key-auth
```

## 🧪 Testes

### Testar Autenticação

```bash
# ✅ Deve funcionar
curl -H "apikey: medico-drjoao-key-123" http://localhost:8000/scheduling

# ❌ Deve retornar 403 Forbidden
curl -H "apikey: paciente-maria-key-789" http://localhost:8000/scheduling

# ❌ Deve retornar 401 Unauthorized
curl http://localhost:8000/scheduling
```

### Ver Logs

```bash
# Logs do Kong
docker logs kong

# Logs dos microserviços
docker logs scheduling-ms
docker logs notification-ms
```

## 📦 Tecnologias

- **Kong Gateway 3.7** - API Gateway com autenticação e ACL
- **PostgreSQL 15** - Banco de dados do Kong
- **Spring Boot 4.0** - Framework dos microserviços
- **Java 21** - Linguagem dos microserviços
- **Docker Compose** - Orquestração de containers

## 🔄 Comandos Úteis

```bash
# Subir todos os serviços
docker compose up -d

# Ver status dos containers
docker compose ps

# Parar todos os serviços
docker compose down

# Rebuild após mudanças no código
docker compose build
docker compose up -d

# Ver logs em tempo real
docker compose logs -f

# Resetar tudo (CUIDADO: remove dados)
docker compose down -v
```

## 🆘 Troubleshooting

### Erro: "No API key found in request"
**Solução**: Adicione o header `apikey` na requisição.

### Erro: "You cannot consume this service"
**Solução**: Usuário não tem permissão. Verifique a role usando:
```bash
curl http://localhost:8001/consumers/maria.paciente/acls | jq
```

### Kong não responde
**Solução**: Verifique se todos os containers estão rodando:
```bash
docker compose ps
docker compose up -d
```

### Microserviço retorna 404
**Solução**: Verifique se o endpoint existe:
```bash
# Testar acesso direto
curl http://localhost:3001/scheduling
curl http://localhost:3002/notification
```

## 📚 Próximos Passos

1. ✅ Autenticação básica com API Keys
2. ✅ Controle de acesso baseado em roles (ACL)
3. 🔜 Implementar endpoints REST completos (CRUD)
4. 🔜 Adicionar rate limiting
5. 🔜 Implementar JWT para tokens com expiração
6. 🔜 Adicionar CORS para aplicações web
7. 🔜 Implementar logging centralizado
8. 🔜 Adicionar monitoramento (Prometheus/Grafana)

## 📄 Licença

Este projeto é parte do Tech Challenge 3 da FIAP.
