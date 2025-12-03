# Guia de Autenticação e Controle de Acesso

## Visão Geral

Este sistema utiliza **Kong API Gateway** com autenticação baseada em **API Keys** e controle de acesso baseado em **Roles (ACL - Access Control List)**.

### Arquitetura de Segurança

```
┌─────────────────────────────────────────────────┐
│              Cliente (Usuário)                  │
│   - Envia API Key no header "apikey"           │
└───────────────────┬─────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────────┐
│           Kong API Gateway                      │
│  ┌───────────────────────────────────────────┐  │
│  │  1. Key Auth Plugin                       │  │
│  │     - Valida se a API key existe          │  │
│  │     - Identifica o usuário (consumer)     │  │
│  └───────────────────────────────────────────┘  │
│                    │                            │
│                    ▼                            │
│  ┌───────────────────────────────────────────┐  │
│  │  2. ACL Plugin                            │  │
│  │     - Verifica role do usuário            │  │
│  │     - Permite ou bloqueia acesso          │  │
│  └───────────────────────────────────────────┘  │
└───────────────────┬─────────────────────────────┘
                    │
           ┌────────┴────────┐
           ▼                 ▼
   ┌───────────────┐  ┌─────────────────┐
   │ Scheduling MS │  │ Notification MS │
   └───────────────┘  └─────────────────┘
```

## Roles e Permissões

### Matriz de Controle de Acesso

| Role      | /scheduling | /notification |
|-----------|-------------|---------------|
| `medico`  | ✅ Permitido | ✅ Permitido   |
| `paciente`| ❌ Bloqueado | ✅ Permitido   |

### Descrição das Roles

#### 1. Médico (`medico`)
- **Acesso total** aos serviços
- Pode consultar agendamentos (`/scheduling`)
- Pode consultar e enviar notificações (`/notification`)
- Uso típico: Sistema médico, dashboard de profissionais de saúde

#### 2. Paciente (`paciente`)
- **Acesso limitado** aos serviços
- **Não pode** acessar agendamentos diretamente (`/scheduling`)
- Pode consultar notificações (`/notification`)
- Uso típico: Aplicativo mobile do paciente, portal do paciente

## Configuração Rápida

### Executar Script de Configuração

```bash
cd techchallenge
./kong-auth-acl-setup.sh
```

Este script irá:
1. ✅ Limpar configurações anteriores
2. ✅ Habilitar plugins Key Auth e ACL
3. ✅ Criar usuários de teste (Dr. João, Dra. Ana, Maria)
4. ✅ Gerar API keys
5. ✅ Executar testes automatizados

## Usuários de Teste

### 1. Dr. João (Médico)
```
Username: dr.joao
Role: medico
API Key: medico-drjoao-key-123
Permissões: /scheduling, /notification
```

### 2. Dra. Ana (Médica)
```
Username: dra.ana
Role: medico
API Key: medico-draana-key-456
Permissões: /scheduling, /notification
```

### 3. Maria (Paciente)
```
Username: maria.paciente
Role: paciente
API Key: paciente-maria-key-789
Permissões: /notification (apenas)
```

## Exemplos de Uso

### Caso 1: Médico Acessando Scheduling

```bash
curl -H "apikey: medico-drjoao-key-123" http://localhost:8000/scheduling
```

**Resposta:**
```
Hello World
```
✅ **Status: 200 OK**

---

### Caso 2: Médico Acessando Notification

```bash
curl -H "apikey: medico-drjoao-key-123" http://localhost:8000/notification
```

**Resposta:**
```
Notification Service Running
```
✅ **Status: 200 OK**

---

### Caso 3: Paciente Tentando Acessar Scheduling (Bloqueado)

```bash
curl -H "apikey: paciente-maria-key-789" http://localhost:8000/scheduling
```

**Resposta:**
```json
{
  "message": "You cannot consume this service",
  "request_id": "..."
}
```
❌ **Status: 403 Forbidden**

---

### Caso 4: Paciente Acessando Notification

```bash
curl -H "apikey: paciente-maria-key-789" http://localhost:8000/notification
```

**Resposta:**
```
Notification Service Running
```
✅ **Status: 200 OK**

---

### Caso 5: Sem Autenticação (Bloqueado)

```bash
curl http://localhost:8000/scheduling
```

**Resposta:**
```json
{
  "message": "No API key found in request",
  "request_id": "..."
}
```
❌ **Status: 401 Unauthorized**

## Gerenciamento de Usuários

### Criar Novo Médico

