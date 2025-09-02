# 🌐 Microsserviço de Registro de Serviços (Service Registry)

Este serviço é o **Registro e Descoberta de Serviços** (*Service Registry and Discovery*) da plataforma **AlgaDelivery**. Ele é implementado utilizando **Spring Cloud Netflix Eureka Server**.

## 🎯 Objetivo

A função deste componente é atuar como um "catálogo" ou "lista telefônica" para todos os outros microsserviços da arquitetura. Ele resolve o problema de saber onde cada serviço está localizado (endereço IP e porta) em um ambiente dinâmico.

-----

## ✨ Principais Responsabilidades

  * **Registro de Serviços**: Permite que outras aplicações (como `delivery-tracking`, `courier-management`, etc.) se registrem dinamicamente ao serem iniciadas, informando seu nome e localização de rede.
  * **Descoberta de Serviços**: Permite que clientes (principalmente o **API Gateway**) descubram onde encontrar outros serviços consultando o Eureka Server pelo nome da aplicação (ex: `courier-management`), sem precisar saber o endereço IP ou a porta exata.
  * **Monitoramento de Saúde (Health Check)**: O servidor espera receber sinais de vida periódicos (*heartbeats*) de cada instância registrada. Se uma instância para de enviar esses sinais, ela é considerada indisponível e removida do registro, evitando que o tráfego seja roteado para ela.

-----

## 🛠️ Tecnologias Utilizadas

  * **Spring Boot**
  * **Spring Cloud Netflix Eureka Server**: O framework que provê toda a funcionalidade do servidor de registro.

-----

## ⚙️ Configuração Chave

A configuração deste serviço é minimalista, mas possui duas propriedades cruciais:

  * **`eureka.client.register-with-eureka: false`**: Impede que o servidor tente se registrar como um cliente em si mesmo.
  * **`eureka.client.fetch-registry: false`**: Define que este servidor é a fonte da verdade e não deve buscar o registro de nenhum outro par.

-----

## 🚀 Como Executar

1.  **Ordem de Inicialização**: Este serviço deve ser o **primeiro** componente da infraestrutura a ser iniciado, pois os outros dependem dele para se registrarem.

2.  **Execute a aplicação Spring Boot**:

    ```bash
    ./mvnw spring-boot:run
    ```

3.  **Acesse o Dashboard**:
    O painel de controle do Eureka, que lista todos os serviços registrados e seus status, estará disponível no navegador em:
    **[http://localhost:8761](https://www.google.com/search?q=http://localhost:8761)**