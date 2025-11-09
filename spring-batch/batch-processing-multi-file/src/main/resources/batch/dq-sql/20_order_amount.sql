-- Vérifie que le montant n’est pas négatif
UPDATE stg_order
SET valid_flag = FALSE,
    error_code = 'AMOUNT_NEG',
    error_msg  = 'Montant négatif'
WHERE batch_id = :batchId
  AND valid_flag = TRUE
  AND amount IS NOT NULL
  AND amount < 0;
