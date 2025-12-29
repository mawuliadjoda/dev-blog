# 1. 
    CREATE DATABASE order;
    create schema if not exists order_schema;

# 2.

    mvn spring-boot:run


curl -X POST http://localhost:8082/orders   -H "Content-Type: application/json"   -d '{"amount":180.00,"sku":"SKU-123","qty":1}'
curl -X POST http://localhost:8082/orders   -H "Content-Type: application/json"   -d '{"amount":1970.00,"sku":"SKU-123","qty":10}'