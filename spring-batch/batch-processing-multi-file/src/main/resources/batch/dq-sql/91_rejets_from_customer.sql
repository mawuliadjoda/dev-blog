-- Insère les rejets provenant de la table stg_customer
INSERT INTO rejets (batch_id, source_table, source_pk, error_code, error_msg, payload_json)
SELECT :batchId,
       'stg_customer',
       CAST(customer_id AS VARCHAR),
       error_code,
       error_msg,
       to_jsonb(s.*)
FROM stg_customer s
WHERE s.batch_id = :batchId
  AND s.valid_flag = FALSE
  AND s.error_code IS NOT NULL;
