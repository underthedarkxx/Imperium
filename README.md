# Imperium

Intranet corporativa para gestão interna: autenticação com JWT, controle de usuários por papel hierárquico, setores e canal de sugestões. Backend em **Spring Boot 3** e frontend em HTML/CSS/JavaScript puro.

> Projeto em desenvolvimento. O modelo de dados já contempla chamados e histórico; a API implementada hoje cobre autenticação, usuários, setores e sugestões.

---

## Stack

| Camada | Tecnologias |
| :--- | :--- |
| Backend | Java 21, Spring Boot 3.5, Spring Web, Spring Data JPA, Spring Security, Bean Validation |
| Autenticação | JWT (jjwt 0.12), BCrypt |
| Banco | MySQL 8 |
| Build | Maven (wrapper incluso) |
| Frontend | HTML, CSS e JavaScript sem framework |

---

## Funcionalidades

**Autenticação e segurança**

- Login por e-mail e senha com retorno de token JWT (`POST /api/login`)
- Filtro próprio (`JwtFilter`) validando o token em cada requisição
- Sessão *stateless*, CSRF desabilitado e CORS restrito às origens de desenvolvimento
- Senhas persistidas com BCrypt — nenhuma senha em texto puro
- Autorização por papel via `@PreAuthorize`, com quatro níveis: `Colaborador`, `Gerente`, `Administrador` e `CEO`

**Gestão de usuários** (`/api/admin/usuarios`)

- Criar usuário — exclusivo do CEO
- Listar usuários e contar o total — CEO e Administrador
- Atualizar e apagar usuário — exclusivo do CEO
- Status de usuário (`Ativo` / `Desativado`) e vínculo obrigatório com um setor

**Sugestões** (`/api/sugestoes`)

- Qualquer usuário autenticado registra uma sugestão
- CEO e Administrador listam todas as sugestões
- Ciclo de status: `Enviada` → `EmAnalise` → `Aprovada` ou `Rejeitada`

**Inicialização**

- `DataInitializer` cria, na primeira subida, o setor **ADMINISTRAÇÃO** e o usuário administrador inicial

---

## Endpoints

| Método | Rota | Acesso |
| :--- | :--- | :--- |
| `POST` | `/api/login` | Público |
| `POST` | `/api/admin/usuarios` | CEO |
| `GET` | `/api/admin/usuarios` | CEO, Administrador |
| `GET` | `/api/admin/usuarios/contar` | CEO, Administrador |
| `PUT` | `/api/admin/usuarios/{id}` | CEO |
| `DELETE` | `/api/admin/usuarios/{id}` | CEO |
| `POST` | `/api/sugestoes` | Autenticado |
| `GET` | `/api/sugestoes` | CEO, Administrador |

Os arquivos `teste_api.http` e `teste_api_user.http` trazem requisições prontas para testar a API pela IDE.

---

## Modelo de dados

O script `Backend/src/main/resources/schema.sql` cria o banco `Imperium`:

- **Setor** — nome, ramal e descrição
- **Usuario** — e-mail único, senha, papel, status, datas de início e de última alteração, setor
- **Sugestoes** — conteúdo, status e autor
- **Chamados** — título, descrição, prioridade, status, abertura e fechamento, usuário e setor
- **HistoricoChamado** — trilha de acompanhamento de cada chamado

---

## Como executar

**Pré-requisitos:** Java 21, MySQL 8 em execução e Maven (ou o wrapper do projeto).

**1. Criar o banco**

```bash
mysql -u root -p < Backend/src/main/resources/schema.sql
```

**2. Configurar as credenciais**

Em `Backend/src/main/resources/application.properties`, ajuste o acesso ao banco e a chave do JWT. O recomendado é ler tudo de variáveis de ambiente:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Imperium
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
jwt.expiration.ms=3600000
```

```bash
export DB_USER="seu_usuario"
export DB_PASSWORD="sua_senha"
export JWT_SECRET="uma-chave-longa-de-no-minimo-256-bits"
```

**3. Subir a aplicação**

```bash
cd Backend
./mvnw spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

**4. Abrir o frontend**

As telas ficam em `Frontend/Views` (`Login/login.html` e `ADM/adm.html`). Sirva a pasta em `http://localhost:3000` ou `http://localhost:5173` — são as origens liberadas no CORS.

**Testes**

```bash
cd Backend
./mvnw test
```

---

## Estrutura

```
Imperium/
├── Backend/
│   └── src/main/java/com/Imperium/
│       ├── Controllers/    # Authentication, Admin, Sugestao
│       ├── Services/       # regras de negócio e emissão de JWT
│       ├── Repositorys/    # Spring Data JPA
│       ├── Models/         # Usuario, Setor, Sugestoes
│       ├── DTOs/           # entrada e saída da API
│       ├── Enum/           # papelUsuario, StatusUsuario, statusSugestao
│       └── config/         # SecurityConfig, JwtFilter, DataInitializer
└── Frontend/
    ├── Views/              # telas de login e administração
    └── Resources/          # api.js e imagens
```

---

## Autor

**Roberto Dias** — Sistemas de Informação, UVV
[Portfólio](https://robertodias.vercel.app/) · [LinkedIn](https://www.linkedin.com/in/roberto-dias-rdfa23/)
