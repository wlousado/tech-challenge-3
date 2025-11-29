# Guia de Configuração do Kong API Gateway

## Visão Geral

Este projeto utiliza Kong API Gateway para gerenciar o acesso aos microserviços. Este guia explica como configurar e usar a autenticação via API Key.

## Arquitetura Atual

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
│  - Rate Limiting (opcional)         │
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

### Kong Admin API (Porta 8001)
- **Admin API**: `http://localhost:8001`

### Kong Admin GUI (Porta 8002)
- **Admin GUI**: `http://localhost:8002`

### Acesso Direto aos Microserviços (não recomendado em produção)
- **Scheduling MS**: `http://localhost:3001/scheduling`
- **Notification MS**: `http://localhost:3002/notification`

## Configuração Atual do Kong

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

## Configurando Autenticação

### Passo 1: Executar o Script de Configuração

Execute o script de configuração automática:

```bash
cd techchallenge
./kong-auth-setup.sh
```

Este script irá:
1. Habilitar o plugin Key Auth nos serviços
2. Criar 3 consumers (usuários): admin, mobile-app, web-app
3. Gerar API Keys para cada consumer
4. Exibir as chaves geradas

### Passo 2: Testar Sem Autenticação (Deve Falhar)

Após executar o script, tentativas sem API Key retornarão erro 401:

```bash
curl http://localhost:8000/scheduling
# Retorna: {"message":"No API key found in request"}
```

### Passo 3: Testar Com Autenticação (Deve Funcionar)

Use o header `apikey` com uma das chaves geradas:

```bash
# Usando a chave do admin
curl -H "apikey: admin-api-key-12345" http://localhost:8000/scheduling

# Usando a chave do mobile-app
curl -H "apikey: mobile-api-key-67890" http://localhost:8000/notification
```

## Gerenciamento de API Keys

### Listar todos os Consumers

```bash
curl http://localhost:8001/consumers | jq '.data[] | {username: .username, custom_id: .custom_id}'
```

### Criar Novo Consumer

```bash
curl -X POST http://localhost:8001/consumers \
  --data "username=novo-usuario" \
  --data "custom_id=novo-usuario-001"
```

### Gerar API Key para Consumer

**Chave Automática (Kong gera):**
```bash
curl -X POST http://localhost:8001/consumers/novo-usuario/key-auth
```

**Chave Customizada:**
```bash
curl -X POST http://localhost:8001/consumers/novo-usuario/key-auth \
  --data "key=minha-chave-secreta-12345"
```

### Listar API Keys de um Consumer

```bash
curl http://localhost:8001/consumers/admin/key-auth
```

### Revogar API Key

```bash
# Primeiro, obtenha o ID da chave
curl http://localhost:8001/consumers/admin/key-auth

# Depois delete usando o ID
curl -X DELETE http://localhost:8001/consumers/admin/key-auth/{KEY_ID}
```

## Outros Plugins Úteis

### Rate Limiting

Limite o número de requisições por consumidor:

```bash
# Limitar a 100 requisições por minuto
curl -X POST http://localhost:8001/services/scheduling-service/plugins \
  --data "name=rate-limiting" \
  --data "config.minute=100" \
  --data "config.policy=local"
```

### CORS (Cross-Origin Resource Sharing)

Habilitar CORS para chamadas de browsers:

```bash
curl -X POST http://localhost:8001/services/scheduling-service/plugins \
  --data "name=cors" \
  --data "config.origins=*" \
  --data "config.methods=GET,POST,PUT,DELETE" \
  --data "config.headers=Accept,Content-Type,apikey"
```

### Request Transformer

Adicionar headers automáticos nas requisições:

```bash
curl -X POST http://localhost:8001/services/scheduling-service/plugins \
  --data "name=request-transformer" \
  --data "config.add.headers=X-Service-Type:Scheduling"
```

### Logging (File Log)

Registrar todas as requisições em arquivo:

```bash
curl -X POST http://localhost:8001/services/scheduling-service/plugins \
  --data "name=file-log" \
  --data "config.path=/tmp/kong-scheduling.log"
```

## Remover Autenticação

Se precisar remover a autenticação:

```bash
# Listar plugins do serviço
curl http://localhost:8001/services/scheduling-service/plugins

# Deletar o plugin key-auth usando o ID
curl -X DELETE http://localhost:8001/plugins/{PLUGIN_ID}
```

## Troubleshooting

### 1. Erro: "No API key found in request"

**Causa**: O plugin Key Auth está ativo mas você não enviou a API key.

**Solução**: Adicione o header `apikey` na requisição:
```bash
curl -H "apikey: sua-chave-aqui" http://localhost:8000/scheduling
```

### 2. Erro: "Invalid authentication credentials"

**Causa**: A API key enviada não existe ou está incorreta.

**Solução**: Verifique suas chaves:
```bash
curl http://localhost:8001/key-auths
```

### 3. Kong não responde

**Causa**: Containers podem estar parados.

**Solução**:
```bash
docker compose ps
docker compose up -d
```

### 4. Serviço retorna 404

**Causa**: Route ou controller não configurado corretamente.

**Solução**: Verifique a configuração:
```bash
# Verificar routes
curl http://localhost:8001/routes | jq '.data[] | {name: .name, paths: .paths}'

# Testar acesso direto ao microserviço
curl http://localhost:3001/scheduling
```

## Boas Práticas

### Segurança

1. **Nunca** exponha a porta do Kong Admin API (8001) publicamente
2. **Sempre** use HTTPS em produção (configurar certificados SSL)
3. **Rotacione** API keys periodicamente
4. **Use** diferentes keys para diferentes ambientes (dev, staging, prod)
5. **Monitore** tentativas de acesso não autorizado

### Performance

1. Configure **rate limiting** para evitar abuso
2. Use **caching** para endpoints que não mudam frequentemente
3. Configure **timeout** adequado para os upstreams
4. Monitore métricas do Kong

### Monitoramento

1. Use plugins de logging (file-log, syslog, http-log)
2. Integre com ferramentas de APM (Datadog, New Relic)
3. Configure alertas para erros 5xx e alta latência

## Exemplo de Uso em Aplicação

### JavaScript/Node.js

```javascript
const axios = require('axios');

const API_KEY = 'admin-api-key-12345';
const BASE_URL = 'http://localhost:8000';

async function getScheduling() {
  const response = await axios.get(`${BASE_URL}/scheduling`, {
    headers: {
      'apikey': API_KEY
    }
  });
  return response.data;
}
```

### Python

```python
import requests

API_KEY = 'admin-api-key-12345'
BASE_URL = 'http://localhost:8000'

def get_scheduling():
    headers = {'apikey': API_KEY}
    response = requests.get(f'{BASE_URL}/scheduling', headers=headers)
    return response.text
```

### Java

```java
import java.net.http.*;
import java.net.URI;

public class KongClient {
    private static final String API_KEY = "admin-api-key-12345";
    private static final String BASE_URL = "http://localhost:8000";

    public String getScheduling() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/scheduling"))
            .header("apikey", API_KEY)
            .GET()
            .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
```

## Referências

- [Kong Gateway Documentation](https://docs.konghq.com/gateway/latest/)
- [Key Auth Plugin](https://docs.konghq.com/hub/kong-inc/key-auth/)
- [Kong Admin API](https://docs.konghq.com/gateway/latest/admin-api/)
