![Java 21](https://img.shields.io/badge/Java-21-blue?logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build%20Tool-C71A36?logo=apachemaven&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?logo=docker&logoColor=white)
[![Continuous Integration With Github Actions](https://github.com/sahid-sousa/rest-with-spring-boot/actions/workflows/continuous-integration.yml/badge.svg)](https://github.com/sahid-sousa/rest-with-spring-boot/actions/workflows/continuous-integration.yml)

# rest-with-spring-boot

Projeto de API REST desenvolvido com **Spring Boot** e configurada para rodar em containers Docker utilizando `docker-compose`.

## 📦 Tecnologias

- Java 21
- Spring Boot
- MySQL 8.0
- Docker & Docker Compose

---

## 🚀 Como executar

### Pré-requisitos

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

### 📁 Estrutura esperada

Certifique-se de que o projeto foi compilado e que o `.jar` está presente na pasta `target/`.

```
./mvnw clean package -DskipTests
```

> Isso gerará um arquivo `.jar` dentro da pasta `target/`, que será utilizado pelo Docker.

---

### ▶️ Subindo os containers

Execute o comando abaixo na raiz do projeto (onde estão o `Dockerfile` e o `docker-compose.yml`):

```
docker-compose up --build
```

Esse comando fará o seguinte:

1. Criará e iniciará um container MySQL (porta 3306).
2. Criará e iniciará o container da aplicação Spring Boot (porta 8080), após o banco estar pronto.

---

### 🔍 Acessando a aplicação

Após a execução bem-sucedida, a API estará acessível em:

```
http://localhost:8080/swagger-ui/index.html
```

---

## ⚙️ Configurações da aplicação

A aplicação está configurada para utilizar as seguintes variáveis de ambiente:

| Variável              | Valor                                            |
|-----------------------|--------------------------------------------------|
| `DATASOURCE_URL`      | `jdbc:mysql://db:3306/rest_with_spring_boot?...` |
| `DATASOURCE_USERNAME` | `admin`                                          |
| `DATASOURCE_PASSWORD` | `admin`                                          |
| `DATASOURCE_DBCREATE` | `update`                                         |

> Essas variáveis são passadas automaticamente via `docker-compose.yml`.

---

## 🛑 Finalizando os containers

Para parar e remover os containers, utilize:

```
docker-compose down
```

---

## 🐳 Sobre o Dockerfile

O `Dockerfile` utilizado é simples e eficiente:

```Dockerfile
FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Ele copia o `.jar` gerado para dentro da imagem e executa o Spring Boot diretamente com `java -jar`.
