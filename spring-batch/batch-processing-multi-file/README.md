

-- create table if not exists stg_customer
-- (
-- customer_id bigint primary key,
-- first_name  varchar(100),
-- last_name   varchar(100),
-- email       varchar(255),
-- gender      varchar(50),
-- contact     varchar(50),
-- country     varchar(100),
-- dob         date
-- );
-- 
-- create table if not exists stg_order
-- (
-- order_id     bigint primary key,
-- customer_id  bigint not null,
-- order_date   date,
-- amount       numeric(14,2),
-- status       varchar(30)
-- );


-- ===============================================
-- TABLE: stg_customer
-- ===============================================
CREATE TABLE IF NOT EXISTS stg_customer
(
batch_id     VARCHAR(64)  NOT NULL,
customer_id  BIGINT       NOT NULL,
first_name   VARCHAR(100),
last_name    VARCHAR(100),
email        VARCHAR(255),
gender       VARCHAR(50),
contact      VARCHAR(50),
country      VARCHAR(100),
dob          DATE,

    -- Colonnes de suivi de qualité et de validation
    valid_flag   BOOLEAN      DEFAULT TRUE,
    error_code   VARCHAR(64),
    error_msg    TEXT,

    CONSTRAINT stg_customer_pk PRIMARY KEY (batch_id, customer_id)
);

CREATE INDEX IF NOT EXISTS idx_stg_customer_batch ON stg_customer(batch_id);
CREATE INDEX IF NOT EXISTS idx_stg_customer_valid ON stg_customer(valid_flag);

-- ===============================================
-- TABLE: stg_order
-- ===============================================
CREATE TABLE IF NOT EXISTS stg_order
(
batch_id     VARCHAR(64)  NOT NULL,
order_id     BIGINT       NOT NULL,
customer_id  BIGINT       NOT NULL,
order_date   DATE,
amount       NUMERIC(14,2),
status       VARCHAR(30),

    -- Colonnes de suivi de qualité et de validation
    valid_flag   BOOLEAN      DEFAULT TRUE,
    error_code   VARCHAR(64),
    error_msg    TEXT,

    CONSTRAINT stg_order_pk PRIMARY KEY (batch_id, order_id)
);

CREATE INDEX IF NOT EXISTS idx_stg_order_batch ON stg_order(batch_id);
CREATE INDEX IF NOT EXISTS idx_stg_order_valid ON stg_order(valid_flag);
CREATE INDEX IF NOT EXISTS idx_stg_order_fk    ON stg_order(batch_id, customer_id);
