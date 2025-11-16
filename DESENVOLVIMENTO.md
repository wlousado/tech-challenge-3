# Guia de Desenvolvimento - Med Clinic

## 🎯 Como Funciona o Fluxo de Desenvolvimento

### 📦 **3 Repositórios Separados**

Você tem 3 projetos Git completamente independentes:

```
modulo03/
├── med-clinic/         → Só tem Docker e configurações do Kong
├── scheduling-ms/      → Código do microserviço de agendamento
└── notification-ms/    → Código do microserviço de notificações
```

Cada um tem seu próprio `.git`, suas próprias branches, seus próprios commits.

---

## 👨‍💻 **Para Alguém Começar a Trabalhar**

### 1️⃣ Clonar os 3 repositórios:

```bash
Em uma pasta de sua preferência no seu computador:

git clone git@github.com:wlousado/tech-challenge-3.git
git clone git@github.com:ThiagoLeite06/scheduling-ms.git
git clone git@github.com:ThiagoLeite06/notification-ms.git
```

### 2️⃣ Subir tudo:

```bash
cd med-clinic
./start.sh
```

**Pronto!** O Docker vai buscar o código nas pastas `../scheduling-ms` e `../notification-ms` e fazer o build.

---

## 🔄 **Fazendo Alterações nos Microserviços**

### **Cenário 1: Você quer alterar o Scheduling MS**

```bash
# 1. Vai até a pasta do microserviço
cd ...scheduling-ms

# 2. Cria uma branch
git checkout -b feat/nova-funcionalidade

# 3. Faz suas alterações no código
# ... edita arquivos ...

# 4. Commit e push NO REPOSITÓRIO DO MICROSERVIÇO
git add .
git commit -m "feat: adiciona nova funcionalidade"
git push origin feat/nova-funcionalidade
```

### **Cenário 2: Testar suas alterações com Docker**

```bash
# Volta para o med-clinic
cd ...med-clinic

# Rebuilda só o serviço que mudou
docker compose up -d --build scheduling-ms

# Ou rebuilda tudo
./start.sh
```

---

## 📤 **Como os Commits Funcionam**

### **Cada repositório é TOTALMENTE INDEPENDENTE:**


## ✅ **Resumo Rápido**

| Ação | Onde fazer |
|------|------------|
| Alterar código do scheduling | `cd scheduling-ms` → edita → commit → push |
| Alterar código do notification | `cd notification-ms` → edita → commit → push |
| Alterar config do Kong/Docker | `cd med-clinic` → edita → commit → push |
| Rodar tudo | `cd med-clinic` → `./start.sh` |
| Rebuild após mudanças | `cd med-clinic` → `./start.sh` |

---

## 🚀 **Comandos Úteis**

### Ver logs em tempo real
```bash
cd med-clinic
docker compose logs -f scheduling-ms
docker compose logs -f notification-ms
```

### Parar todos os serviços
```bash
cd med-clinic
./stop.sh
```

### Rebuild de um serviço específico
```bash
cd med-clinic
docker compose up -d --build scheduling-ms
```

### Acessar o container
```bash
docker exec -it scheduling-ms sh
docker exec -it notification-ms sh
```

---

## 💡 **Vantagens desta Arquitetura**

✅ **Independência**: Cada microserviço tem seu próprio repositório e ciclo de vida
✅ **Deploy independente**: Cada serviço pode ser deployado separadamente
✅ **Desenvolvimento paralelo**: Times podem trabalhar independentemente
✅ **Versionamento claro**: Cada serviço tem seu próprio histórico Git
✅ **CI/CD simplificado**: Pipelines independentes por serviço
