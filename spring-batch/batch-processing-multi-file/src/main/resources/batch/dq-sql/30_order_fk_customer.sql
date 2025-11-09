-- Vérifie la cohérence FK : chaque commande doit référencer un client valide du même batch
UPDATE stg_order o
SET valid_flag = FALSE,
    error_code = 'FK_CUSTOMER',
    error_msg  = 'Customer inexistant'
WHERE o.batch_id = :batchId
  AND o.valid_flag = TRUE
  AND NOT EXISTS (
    SELECT 1
    FROM stg_customer c
    WHERE c.batch_id = o.batch_id
      AND c.valid_flag = TRUE
      AND c.customer_id = o.customer_id
);
