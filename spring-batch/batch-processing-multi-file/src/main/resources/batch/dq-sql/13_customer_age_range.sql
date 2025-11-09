-- Vérifie que l’âge n’excède pas 120 ans
UPDATE stg_customer
SET valid_flag = FALSE,
    error_code = 'DOB_AGE_RANGE',
    error_msg  = 'Âge > 120 ans'
WHERE batch_id = :batchId
  AND valid_flag = TRUE
  AND dob IS NOT NULL
  AND dob < CURRENT_DATE - INTERVAL '120 years';
