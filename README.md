# Notification Queue API

[![CI](https://github.com/ViiniDev/notification-queue-api/actions/workflows/ci.yml/badge.svg)](https://github.com/ViiniDev/notification-queue-api/actions/workflows/ci.yml)

API REST com mensageria RabbitMQ para demonstrar publicacao, consumo assincrono e processamento de eventos.

## Funcionalidades

- Recebe solicitacoes de notificacao via endpoint REST.
- Salva a notificacao com status `QUEUED`.
- Publica uma mensagem no RabbitMQ.
- Consumer processa a mensagem e marca como `PROCESSED`.
- Listagem de notificacoes por status.
- PostgreSQL e RabbitMQ via Docker Compose.
- Painel RabbitMQ Management.
- Testes de integracao com RabbitTemplate mockado.
- CI com GitHub Actions para testes e build Docker.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web
- Spring AMQP
- RabbitMQ
- Spring Data JPA
- PostgreSQL
- H2 Database
- Docker e Docker Compose
- Swagger/OpenAPI
- JUnit, MockMvc e Mockito
- GitHub Actions
- Maven

## Como Executar Localmente

```bash
mvn clean package
java -jar target/notification-queue-api-0.0.1-SNAPSHOT.jar
```

No modo local, o listener do RabbitMQ fica desligado por padrao para a aplicacao subir sem RabbitMQ.

Swagger:

```text
http://localhost:8080/docs
```

## Como Executar com Docker

```bash
cp .env.example .env
docker compose up --build
```

API:

```text
http://localhost:8080
```

RabbitMQ Management:

```text
http://localhost:15672
```

## Endpoints Principais

```http
POST /api/notifications
GET /api/notifications
GET /api/notifications?status=PROCESSED
```

## Exemplo

```json
{
  "recipient": "vinicius@email.com",
  "message": "Seu pedido foi criado.",
  "channel": "EMAIL"
}
```

## Testes E CI

```bash
mvn test
```

Os testes validam:

- criacao de notificacao com status `QUEUED`;
- publicacao da mensagem no RabbitMQ via `RabbitTemplate`;
- listagem por status;
- processamento para status `PROCESSED`;
- validacao de requisicao invalida.

O workflow em `.github/workflows/ci.yml` executa os testes Maven e o build Docker via Docker Compose.

## Aprendizados Demonstrados

- Publicacao de eventos em fila.
- Consumo assincrono com `@RabbitListener`.
- Separacao entre receber requisicao e processar trabalho.
- Rastreamento de status de processamento.
- API, banco e broker subindo com Docker Compose.
- Testes de integracao sem depender de broker externo.
- Pipeline de CI para validar testes e build Docker.
