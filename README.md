# Notification Queue API

API REST com mensageria RabbitMQ para demonstrar publicacao, consumo assíncrono e processamento de eventos.

## Funcionalidades

- Recebe solicitacoes de notificacao via endpoint REST.
- Salva a notificacao com status `QUEUED`.
- Publica uma mensagem no RabbitMQ.
- Consumer processa a mensagem e marca como `PROCESSED`.
- Listagem de notificacoes por status.
- PostgreSQL e RabbitMQ via Docker Compose.
- Painel RabbitMQ Management.

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
- Maven

## Como Executar Localmente

```bash
mvn clean package
java -jar target/notification-queue-api-0.0.1-SNAPSHOT.jar
```

No modo local, o listener do RabbitMQ fica desligado por padrao para a aplicacao subir sem RabbitMQ.

## Como Executar com Docker

```bash
cp .env.example .env
docker compose up --build
```

API:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/docs
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

## Aprendizados Demonstrados

- Publicacao de eventos em fila.
- Consumo assíncrono com `@RabbitListener`.
- Separacao entre receber requisicao e processar trabalho.
- Rastreamento de status de processamento.
- API, banco e broker subindo com Docker Compose.
