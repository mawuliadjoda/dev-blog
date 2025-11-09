-- Insère les rejets provenant de la table stg_order
INSERT INTO rejets (batch_id, source_table, source_pk, error_code, error_msg, payload_json)
SELECT :batchId,
       'stg_order',
       CAST(order_id AS VARCHAR),
       error_code,
       error_msg,
       to_jsonb(o.*)
FROM stg_order o
WHERE o.batch_id = :batchId
  AND o.valid_flag = FALSE
  AND o.error_code IS NOT NULL;
