# 🧱 Arquitetura do Sistema - Tech Challenge

---

## 📌 Visão Geral

O sistema foi desenvolvido com base nos princípios de **Clean Architecture** e **Arquitetura Hexagonal (Ports & Adapters)**, promovendo separação clara de responsabilidades e baixo acoplamento entre as camadas.

A estrutura do projeto está organizada da seguinte forma:

* **Infra (Web / Controllers)** → Entrada da aplicação (REST APIs)
* **Core (Use Cases)** → Regras de negócio

  * `in` → contratos de entrada (Input Ports)
  * `impl` → implementação dos casos de uso
  * `out` → contratos de saída (Gateways)
* **Domain** → Entidades, Value Objects e Regras de domínio
* **Infra (Persistence / External / Security)** → Implementações técnicas (MongoDB, APIs externas, segurança)

---

## 🧭 Padrões Arquiteturais Utilizados

* **Clean Architecture**
* **Ports & Adapters (Hexagonal)**
* **CQRS (parcial)** – separação de leitura e escrita
* **API REST**
* **Integração com serviços externos (Payment Gateway)**

---

## 🗄️ Banco de Dados

O sistema utiliza **MongoDB (NoSQL)** como banco de dados principal.

### 🎯 Justificativas:

* Flexibilidade na modelagem de documentos
* Melhor adaptação para agregados (ex: Order com itens)
* Redução de complexidade (sem joins)
* Alta escalabilidade horizontal

---

## 🔄 Fluxo Principal de Funcionamento

### 🛒 Criação de Pedido

1. Cliente envia requisição `POST /orders`
2. **OrderController** recebe a requisição
3. Converte DTO → domínio (mapper)
4. Chama `CreateOrderUseCase` (camada core)
5. O UseCase:

   * valida os dados
   * cria a entidade Order
   * utiliza `OrderGateway` para persistência
6. O adapter MongoDB salva o pedido
7. Retorna resposta ao cliente

---

### 💳 Processamento de Pagamento

1. Cliente envia `POST /orders/{orderId}/payments`
2. **PaymentController** recebe a requisição
3. Chama `ProcessPaymentUseCase`
4. O UseCase:

   * valida o pedido
   * chama `PaymentGateway`
5. O adapter externo realiza a integração com o serviço de pagamento
6. O status do pedido é atualizado
7. Retorna resposta ao cliente

---

## 🔍 CQRS (Command Query Responsibility Segregation)

O sistema aplica CQRS de forma parcial, separando operações de leitura e escrita.

### ✍️ Commands (escrita)

* `POST /users`
* `POST /orders`
* `POST /payments`

### 🔍 Queries (leitura)

* `GET /users`
* `GET /orders`
* `GET /queries/users/{userId}/addresses`

👉 Destaque: uso de `user-address-query-controller` para leitura otimizada.

### 🎯 Benefícios:

* Melhor organização do código
* Otimização de consultas
* Escalabilidade independente

---

## ⚡ Uso de Eventos (Evolução Arquitetural)

O sistema está preparado para evolução com **event-driven architecture**.

### Exemplos de eventos:

* `OrderCreatedEvent`
* `PaymentProcessedEvent`

### Fluxo com eventos:

1. Pedido é criado
2. Evento é disparado
3. Serviço de pagamento reage ao evento

### 🎯 Benefícios:

* Desacoplamento entre módulos
* Facilidade de extensão (ex: notificações)
* Maior resiliência

---

## 🔐 Segurança

A camada de segurança é isolada na infraestrutura:

* `SecurityConfig`
* `JwtTokenProviderAdapter`
* `BCryptPasswordEncoderAdapter`

### 🎯 Benefícios:

* Desacoplamento da lógica de negócio
* Facilidade de troca de implementação
* Proteção de endpoints via JWT

---

## 🔁 Mapeamento de Dados (Mappers)

O sistema utiliza mapeadores em duas camadas:

### 1. Web Layer

* Conversão entre DTO ↔ domínio

### 2. Persistence Layer

* Conversão entre domínio ↔ MongoDB

### 🎯 Benefícios:

* Isolamento do domínio
* Independência de frameworks
* Facilidade de manutenção

---

## 🛡️ Pontos de Resiliência

A arquitetura apresenta diversos mecanismos de resiliência:

### 🔹 Desacoplamento por interfaces (Gateways)

* Permite troca de banco ou serviços externos sem impacto no core

### 🔹 Isolamento de infraestrutura

* Falhas externas não afetam diretamente a lógica de negócio

### 🔹 Tratamento de exceções centralizado

* Evita exposição de detalhes internos

### 🔹 Integração externa desacoplada

* Gateway de pagamento isolado em adapter

### 🔹 Escalabilidade com MongoDB

* Melhor performance para leitura e escrita

### 🔹 Preparação para retry e circuit breaker

* Pode ser integrado com bibliotecas como Resilience4j

---

## 🚀 Considerações Finais

A arquitetura adotada garante:

* Alta coesão e baixo acoplamento
* Facilidade de testes
* Escalabilidade
* Facilidade de evolução

O uso de padrões como **Clean Architecture, CQRS e integração por eventos** posiciona o sistema como uma solução moderna e preparada para crescimento.
