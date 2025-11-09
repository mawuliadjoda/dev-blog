-- Supprime les rejets précédents du même batch (idempotence)
DELETE FROM rejets WHERE batch_id = :batchId;
