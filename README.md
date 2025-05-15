# Desafio - LuizaLabs - Integração entre sistemas
O sistema legado que possui um arquivo de pedidos desnormalizado, precisamos transformá-lo em um arquivo json normalizado. E para isso precisamos satisfazer alguns requisitos.

## Objetivo do Desafio
Construir um sistema que receba um arquivo via API REST e processe-o para ser retornado via API REST.

## Executando a Aplicação Localmente:

1. Clone o repositório
   ```sh
   git clone https://github.com/kleberaluizio/Desafio.git
   ```
2. Navegue até o diretório do projeto:
```sh
   cd Desafio/fileprocessor
 ```
3. Compile e execute com Docker Compose:
 ```sh
   docker-compose up --build
 ```

## Endpoints
**POST /v1/file/process**

Processa um arquivo .txt com registros de compras, aplicando filtros opcionais por id de pedido e intervalo de data de compra (data início e data fim).
  
**Parâmetros esperados (form-data):**

| Campo      | Tipo    | Obrigatório | Descrição                                                                 |
|------------|---------|-------------|---------------------------------------------------------------------------|
| file       | arquivo | Sim         | Arquivo `.txt` contendo os registros a serem processados.                |
| order_id   | string  | Não         | ID do pedido a ser filtrado.                                             |
| start_date | string  | Não         | Data inicial do filtro no formato `YYYY-MM-DD`.                          |
| end_date   | string  | Não         | Data final do filtro no formato `YYYY-MM-DD`.                            |


**Retornos:**
  * 200 (OK): Sucesso. Retorna a lista de registros de compras processados.
  * 400 (Bad Request): Erro de validação. Filtros inválidos ou problemas no conteúdo do arquivo.
  * 500 (Internal Server Error): Falha interna no servidor.

## Licença
Este repositório é licenciado sob a [licença MIT](LICENSE).
