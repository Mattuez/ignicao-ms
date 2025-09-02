# 🛵 Microsserviço de Gestão de Entregadores (Courier Management)

Este é um microsserviço desenvolvido como parte do projeto de estudos **AlgaDelivery**, do curso **Ignição Microsserviços**.

## 🎯 Objetivo

O principal objetivo deste serviço é gerenciar todas as informações e operações relacionadas aos entregadores da plataforma. Ele atua como a fonte da verdade para os dados dos entregadores e controla a lógica de atribuição e finalização de entregas.

-----

## ✨ Principais Responsabilidades

* **Gerenciamento de Entregadores**: Fornece operações de CRUD (Criação, Leitura, Atualização) para os entregadores.
* **Cálculo de Frete**: Expõe um endpoint para calcular o valor do repasse a um entregador com base na distância percorrida.
* **Atribuição de Entregas**: Reage a eventos de novas entregas, atribuindo-as a um entregador disponível seguindo uma lógica de negócio (o entregador que finalizou uma entrega há mais tempo é o próximo a ser selecionado).
* **Finalização de Entregas**: Processa eventos para marcar uma entrega como finalizada no sistema, atualizando o status do entregador correspondente.

-----

## 🛠️ Tecnologias Utilizadas

* **Java e Spring Boot**: Plataforma de desenvolvimento principal.
* **Spring Web**: Para a criação dos endpoints da API REST.
* **Spring Data JPA**: Para a persistência de dados com o banco de dados.
* **Spring for Apache Kafka**: Para a comunicação assíncrona baseada em eventos.
* **Spring Cloud Discovery Client**: Para registro e descoberta de serviços na arquitetura (Service Discovery).
* **Lombok**: Para reduzir o código boilerplate.
* **Docker**: Para a orquestração de dependências como banco de dados e Kafka em ambiente de desenvolvimento.

-----

## 🔌 API Endpoints

O serviço expõe os seguintes endpoints REST sob o caminho base `/api/v1/couriers`:

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/` | Cadastra um novo entregador. |
| `PUT` | `/{courierId}` | Atualiza os dados de um entregador existente. |
| `GET` | `/` | Lista todos os entregadores de forma paginada. |
| `GET` | `/{courierId}` | Busca um entregador específico pelo seu ID. |
| `POST`| `/payout-calculation`| Calcula o valor de repasse para uma entrega com base na distância em KM. |

-----

## 📢 Comunicação Assíncrona (Eventos)

Este microsserviço atua como um consumidor de eventos para se manter sincronizado com o restante da plataforma, utilizando **Apache Kafka**.

### Tópico Consumido

* `deliveries.v1.events`

### Eventos Processados

* **`DeliveryPlacedIntegrationEvent`**: Ao receber este evento, o serviço aciona a `CourierDeliveryService` para encontrar um entregador disponível e atribuir a nova entrega a ele.
* **`DeliveryFulfilledIntegrationEvent`**: Após a notificação de que uma entrega foi finalizada, o serviço atualiza o status do entregador, registrando a conclusão e deixando-o disponível para novas atribuições.

-----

## 🚀 Como Executar

1.  **Pré-requisitos**:

    * Java (JDK)
    * Maven ou Gradle
    * Docker e Docker Compose

2.  **Suba as dependências (Banco de dados, Kafka, etc.)**:

    ```bash
    docker-compose up -d
    ```

3.  **Execute a aplicação Spring Boot**:

    ```bash
    ./mvnw spring-boot:run
    ```

4.  O serviço estará disponível em `http://localhost:8081` (ou na porta configurada).