```bash
# 1. Criar consumer
curl -X POST http://localhost:8001/consumers \
  --data "username=dr.pedro" \
  --data "custom_id=medico-003"

# 2. Adicionar ao grupo 'medico'
curl -X POST http://localhost:8001/consumers/dr.pedro/acls \
  --data "group=medico"

# 3. Gerar API key
curl -X POST http://localhost:8001/consumers/dr.pedro/key-auth

# Ou com chave customizada:
curl -X POST http://localhost:8001/consumers/dr.pedro/key-auth \
  --data "key=medico-drpedro-key-999"
```

### Criar Novo Paciente

```bash
# 1. Criar consumer
curl -X POST http://localhost:8001/consumers \
  --data "username=joao.paciente" \
  --data "custom_id=paciente-002"

# 2. Adicionar ao grupo 'paciente'
curl -X POST http://localhost:8001/consumers/joao.paciente/acls \
  --data "group=paciente"

# 3. Gerar API key
curl -X POST http://localhost:8001/consumers/joao.paciente/key-auth
```

### Listar Todos os Usuários

```bash
curl http://localhost:8001/consumers | jq '.data[] | {username: .username, custom_id: .custom_id}'
```

### Ver Role de um Usuário

```bash
curl http://localhost:8001/consumers/dr.joao/acls | jq '.data[] | .group'
```

### Listar API Keys de um Usuário

```bash
curl http://localhost:8001/consumers/dr.joao/key-auth | jq '.data[]'
```

### Revogar API Key

```bash
# 1. Obter ID da chave
curl http://localhost:8001/consumers/dr.joao/key-auth | jq '.data[] | {id: .id, key: .key}'

# 2. Deletar a chave usando o ID
curl -X DELETE http://localhost:8001/consumers/dr.joao/key-auth/{KEY_ID}
```

### Remover Usuário Completamente

```bash
curl -X DELETE http://localhost:8001/consumers/dr.joao
```

## Integração com Aplicações

### JavaScript/TypeScript (React, Node.js)

```typescript
const API_BASE_URL = 'http://localhost:8000';

interface User {
  username: string;
  apiKey: string;
  role: 'medico' | 'paciente';
}

class ApiClient {
  private apiKey: string;

  constructor(apiKey: string) {
    this.apiKey = apiKey;
  }

  private async request(endpoint: string, options: RequestInit = {}) {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      ...options,
      headers: {
        'apikey': this.apiKey,
        'Content-Type': 'application/json',
        ...options.headers,
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Request failed');
    }

    return response.text();
  }

  async getScheduling() {
    return this.request('/scheduling');
  }

  async getNotification() {
    return this.request('/notification');
  }
}

// Uso:
const doctorClient = new ApiClient('medico-drjoao-key-123');
const patientClient = new ApiClient('paciente-maria-key-789');

// Médico pode acessar tudo
await doctorClient.getScheduling();    // ✅ OK
await doctorClient.getNotification();  // ✅ OK

// Paciente só pode acessar notification
await patientClient.getNotification(); // ✅ OK
await patientClient.getScheduling();   // ❌ 403 Forbidden
```

### Python

```python
import requests
from typing import Literal

class ApiClient:
    def __init__(self, api_key: str):
        self.api_key = api_key
        self.base_url = 'http://localhost:8000'
        self.headers = {'apikey': api_key}

    def get_scheduling(self) -> str:
        response = requests.get(
            f'{self.base_url}/scheduling',
            headers=self.headers
        )
        response.raise_for_status()
        return response.text

    def get_notification(self) -> str:
        response = requests.get(
            f'{self.base_url}/notification',
            headers=self.headers
        )
        response.raise_for_status()
        return response.text

# Uso:
doctor = ApiClient('medico-drjoao-key-123')
patient = ApiClient('paciente-maria-key-789')

# Médico
print(doctor.get_scheduling())    # ✅ OK
print(doctor.get_notification())  # ✅ OK

# Paciente
print(patient.get_notification()) # ✅ OK
try:
    print(patient.get_scheduling())  # ❌ HTTPError 403
except requests.HTTPError as e:
    print(f"Acesso negado: {e}")
```

### Java (Spring Boot)

```java
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

public class ApiClient {
    private final String apiKey;
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8000";

    public ApiClient(String apiKey) {
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);
        return headers;
    }

    public String getScheduling() {
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
            BASE_URL + "/scheduling",
            HttpMethod.GET,
            entity,
            String.class
        );
        return response.getBody();
    }

    public String getNotification() {
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
            BASE_URL + "/notification",
            HttpMethod.GET,
            entity,
            String.class
        );
        return response.getBody();
    }
}

// Uso:
ApiClient doctor = new ApiClient("medico-drjoao-key-123");
ApiClient patient = new ApiClient("paciente-maria-key-789");

// Médico
System.out.println(doctor.getScheduling());    // ✅ OK
System.out.println(doctor.getNotification());  // ✅ OK

// Paciente
System.out.println(patient.getNotification()); // ✅ OK
try {
    System.out.println(patient.getScheduling()); // ❌ 403
} catch (HttpClientErrorException.Forbidden e) {
    System.out.println("Acesso negado: " + e.getMessage());
}
```

