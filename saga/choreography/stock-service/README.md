# 1. 
    CREATE DATABASE stock;
    create schema if not exists stock_schema;

# 2.

    mvn spring-boot:run

# 3. 

    insert into stock_schema.inventory(sku, available_qty) values ('SKU-123', 5) on conflict (sku) do nothing;
    insert into stock_schema.inventory(sku, available_qty) values ('SKU-ABC', 10) on conflict (sku) do nothing;
