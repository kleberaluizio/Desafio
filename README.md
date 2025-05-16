# Desafio LuizaLabs: Integração de Sistemas e Normalização de Dados

Este projeto tem como objetivo construir um sistema capaz de processar um arquivo de pedidos desnormalizado, transformando-o em um formato JSON normalizado. A comunicação com o sistema será realizada através de uma API REST, que receberá o arquivo para processamento e retornará o resultado.

## Definições Arquiteturais

O Desenvolvimento desse microserviço teve como foco a simplicidade. Foi utilizado Java 21 (última versão LTS) e o Maven para automação da compilação e gerenciamente de dependências. Como framework, foi escolhido o Spring Boot por oferecer uma abordagem simples, moderna e de rápida configuração.

A arquitetura segue o padrão tradicional de camadas, camada de apresentação (controller) e a camada da aplicação (service). Assim, temos uma separação de responsabilidades de forma simples e que com facilmente poderia evoluir para uma arquitetura mais sofisticada. 

## Endpoints

### `POST /api/v1/file/process`

Este endpoint recebe um arquivo `.txt` com registros de compras e aplica filtros opcionais por ID do pedido e intervalo de datas da compra (data de início e data de fim).

**Parâmetros Esperados (Form-Data):**

| Campo        | Tipo    | Obrigatório | Descrição                                                                                                                               |
| :----------- | :------ | :---------- | :-------------------------------------------------------------------------------------------------------------------------------------- |
| `file`       | arquivo | Sim         | Arquivo `.txt` contendo os registros de compras a serem processados.                                                                   |
| `order_id`   | string  | Não         | ID do pedido para filtrar os resultados.                                                                                             |
| `start_date` | string  | Não         | Data inicial do período de filtro, no formato `YYYY-MM-DD` (ex: `2025-12-31`).                                                           |
| `end_date`   | string  | Não         | Data final do período de filtro, no formato `YYYY-MM-DD` (ex: `2026-01-15`).                                                             |

**Respostas:**

  * **`200 OK`:** Retorna um array JSON contendo a lista de registros de compras processados e normalizados.
  * **`400 Bad Request`:** Indica que houve problemas com os parâmetros de filtro ou com o conteúdo do arquivo enviado.
  * **`500 Internal Server Error`:** Indica uma falha inesperada durante o processamento.

## Resposta de Sucesso (`200 OK`)

Quando a requisição é bem-sucedida, a API retorna o código de status `200 OK` e um corpo JSON contendo os usuários e seus respectivos pedidos.

**Formato da Resposta de Sucesso:**

```json
[
    {
        "user_id": 98,
        "name": "Dr. Petra Kutch",
        "orders": [
            {
                "order_id": 1048,
                "date": "2021-06-11",
                "total": 305.5,
                "products": [
                    {
                        "product_id": 4,
                        "value": 305.5
                    }
                ]
            }
        ]
    },
    {
        "user_id": 23,
        "name": "Logan Lynch",
        "orders": [
            {
                "order_id": 250,
                "date": "2021-06-11",
                "total": 1293.88,
                "products": [
                    {
                        "product_id": 0,
                        "value": 1293.88
                    }
                ]
            }
        ]
    }
]
```
### Campos da Resposta

| Campo                         | Descrição                                        |
|------------------------------|--------------------------------------------------|
| `user_id`                    | Identificador do usuário                         |
| `name`                       | Nome completo do usuário                         |
| `orders`                     | Lista de pedidos associados ao usuário           |
| `order_id`                   | Identificador do pedido                          |
| `date`                       | Data do pedido no formato `yyyy-MM-dd`           |
| `total`                      | Valor total do pedido                            |
| `products`                   | Lista de produtos incluídos no pedido            |
| `product_id`                 | Identificador do produto                         |
| `value`                      | Valor do produto no pedido                       |



## Respostas de Erro (`400 Bad Request`)

Em caso de requisição inválida, a API retornará uma resposta com o código de status `400 Bad Request` e um corpo JSON detalhando o erro.

**Formato da Resposta de Erro:**

```json
{
    "timestamp": "YYYY-MM-DDTHH:MM:SS.fffffffff",
    "status": 400,
    "error": "Bad Request",
    "message": "Mensagem descritiva do erro",
    "details": [
        "Detalhe específico do erro 1",
        "Detalhe específico do erro 2",
    ]
}
```
### Possíveis messages e details

| `message`                             | Exemplos de `details`                                                                                                                                         |
|--------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Invalid filter parameter format`    | - Invalid file type: only plain text files (.txt) are allowed.<br>- Invalid value for 'order_id'. Expected type: Integer<br>- Invalid value for 'start_date'. Expected format: YYYY-MM-dd<br>- Invalid value for 'end_date'. Expected format: YYYY-MM-dd<br>- Invalid date range: 'start_date' must be before or equal to 'end_date'.<br>- Both 'start_date' and 'end_date' parameters must be either provided or both omitted |
| `Invalid entries found in the file`  | - The following lines in the file (\<filename\>.txt) are not in the correct format: \<line numbers\>.                                                        |

## Execução Local

Para executar a aplicação localmente, siga os passos abaixo:

1.  **Clone o repositório:**

    ```sh
    git clone https://github.com/kleberaluizio/Desafio.git
    ```

2.  **Navegue até o diretório do processador de arquivos:**

    ```sh
    cd Desafio/fileprocessor
    ```

3.  **Compile e execute utilizando Docker Compose:**

    ```sh
    docker-compose up --build
    ```