## Códigos de Resposta HTTP

| Código | Significado | Quando Ocorre |
|--------|-------------|---------------|
| **200 OK** | Sucesso | Autenticação válida e permissão concedida |
| **401 Unauthorized** | Não autenticado | API key não fornecida ou inválida |
| **403 Forbidden** | Sem permissão | Autenticado mas sem permissão para acessar o recurso |
| **404 Not Found** | Não encontrado | Endpoint não existe |
| **500 Internal Server Error** | Erro do servidor | Problema no Kong ou no microserviço |

## Troubleshooting

### Problema: "No API key found in request"

**Causa**: Header `apikey` não foi enviado.

**Solução**:
```bash
# ❌ Errado
curl http://localhost:8000/scheduling

# ✅ Correto
curl -H "apikey: sua-chave-aqui" http://localhost:8000/scheduling
```

---

### Problema: "Invalid authentication credentials"

**Causa**: API key incorreta ou não existe.

**Solução**: Verifique suas credenciais:
```bash
curl http://localhost:8001/key-auths | jq '.data[] | {key: .key, consumer: .consumer.username}'
```

---

### Problema: "You cannot consume this service"

**Causa**: Usuário autenticado mas não tem permissão (role incorreta).

**Solução**: Verifique a role do usuário:
```bash
curl http://localhost:8001/consumers/maria.paciente/acls | jq '.data[] | .group'
```

Se a role estiver errada, atualize:
```bash
# Remover role antiga
curl -X DELETE http://localhost:8001/consumers/maria.paciente/acls/{ACL_ID}

# Adicionar role correta
curl -X POST http://localhost:8001/consumers/maria.paciente/acls \
  --data "group=medico"
```

---

### Problema: Usuário foi removido mas API key ainda funciona

**Causa**: Cache do Kong.

**Solução**: Force flush do cache:
```bash
# Recarregar configurações do Kong
docker restart kong
```

## Boas Práticas de Segurança

### 1. Rotação de API Keys

Troque as API keys periodicamente:

```bash
# 1. Criar nova key
NEW_KEY=$(curl -s -X POST http://localhost:8001/consumers/dr.joao/key-auth | jq -r '.key')

# 2. Atualizar aplicação com nova key

# 3. Aguardar período de transição (ex: 24h)

# 4. Revogar key antiga
curl -X DELETE http://localhost:8001/consumers/dr.joao/key-auth/{OLD_KEY_ID}
```

### 2. Nunca Exponha API Keys no Frontend

❌ **Não faça:**
```javascript
// NUNCA coloque API keys no código frontend!
const API_KEY = 'medico-drjoao-key-123';
```

✅ **Faça:**
```javascript
// Backend proxy que adiciona a API key
async function fetchScheduling() {
  const response = await fetch('/api/scheduling'); // Seu backend
  // Seu backend adiciona o header apikey antes de chamar o Kong
}
```

### 3. Use HTTPS em Produção

Em produção, **sempre** use HTTPS para proteger as API keys em trânsito.

### 4. Monitore Tentativas de Acesso Negadas

Configure alertas para múltiplas tentativas 401/403:

```bash
# Adicionar plugin de logging
curl -X POST http://localhost:8001/services/scheduling-service/plugins \
  --data "name=file-log" \
  --data "config.path=/tmp/kong-access.log"
```

### 5. Rate Limiting por Usuário

Evite abuso limitando requisições por consumer:

```bash
curl -X POST http://localhost:8001/plugins \
  --data "name=rate-limiting" \
  --data "config.minute=100" \
  --data "config.policy=local"
```

## Próximos Passos (Evolução)

### 1. JWT (JSON Web Tokens)

Para sistemas mais robustos, considere migrar para JWT:
- Tokens com expiração automática
- Claims customizadas (roles, permissões específicas)
- Suporte a refresh tokens

### 2. OAuth 2.0

Para aplicações de terceiros:
- Flow de autorização completo
- Scopes granulares
- Integração com Identity Providers (Google, Azure AD, etc.)

### 3. Multi-tenancy

Adicionar suporte a múltiplas clínicas/hospitais:
- Isolamento de dados por tenant
- Permissões específicas por tenant
- Consumer groups por organização

## Referências

- [Kong Key Auth Plugin](https://docs.konghq.com/hub/kong-inc/key-auth/)
- [Kong ACL Plugin](https://docs.konghq.com/hub/kong-inc/acl/)
- [Kong Admin API - Consumers](https://docs.konghq.com/gateway/latest/admin-api/consumers/)
- [Kong Admin API - Plugins](https://docs.konghq.com/gateway/latest/admin-api/plugins/)
