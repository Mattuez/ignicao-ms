# 🚪 Microsserviço API Gateway

Este serviço é o **ponto único de entrada** (*Single Point of Entry*) para a plataforma **AlgaDelivery**. Construído com **Spring Cloud Gateway**, ele é responsável por receber todas as requisições externas e roteá-las para os microsserviços internos apropriados.

## 🎯 Objetivo

O objetivo principal do API Gateway é centralizar a gestão do tráfego, simplificar a arquitetura para os clientes consumidores (front-end, aplicativos móveis, etc.) e aplicar políticas transversais, como resiliência e transformação de dados, em um único local.

-----

## ✨ Principais Responsabilidades

* **Roteamento Inteligente**: Direciona as requisições para os microsserviços `delivery-tracking` ou `courier-management` com base no caminho (path) da URL, utilizando a descoberta de serviços do Eureka para encontrar as instâncias ativas.
* **Centralização da Resiliência**: Implementa padrões de resiliência de forma centralizada usando **Resilience4j**:
    * **Retry**: Tenta reenviar requisições que falharam para o serviço de `delivery-tracking`.
    * **Circuit Breaker**: Protege o sistema contra falhas em cascata, interrompendo o envio de tráfego para o serviço de `delivery-tracking` se este apresentar falhas contínuas.
* **API de Fachada (Facade Pattern)**: Expõe endpoints públicos (`/public/...`) mais simples e amigáveis, que são internamente reescritos (`RewritePath`) para os endpoints privados (`/api/v1/...`) do microsserviço de `courier-management`.
* **Transformação de Resposta**: Para os endpoints públicos, o gateway filtra o corpo da resposta (`ResponseBody`) para **remover atributos sensíveis ou desnecessários** (como `phone`, `pendingDeliveries`, etc.) antes de enviá-los ao cliente.

-----

## 🛠️ Tecnologias Utilizadas

* **Spring Boot**
* **Spring Cloud Gateway**: Para a implementação do gateway.
* **Spring Cloud Netflix Eureka Client**: Para a descoberta de serviços.
* **Resilience4j**: Para a implementação dos padrões de Circuit Breaker e Retry.

-----

## 🗺️ Configuração de Rotas

O gateway possui as seguintes rotas configuradas:

### Para o Microsserviço `delivery-tracking`

* **ID da Rota**: `delivery-tracking-rout`
* **Caminho (`Path`)**: `/api/v1/deliveries/**`
* **Destino**: `lb://delivery-tracking` (roteamento com load balance)
* **Filtros Aplicados**:
    * **Retry**:
        * **Tentativas**: 3.
        * **Gatilhos**: Respostas com status `500 INTERNAL_SERVER_ERROR` ou `502 BAD_GATEWAY`.
        * **Métodos**: Aplicado apenas para `GET` e `PUT`.
        * **Backoff**: Exponencial, começando com 10ms.
    * **Circuit Breaker**:
        * **Nome**: `delivery-tracking-route-circuit-breaker`.
        * **Gatilhos**: Respostas com status `500`, `502` ou `504`.
        * **Configuração**: O circuito abre após 50% de falhas em uma janela de 10 chamadas e permanece aberto por 5 segundos.

### Para o Microsserviço `courier-management`

1.  **Rota Privada (API Interna)**

    * **ID da Rota**: `courier-management-route`
    * **Caminho (`Path`)**: `/api/v1/couriers/**`
    * **Destino**: `lb://courier-management`
    * **Descrição**: Rota padrão que expõe a API interna do serviço de entregadores.

2.  **Rota Pública (Listagem)**

    * **ID da Rota**: `courier-management-public-route`
    * **Caminho (`Path`)**: `/public/couriers` (apenas `GET`)
    * **Destino**: `lb://courier-management`
    * **Filtros**:
        * `RewritePath`: Reescreve `/public/couriers` para `/api/v1/couriers`.
        * `RemoveJsonAttributesResponseBody`: Remove os campos `pendingDeliveries`, `pendingDeliveriesQuantity` e `phone` da resposta.

3.  **Rota Pública (Busca por ID)**

    * **ID da Rota**: `courier-management-public-segments-route`
    * **Caminho (`Path`)**: `/public/couriers/**` (apenas `GET`)
    * **Destino**: `lb://courier-management`
    * **Filtros**:
        * `RewritePath`: Reescreve `/public/couriers/{id}` para `/api/v1/couriers/{id}`.
        * `RemoveJsonAttributesResponseBody`: Remove os mesmos campos da resposta.

-----

## 🚀 Como Executar

1.  **Pré-requisitos**:

    * Java (JDK)
    * Maven ou Gradle
    * Uma instância do **Eureka Server** em execução.
    * Os microsserviços (`delivery-tracking`, `courier-management`) devem estar registrados no Eureka.

2.  **Execute a aplicação Spring Boot**:

    ```bash
    ./mvnw spring-boot:run
    ```

3.  O gateway estará disponível na porta `9999`. Todas as requisições para a plataforma devem ser direcionadas para `http://localhost:9999`.