# User Manager API

Trata-se  de uma API voltada para  a capacidade de servir e possibilitar uma gestão e autenticação de usuários. Em nossos serviços consta autenticação construída com **Oauth2**, exclusão de usuários utilizando os princípios de **soft delete**, **validações** de campos obrigatórios, geração de **tokens de acesso.** Contudo, todas as regras de negócio são validadas por meio de **testes unitários** implementados para a camada de serviço.

### **📋 Pré-requisitos**

Para executar o projeto, será necessário instalar os seguintes programas:

<aside>
💡 - JDK 11                                                                                                                                                            -Maven 3.8.1                                                                                                                                      -Docker Desktop

</aside>

****🔧 Construção****

Para construir o projeto com o Maven, executar os comando abaixo:

```java

mvn clean install
```

O comando irá baixar todas as dependências do projeto e criar um diretório *target*
 com os artefatos construídos, que incluem o arquivo jar do projeto. Além disso, serão executados os testes unitários, e se algum falhar, o Maven exibirá essa informação no console.

## Banco de Dados

Para criar um banco de dados para o sistema basta executar o comando a abaixo:

```java
docker-compose up
```

## Testes

Para rodar os testes, utilize o comando abaixo:

```java
mvn test
```

## Documentação

Para ter acesso a documentação  acesse os seguintes arquivos:

- **user-manager.postman_collection.json**  → Trás todos as requisições configuradas para utilização.
- **user-manager-environmnent-local.postman_environment.json** → Trás o environment para ser utilizado com as requisições.
- **Swagger Documentation.pdf** → Trás uma visualização ampla de todos os endpoints presentes na user-api e das classes envolvidas.
