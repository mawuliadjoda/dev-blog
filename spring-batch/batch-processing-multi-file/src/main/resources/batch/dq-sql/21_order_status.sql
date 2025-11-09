-- Vérifie la validité du statut de commande
UPDATE stg_order
SET valid_flag = FALSE,
    error_code = 'STATUS_INVALID',
    error_msg  = 'Valeur non autorisée'
WHERE batch_id = :batchId
  AND valid_flag = TRUE
  AND COALESCE(status,'') NOT IN ('NEW','PAID','CANCELLED','SHIPPED','REFUNDED');
