# Controle de Estoque - Loja de Roupas

## Integrantes
- [Bruno de Carvalho]
- [João vitor Mulinari]
- 

## Tema e objetivo
Sistema web para controlar o estoque de uma loja de roupas. Permite cadastrar
categorias e produtos e registrar entradas e saídas, mantendo o histórico de
movimentações e impedindo que o estoque fique negativo.

## Tecnologias
- Java [25  e Spring Boot
- Spring Web e Spring Data JPA (Hibernate)
- MySQL 8
- ReactJS (Vite)
- JUnit 5 e Mockito

## Estrutura do projeto
```
database/schema.sql       script do banco
src/main/java/...         backend (controller, service, repository, domain, dto, exception, config)
src/test/java/...         testes automatizados
frontend/                 aplicação React
```

## Banco de dados
Banco escolhido: **MySQL 8**.

Relacionamentos: uma `categoria` tem muitos `produto`, e um `produto` tem muitas `movimentacao`.

### Como criar o banco
1. Instale o MySQL Server e deixe o serviço rodando.
2. Execute o script `database/schema.sql`. Duas opções:
    - Pelo terminal, na raiz do projeto: `mysql -u root -p < database/schema.sql`
    - Ou abra o arquivo no MySQL Workbench e clique no raio.
3. O script cria o banco `estoque_roupas` e as tabelas `categoria`, `produto` e `movimentacao`.

### Como configurar usuário e senha
A conexão está em `src/main/resources/application.properties` e lê o usuário e a senha de variáveis de ambiente:

| Variável | Significado | Padrão |
|---|---|---|
| `DB_USER` | usuário do MySQL | `root` |
| `DB_PASSWORD` | senha do MySQL | vazio |

Defina `DB_PASSWORD` antes de rodar. No Windows (cmd):
```
set DB_PASSWORD=suasenha
```
Na IntelliJ: Run > Edit Configurations > Environment variables > `DB_PASSWORD=suasenha`.

O projeto usa `spring.jpa.hibernate.ddl-auto=validate`: o Hibernate só confere se as entidades batem com as tabelas criadas pelo `schema.sql`.

## Como executar o backend
1. Banco criado e MySQL rodando.
2. Variável `DB_PASSWORD` definida.
3. Na raiz do projeto: `mvnw spring-boot:run` (ou rode a classe `LojaroupasApplication` pela IDE).
4. A API fica em `http://localhost:8080`.

## Como executar o frontend
Em outro terminal:
```
cd frontend
npm install
npm run dev
```
Acesse `http://localhost:5173`. O backend precisa estar rodando.

## Como executar os testes
```
mvnw test
```
Os testes de regra de negócio não precisam do banco. O teste `LojaroupasApplicationTests` sobe a aplicação inteira e precisa do MySQL e da variável `DB_PASSWORD`.

## Principais funcionalidades
- Cadastro, listagem e exclusão de categorias
- Cadastro, listagem e busca de produtos, e alteração de preço
- Registro de entradas e saídas de estoque, com histórico por produto
- Painel com total de produtos, unidades, valor em estoque e produtos com estoque baixo

## Regras de negócio
- Uma saída não pode deixar o estoque negativo (422).
- Não é possível excluir uma categoria que possui produtos (422).
- Não é possível cadastrar duas categorias com o mesmo nome (422).
- Quantidade de movimentação e preço devem ser maiores que zero (400).

## Endpoints
| Método | Rota | Descrição |
|---|---|---|
| POST | /api/categorias | Cadastrar categoria |
| GET | /api/categorias | Listar categorias |
| GET | /api/categorias/{id} | Buscar categoria |
| DELETE | /api/categorias/{id} | Excluir categoria |
| POST | /api/produtos | Cadastrar produto |
| GET | /api/produtos | Listar produtos |
| GET | /api/produtos/{id} | Buscar produto |
| PATCH | /api/produtos/{id}/preco | Alterar preço |
| POST | /api/movimentacoes | Registrar entrada ou saída |
| GET | /api/movimentacoes/produto/{id} | Histórico do produto |