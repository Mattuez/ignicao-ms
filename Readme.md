# 🚀 AlgaDelivery - Projeto de Estudos de Microsserviços

Bem-vindo ao repositório do projeto AlgaDelivery\! Este projeto foi desenvolvido como parte do curso **Ignição Microsserviços** e serve como uma implementação prática e aprofundada dos conceitos e padrões que governam uma arquitetura de software moderna, distribuída e resiliente.

## 🎯 Sobre o Projeto

O AlgaDelivery simula uma plataforma de logística e entregas, onde cada domínio de negócio é encapsulado em seu próprio microsserviço. O objetivo é explorar os desafios e as soluções relacionados à comunicação, resiliência, descoberta de serviços e gerenciamento de dados em um ecossistema distribuído.

Além do conteúdo coberto pelo curso, este repositório é também um *playground* para aprofundar e expandir o escopo original, agregando funcionalidades que trazem desafios do mundo real. Destaques dessas melhorias incluem a **integração real com a API do Google Maps** para cálculo de distância e o desenvolvimento de um **serviço de pagamentos com PIX**.

-----

## 🏛️ Arquitetura da Plataforma

A plataforma utiliza a arquitetura de microsserviços, onde cada serviço tem um escopo de responsabilidade bem definido e se comunica com os demais através de protocolos síncronos (REST) e assíncronos (eventos com Apache Kafka).

### Componentes da Arquitetura:

* **🌐 Service Registry (`service-registry`)**

    * **Tecnologia:** Spring Cloud Netflix Eureka.
    * **Descrição:** Atua como o "catálogo" da plataforma. Todos os outros microsserviços se registram nele, permitindo a descoberta dinâmica de suas localizações (IP e porta) na rede.

* **🚪 API Gateway (`gateway`)**

    * **Tecnologia:** Spring Cloud Gateway.
    * **Descrição:** É o ponto único de entrada para todas as requisições externas. Ele é responsável por rotear o tráfego para o serviço correto, além de aplicar padrões de resiliência cruciais como **Retry** e **Circuit Breaker** com Resilience4j.

* **🚚 Delivery Tracking (`delivery-tracking`)**

    * **Descrição:** O coração da aplicação. Gerencia todo o ciclo de vida de uma entrega, desde sua criação até a finalização. É a fonte de eventos de domínio importantes, como `DeliveryPlacedEvent` e `DeliveryFulfilledEvent`, que são publicados no Kafka para notificar outros sistemas.

* **🛵 Courier Management (`courier-management`)**

    * **Descrição:** Gerencia o cadastro e a lógica de negócio relacionada aos entregadores. Este serviço consome eventos do Kafka para reagir a novas entregas, atribuindo-as a entregadores disponíveis, e também para processar a finalização das corridas.

### Fluxo de Comunicação:

1.  O **Cliente** (ex: um app front-end) faz uma requisição para o **API Gateway**.
2.  O **API Gateway** consulta o **Service Registry** para descobrir a localização do microsserviço de destino.
3.  A requisição é encaminhada para o serviço, por exemplo, o **Delivery Tracking**.
4.  O **Delivery Tracking**, ao mudar o estado de uma entrega, publica um evento no **Apache Kafka**.
5.  O **Courier Management**, que está escutando o tópico, consome o evento e executa a sua lógica de negócio (ex: atribuir um entregador).

-----

## 🧠 Conceitos e Padrões Aplicados

Este projeto foi uma oportunidade para estudar e aplicar diversos conceitos do desenvolvimento de software moderno:

* **Domain-Driven Design (DDD):**
    * **Design Estratégico:** Mapeamento de Subdomínios e definição de Bounded Contexts.
    * **Design Tático:** Implementação de Aggregates, Entities e Value Objects para criar um Rich Domain Model.
* **Comunicação Assíncrona:** Uso de Arquitetura Orientada a Eventos (Event-Driven Architecture) com Apache Kafka para baixo acoplamento e escalabilidade.
* **Padrões de Resiliência:**
    * **Circuit Breaker:** Para evitar falhas em cascata.
    * **Retry:** Para lidar com falhas transitórias.
    * **Timeout:** Para evitar que o sistema fique bloqueado esperando por respostas.
* **Padrões de Microsserviços:**
    * **API Gateway:** Para centralizar o acesso e as políticas transversais.
    * **Service Discovery:** Para localização dinâmica de serviços.
* **Infraestrutura como Código:** Utilização do Docker e Docker Compose para provisionar e gerenciar a infraestrutura de desenvolvimento.

-----

## 🛠️ Tecnologias Utilizadas

* **Linguagem/Framework:** Java 17, Spring Boot 3
* **Spring Cloud:** Gateway, Netflix Eureka
* **Comunicação:** Spring for Apache Kafka, Spring RestClient (HTTP Interface)
* **Persistência de Dados:** Spring Data JPA, Hibernate
* **Resiliência:** Resilience4j
* **Containerização:** Docker, Docker Compose
* **Integrações Externas:** Google Maps API (no `delivery-tracking`)

-----

## 🚀 Como Executar a Plataforma

1.  **Pré-requisitos:**

    * Java (JDK) 17+
    * Docker e Docker Compose
    * Uma chave de API do Google Maps (necessária para o serviço `delivery-tracking`).

2.  **Configuração:**

    * Clone este repositório.
    * Configure a chave da API do Google Maps no `application.properties` do serviço `delivery-tracking`.

3.  **Inicialização:**

    * **Infraestrutura:** Suba os containers do Kafka e demais dependências:

      ```bash
      docker-compose up -d
      ```

    * **Aplicações (Ordem Recomendada):**

        1.  `service-registry`
        2.  `delivery-tracking` e `courier-management` (a ordem entre eles não importa)
        3.  `gateway`

    * Cada serviço pode ser iniciado com o comando Maven: `./mvnw spring-boot:run`

4.  Após a inicialização, a plataforma estará acessível através do API Gateway na porta `9999`.

-----

## 🛣️ Próximos Passos e Roadmap

Este projeto é um campo de estudos em constante evolução, com funcionalidades sendo adicionadas além do escopo original do curso.

### ✅ Implementado

* **Integração com Google Maps**: O serviço `delivery-tracking` foi aprimorado com uma integração real à **API Google Maps Distance Matrix**. Isso substitui o cálculo de distância fixo por uma estimativa precisa baseada nos endereços do remetente e destinatário, tornando a simulação do frete e do tempo de entrega muito mais realista.

### 🚧 Em Desenvolvimento

* **💳 Microsserviço `payment-processor`**: Está em andamento o desenvolvimento de um novo serviço dedicado ao processamento de pagamentos. A primeira funcionalidade planejada é a **integração com o sistema de pagamentos PIX**, que permitirá explorar desafios como transações distribuídas, segurança em pagamentos e comunicação com gateways de pagamento externos.