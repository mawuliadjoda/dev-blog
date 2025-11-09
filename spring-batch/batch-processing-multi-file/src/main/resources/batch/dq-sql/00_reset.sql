-- Réinitialise les flags et messages de validation pour le batch courant
UPDATE stg_customer
SET valid_flag = TRUE,
    error_code = NULL,
    error_msg  = NULL
WHERE batch_id = :batchId;

UPDATE stg_order
SET valid_flag = TRUE,
    error_code = NULL,
    error_msg  = NULL
WHERE batch_id = :batchId;
