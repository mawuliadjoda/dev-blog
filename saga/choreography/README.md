

# 
    sudo apt update
    sudo apt install -y curl zip unzip
    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh" => reload shell
    

    sdk version
    sdk list java
    sdk install java  25.0.1-tem
    java -version




# Supprimer tous les conteneurs arrêtés

    docker container prune


# Happy path: saga choreography

curl -X POST http://localhost:8082/orders   -H "Content-Type: application/json"   -d '{"amount":180.00,"sku":"SKU-123","qty":1}'

Client
|
|  HTTP POST /orders  (order-service:8082)
v
order-service
[CODE] OrderController#create(req)
- INSERT order_svc.orders(id, status=PAYMENT_PENDING, amount, sku, qty)
- StreamBridge.send("reservePaymentCommand-out-0", ReservePaymentCommand)

[CLOUD STREAM OUT]
binding: reservePaymentCommand-out-0
destination(topic): payment.reserve.command
|
v
Kafka topic: payment.reserve.command
|
v
payment-service
[CLOUD STREAM IN]
function: reservePaymentCommand
binding: reservePaymentCommand-in-0
destination(topic): payment.reserve.command
group: payment-service

[CODE] PaymentHandlers#onReservePaymentCommand(cmd)
- (idempotence) check payment_svc.processed_events(eventId)
- INSERT payment_svc.payment_reservations(orderId, status=RESERVED, amount)
- StreamBridge.send("paymentReserved-out-0", PaymentReservedEvent)

[CLOUD STREAM OUT]
binding: paymentReserved-out-0
destination(topic): payment.reserved
|
v
Kafka topic: payment.reserved
|
v
order-service
[CLOUD STREAM IN]
function: paymentReserved
binding: paymentReserved-in-0
destination(topic): payment.reserved
group: order-service

[CODE] OrderOrchestratorHandlers#onPaymentReserved(evt)
- UPDATE order_svc.orders SET status=STOCK_PENDING WHERE id=orderId
- StreamBridge.send("reserveStockCommand-out-0", ReserveStockCommand(orderId, sku, qty))

[CLOUD STREAM OUT]
binding: reserveStockCommand-out-0
destination(topic): stock.reserve.command
|
v
Kafka topic: stock.reserve.command
|
v
stock-service
[CLOUD STREAM IN]
function: reserveStockCommand
binding: reserveStockCommand-in-0
destination(topic): stock.reserve.command
group: stock-service

[CODE] StockHandlers#onReserveStockCommand(cmd)
- (idempotence) check stock_svc.processed_events(eventId)
- SELECT stock_svc.inventory WHERE sku=cmd.sku
- IF inventory.available_qty >= qty:
UPDATE inventory.available_qty = available_qty - qty
INSERT stock_svc.stock_reservations(orderId, sku, qty, status=RESERVED)
StreamBridge.send("stockReserved-out-0", StockReservedEvent)
ELSE:
StreamBridge.send("stockFailed-out-0", StockFailedEvent("OUT_OF_STOCK"))

[CLOUD STREAM OUT]
binding: stockReserved-out-0
destination(topic): stock.reserved
|
v
Kafka topic: stock.reserved
|
v
order-service
[CLOUD STREAM IN]
function: stockReserved
binding: stockReserved-in-0
destination(topic): stock.reserved
group: order-service

[CODE] OrderOrchestratorHandlers#onStockReserved(evt)
- UPDATE order_svc.orders SET status=COMPLETED WHERE id=orderId
- StreamBridge.send("orderCompleted-out-0", OrderCompletedEvent)

[CLOUD STREAM OUT]
binding: orderCompleted-out-0
destination(topic): order.completed
|
v
Kafka topic: order.completed


---------------------------------------------------------------------
curl -X POST http://localhost:8082/orders   -H "Content-Type: application/json"   -d '{"amount":1970.00,"sku":"SKU-123","qty":10}'


# payment fail” (compensation)

... (jusqu'à payment-service consume payment.reserve.command)

payment-service
[CODE] onReservePaymentCommand(cmd)
- IF amount > 1000:
StreamBridge.send("paymentFailed-out-0", PaymentFailedEvent("PAYMENT_DECLINED"))

[OUT] paymentFailed-out-0 -> topic payment.failed
|
v
Kafka: payment.failed
|
v
order-service
[IN] paymentFailed-in-0 (function paymentFailed) <- topic payment.failed

[CODE] onPaymentFailed(evt)
- UPDATE orders SET status=CANCELLED, reason="PAYMENT_DECLINED"
- StreamBridge.send("orderCancelled-out-0", OrderCancelledEvent)

[OUT] orderCancelled-out-0 -> topic order.cancelled
|
v
Kafka: order.cancelled
|
+--> payment-service
|     [IN] orderCancelled-in-0 (function orderCancelled)
|     [CODE] onOrderCancelled(evt)
|           - UPDATE payment_reservations SET status=CANCELLED (si existe)
|
+--> stock-service
[IN] orderCancelled-in-0 (function orderCancelled)
[CODE] onOrderCancelled(evt)
- release stock reservation + restore inventory (si reservation existait)


----------------------------------------------------------------
# stock fail” (compensation complète)


... (jusqu'à stock-service consume stock.reserve.command)

stock-service
[CODE] onReserveStockCommand(cmd)
- IF inventory.available_qty < qty:
StreamBridge.send("stockFailed-out-0", StockFailedEvent("OUT_OF_STOCK"))

[OUT] stockFailed-out-0 -> topic stock.failed
|
v
Kafka: stock.failed
|
v
order-service
[IN] stockFailed-in-0 (function stockFailed) <- topic stock.failed

[CODE] onStockFailed(evt)
- UPDATE orders SET status=CANCELLED, reason="OUT_OF_STOCK"
- StreamBridge.send("orderCancelled-out-0", OrderCancelledEvent)

[OUT] orderCancelled-out-0 -> topic order.cancelled
|
v
Kafka: order.cancelled
|
+--> payment-service
|     [IN] orderCancelled-in-0
|     [CODE] onOrderCancelled(evt)
|           - UPDATE payment_reservations SET status=CANCELLED
|
+--> stock-service
[IN] orderCancelled-in-0
[CODE] onOrderCancelled(evt)
- IF reservation was RESERVED:
inventory.available_qty += reserved.qty
reservation.status = RELEASED
