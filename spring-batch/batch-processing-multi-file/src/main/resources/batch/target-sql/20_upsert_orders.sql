-- @step: insertTargetsStep
-- @description: Upsert des enregistrements valides du batch dans la table customer
-- @author: Mawuli ADJODA
-- @since: 2025-11-09

-- Insert / Update des commandes valides du batch courant
INSERT INTO orders (
    order_id,
    customer_id,
    order_date,
    amount,
    status,
    created_at,
    updated_at
)
SELECT
    o.order_id,
    o.customer_id,
    o.order_date,
    o.amount,
    o.status,
    now(),
    now()
FROM stg_order o
WHERE o.batch_id = :batchId
  AND o.valid_flag = TRUE
  -- Vérifie que le client parent existe déjà dans la table cible
  AND EXISTS (
    SELECT 1
    FROM customer c
    WHERE c.customer_id = o.customer_id
)
    ON CONFLICT (order_id) DO UPDATE SET
    customer_id = EXCLUDED.customer_id,
      order_date  = EXCLUDED.order_date,
      amount      = EXCLUDED.amount,
      status      = EXCLUDED.status,
      updated_at  = now();
