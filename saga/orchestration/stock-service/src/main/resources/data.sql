insert into stock_schema.inventory(sku, available_qty) values ('SKU-123', 5) on conflict (sku) do nothing;
insert into stock_schema.inventory(sku, available_qty) values ('SKU-ABC', 10) on conflict (sku) do nothing;
