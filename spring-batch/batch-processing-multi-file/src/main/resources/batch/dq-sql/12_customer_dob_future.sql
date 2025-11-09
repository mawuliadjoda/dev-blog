-- Vérifie que la date de naissance n’est pas future
UPDATE stg_customer
SET valid_flag = FALSE,
    error_code = 'DOB_FUTURE',
    error_msg  = 'Date de naissance future'
WHERE batch_id = :batchId
  AND valid_flag = TRUE
  AND dob > CURRENT_DATE;
