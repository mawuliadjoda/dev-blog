-- @step: insertTargetsStep
-- @description: Upsert des enregistrements valides du batch dans la table customer
-- @author: Mawuli ADJODA
-- @since: 2025-11-09

-- Insert / Update des clients valides du batch courant
INSERT INTO customer (
    customer_id,
    first_name,
    last_name,
    email,
    gender,
    contact,
    country,
    dob,
    created_at,
    updated_at
)
SELECT DISTINCT
    s.customer_id,
    s.first_name,
    s.last_name,
    s.email,
    s.gender,
    s.contact,
    s.country,
    s.dob,
    now(),
    now()
FROM stg_customer s
WHERE s.batch_id = :batchId
  AND s.valid_flag = TRUE
    ON CONFLICT (customer_id) DO UPDATE SET
    first_name = EXCLUDED.first_name,
     last_name  = EXCLUDED.last_name,
     email      = EXCLUDED.email,
     gender     = EXCLUDED.gender,
     contact    = EXCLUDED.contact,
     country    = EXCLUDED.country,
     dob        = EXCLUDED.dob,
     updated_at = now();
