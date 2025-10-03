# Sistema de Processamento de Vendas - Varejo Rápido  

## Descrição  
Este é um sistema desenvolvido por **Renan Moreira da Silva**, para a disciplina de Programação Orientada a Objetos (POO) do curso de **Sistemas de Informação - IFMA**.  

O sistema tem como objetivo realizar o **processamento e a gestão de vendas** em ambientes de varejo. Ele permite importar arquivos de vendas em lote, gerenciar produtos, clientes e vendedores, além de disponibilizar relatórios de vendas de forma eficiente por meio de uma **API REST**.  

## Funcionalidades  
- Processar Arquivos de Vendas: Importação e leitura de arquivos em formato de posição fixa  
- Gestão de Produtos: Cadastro e atualização automática de produtos  
- Gestão de Clientes/Vendedores: Cadastro e atualização de clientes e vendedores  
- Registro de Vendas: Armazenamento completo das transações  
- API REST: Endpoints para consulta e processamento de dados  
- Relatórios de Vendas: Visualização com detalhes de produtos, clientes, valores e totais  

## Tecnologias Utilizadas  
🖥️ Java 23  
📂 MySQL 8.0  
🌐 Spring Boot 3.5.6  
📊 Hibernate 6.6.29  
🔗 Maven  
🛠️ IntelliJ IDEA  
📡 Postman  

## Como Executar  

### Requisitos  
- Java JDK 21+  
- MySQL 8.0+  
- IntelliJ IDEA (ou outra IDE compatível)  
- Postman  

### Configuração do Banco de Dados  
```sql
CREATE DATABASE vendas_db;
USE vendas_db;
```
## Estrutura do Projeto
```
vendas-api/
│
├── src/main/java/br/com/trabalho/vendas_api/
│   ├── controller/
│   ├── service/
│   ├── model/
│   ├── repository/
│   ├── dto/
│   └── VendasApiApplication.java
│
├── src/main/resources/
│   └── application.properties
│
└── vendas_dia_2025-09-29.dat
```
## Funcionamento do Sistema
- Leitura do arquivo
- Extração dos campos
- Validação dos dados
- Persistência no banco
- Disponibilização via API REST


