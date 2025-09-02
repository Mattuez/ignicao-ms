# 🚚 Microsserviço de Rastreamento de Entregas (Delivery Tracking)

Este microsserviço é um componente central do projeto **AlgaDelivery**, desenvolvido durante o curso **Ignição Microsserviços**. Ele é responsável por orquestrar e gerenciar todo o ciclo de vida de uma entrega.

## 🎯 Objetivo

O objetivo deste serviço é atuar como a autoridade central para a criação, o gerenciamento de status e o rastreamento de entregas. Ele lida com a lógica de negócio desde o momento em que um pedido é rascunhado até a sua finalização, comunicando os eventos importantes para o resto da plataforma.

-----

## ✨ Principais Responsabilidades

* **Gestão de Entregas**: Fornece endpoints para criar, editar e consultar entregas.
* **Controle de Fluxo (State Machine)**: Gerencia o ciclo de vida de uma entrega através de status bem definidos: `DRAFT` -\> `WAITING_FOR_COURIER` -\> `IN_TRANSIT` -\> `DELIVERED`.
* **Cálculo de Custos e Prazos**:
    * **Integração com Google Maps**: Calcula a distância real e a estimativa de tempo de entrega entre o remetente e o destinatário utilizando a **Distance Matrix API do Google Maps**.
    * **Comunicação Síncrona**: Consulta o microsserviço de **Gestão de Entregadores** para obter o valor do repasse (frete) a ser pago ao entregador.
* **Publicação de Eventos**: Atua como um produtor de eventos, publicando as principais mudanças de estado de uma entrega (ex: "enviada", "coletada", "finalizada") em um tópico Kafka para que outros serviços possam reagir.

-----

## 🛠️ Tecnologias Utilizadas

* **Java e Spring Boot**: Plataforma de desenvolvimento principal.
* **Spring Web**: Para a criação da API REST.
* **Spring Data JPA**: Para persistência de dados.
* **Spring for Apache Kafka**: Para publicação de eventos de integração.
* **Spring HTTP Interface (`@HttpExchange`)**: Para a comunicação síncrona e declarativa com outras APIs.
* **Spring Cloud LoadBalancer**: Para balanceamento de carga nas chamadas entre serviços.
* **Resilience4j**: Para a implementação de padrões de resiliência como **Retry** e **Circuit Breaker** nas chamadas HTTP. [cite: 80, 81, 84]
* **Google Maps Services for Java**: Para integração com a API do Google Maps.
* **Lombok**: Para a redução de código boilerplate.

-----

## 🔌 API Endpoints

O serviço expõe os seguintes endpoints REST sob o caminho base `/api/v1/deliveries`:

| Método   | Rota                           | Descrição                                                                  |
| :------- | :----------------------------- | :------------------------------------------------------------------------- |
| `POST`   | `/`                            | Cria uma nova entrega com o status `DRAFT`.                                |
| `PUT`    | `/{deliveryId}`                | Atualiza os dados de uma entrega que ainda está em rascunho.               |
| `GET`    | `/`                            | Lista todas as entregas de forma paginada.                                 |
| `GET`    | `/{deliveryId}`                | Busca uma entrega específica pelo seu ID.                                  |
| `POST`   | `/{deliveryId}/placement`      | Altera o status da entrega para `WAITING_FOR_COURIER` e publica o evento.  |
| `POST`   | `/{deliveryId}/pickups`        | Informa que a entrega foi coletada, alterando o status para `IN_TRANSIT`.  |
| `POST`   | `/{deliveryId}/completion`     | Finaliza a entrega, alterando o status para `DELIVERED` e publica o evento.|

-----

## 📢 Comunicação Assíncrona (Eventos Publicados)

Este microsserviço é uma fonte de eventos para a plataforma. Ele publica mensagens no seguinte tópico Kafka:

### Tópico de Publicação

* `deliveries.v1.events`

### Eventos Gerados

* **`DeliveryPlacedEvent`**: Publicado quando uma entrega é oficialmente solicitada (sai do estado de rascunho). É o gatilho para o serviço de **Gestão de Entregadores** atribuir um entregador.
* **`DeliveryPickedUpEvent`**: Publicado quando um entregador coleta a entrega.
* **`DeliveryFulfilledEvent`**: Publicado quando a entrega é concluída com sucesso. É o gatilho para o serviço de **Gestão de Entregadores** atualizar o status do entregador.

-----

## 🔄 Comunicação Síncrona (Integrações)

Para realizar o cálculo de custos, este serviço consome APIs de outros serviços:

### Serviço Consumido

* **`courier-management` (Gestão de Entregadores)**

### Endpoint Utilizado

* `POST /api/v1/couriers/payout-calculation`

### Finalidade

* Obter o valor do repasse que deve ser pago ao entregador com base na distância calculada pelo Google Maps. [cite\_start]A comunicação é protegida com padrões de resiliência para evitar falhas em cascata.

-----

## 🚀 Como Executar

1.  **Pré-requisitos**:

    * Java (JDK)
    * Maven ou Gradle
    * Docker e Docker Compose
    * Uma chave de API do **Google Maps** com a "Distance Matrix API" ativada.

2.  **Configuração**:

    * Configure sua chave da API do Google Maps como uma variável de ambiente ou no arquivo `application.properties`:
      ```properties
      google-maps.api-key=SUA_CHAVE_API_AQUI
      ```

3.  **Suba as dependências (Banco de dados, Kafka, etc.)**:

    ```bash
    docker-compose up -d
    ```

4.  **Execute a aplicação Spring Boot**:

    ```bash
    ./mvnw spring-boot:run
    ```