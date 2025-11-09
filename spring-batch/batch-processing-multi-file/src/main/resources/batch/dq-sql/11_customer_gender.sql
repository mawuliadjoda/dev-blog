-- Vérifie la validité du genre
UPDATE stg_customer
SET valid_flag = FALSE,
    error_code = 'GENDER_INVALID',
    error_msg  = 'Valeur non autorisée'
WHERE batch_id = :batchId
  AND valid_flag = TRUE
  AND COALESCE(gender,'') NOT IN ('Male','Female','Other','Unknown');
