-- Vérifie la validité du format email
UPDATE stg_customer
SET valid_flag = FALSE,
    error_code = 'EMAIL_FORMAT',
    error_msg  = 'Email invalide'
WHERE batch_id = :batchId
  AND valid_flag = TRUE
  AND (email IS NULL OR email !~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');
