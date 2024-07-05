## Objetivos do projeto

Aprender a implementar sistema de mensageria utilizando Spring e RabbitMq, junto com a utilização de um banco de dados não relacional.

## Referência:

https://github.com/buildrun-tech/buildrun-desafio-backend-btg-pactual/tree/main

## Tecnologias utilizadas:

- Spring Boot;
- RabbitMq;
- Docker;
- Spring Data MongoDB;
- MongoDB Compass;

## Passos de como executar o projeto:

1. Acessar pasta "local" do projeto e rodar comando :
   `docker compose up`

2. Baixar o [MongoDB Compass](https://www.mongodb.com/try/download/compass);

3. Rodar projeto via IDE;

4.  Acessar http://localhost:15672/ user/password: guest;

5. Abrir MongoDB Compass;

6. Publicar mensagem no RabbitMq: 

   - menu Queues and Streams;

   - Publish message;

   - No campo Payload adicionar o seguinte body e clicar em Publish message:

     `   {
            "codigoPedido": 1001,
            "codigoCliente":1,
            "itens": [
                {
                    "produto": "lápis",
                    "quantidade": 100,
                    "preco": 1.10
                },
                {
                    "produto": "caderno",
                    "quantidade": 10,
                    "preco": 1.00
                }
            ]
        }`

7. Acessar MongoDB Compass, tb_orders e clicar no botão de atualizar lista de registros. Verificar que o registro foi inserido.
