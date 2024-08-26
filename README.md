# API Básica em Java com Spring Boot

Este projeto é uma API básica desenvolvida em Java utilizando Spring Boot. A API fornece funcionalidades para autenticação, gerenciamento de usuários e posts. Usuários podem criar, listar e apagar posts, com regras de autorização baseadas em roles.

## Funcionalidades

- **Login**: Autentica um usuário e retorna um token de acesso.
- **Cadastrar Usuário**: Registra novos usuários na aplicação.
- **Listar Todos os Posts**: Lista todos os posts existentes na plataforma.
- **Criar Post**: Cria novos posts.
- **Apagar Post**: Permite apagar posts. Cada usuário pode apagar apenas seus próprios posts, enquanto o ADMIN pode apagar qualquer post.

## Instruções para Iniciar o Projeto

Para iniciar o projeto, siga os passos abaixo:

1. **Navegue até a pasta `docker` do projeto**:
   ```bash
   cd docker
   ```

2. **Execute o comando para subir os containers Docker**:
   ```bash
   docker compose up
   ```
3. **Starte o projeto Spring `SecurityApplication.java`.


4. **Acesse a API**: Após a execução dos comandos acima, a API estará disponível para uso.


## Endpoints

### 1. Login

**POST /login**

- **Descrição**: Autentica um usuário e retorna um token JWT para acesso às funcionalidades da API.
- **Requisição**:

  ```json
  {
    "username": "user",
    "password": "password"
  }
    ```
  Resposta:

    ```json
  {
  
  "accessToken": "JWT_TOKEN",
  "expiresIn": 300
  }
    ```  
**Status Codes:**
- 200 OK: Autenticação bem-sucedida.
- 401 Unauthorized: Usuário ou senha inválidos.

### 2. Cadastrar Usuário

**POST /users**

- **Descrição**: Registra um novo usuário.
- **Requisição**:
  ```json
  {
    "username": "newuser",
    "password": "newpassword"
  }
  ```
- **Resposta**: Status `201 Created`
- **Status Codes**:
  - `201 Created`: Usuário criado com sucesso.
  - `409 Conflict`: Nome de usuário já existe.

### 3. Listar Todos os Posts

**GET /feeds**

- **Descrição**: Lista todos os posts existentes com suporte à paginação.
- **Requisição**: Cabeçalho de autenticação com o token JWT. Suporta os seguintes parâmetros opcionais:
  - `page`: Número da página a ser retornada (valor padrão: `0`).
  - `pageSize`: Número de posts por página (valor padrão: `10`).


  Exemplo de requisição com paginação:
  ```
  GET /feeds?page=1&pageSize=5
  ```

- **Resposta**:
  ```json
  {
    "content": [
      {
        "tweetId": 1,
        "content": "Conteúdo do post",
        "username": "user"
      }
    ],
    "page": 1,
    "pageSize": 5,
    "totalPages": 3,
    "totalPosts": 15
  }
  ```
- **Status Codes**:
  - `200 OK`: Requisição bem-sucedida.

### 4. Criar Post

**POST /posts**

- **Descrição**: Cria um novo post.
- **Requisição**:
  ```json
  {
    "content": "Conteúdo do novo post"
  }
  ```
- **Resposta**: Status `201 Created`
- **Status Codes**:
  - `201 Created`: Post criado com sucesso.

### 5. Deletar Post

**DELETE /posts/{id}**

- **Descrição**: Apaga um post existente. Cada usuário pode apagar apenas seus próprios posts. O ADMIN pode apagar qualquer post.
- **Requisição**: Cabeçalho de autenticação com o token JWT.
- **Resposta**: Status `204 No Content`
- **Status Codes**:
  - `204 No Content`: Post apagado com sucesso.
  - `403 Forbidden`: Tentativa de apagar um post que não pertence ao usuário, se não for ADMIN.
  - `404 Not Found`: Post não encontrado.