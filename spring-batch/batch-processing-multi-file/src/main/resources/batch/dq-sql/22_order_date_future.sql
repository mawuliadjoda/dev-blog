-- Vérifie que la date de commande n’est pas dans le futur
UPDATE stg_order
SET valid_flag = FALSE,
    error_code = 'DATE_FUTURE',
    error_msg  = 'Date future'
WHERE batch_id = :batchId
  AND valid_flag = TRUE
  AND order_date > CURRENT_DATE;
