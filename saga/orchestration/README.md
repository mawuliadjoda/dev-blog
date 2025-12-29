

Ce qui change vs choreography
Topics (Kafka)

Commands (pilotés par l’orchestrateur)

payment.reserve.command

payment.cancel.command ✅ (compensation explicite)

stock.reserve.command

stock.release.command ✅ (compensation explicite)

Events (réponses des participants)

payment.reserved, payment.failed

stock.reserved, stock.failed

Optionnel (pour observers / audit)

order.completed

order.cancelled

Résumé architecture (v3)

order-service :

crée l’order (PAYMENT_PENDING)

envoie ReservePaymentCommand

reçoit payment.reserved → passe STOCK_PENDING → envoie ReserveStockCommand

reçoit stock.reserved → COMPLETED

reçoit payment.failed ou stock.failed → CANCELLED + envoie compensations :

CancelPaymentCommand si payment a été réservé

ReleaseStockCommand si stock a été réservé

payment-service :

consomme payment.reserve.command + payment.cancel.command

produit payment.reserved ou payment.failed

stock-service :

consomme stock.reserve.command + stock.release.command

produit stock.reserved ou stock.failed