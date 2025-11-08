

create table if not exists stg_customer
(
customer_id bigint primary key,
first_name  varchar(100),
last_name   varchar(100),
email       varchar(255),
gender      varchar(50),
contact     varchar(50),
country     varchar(100),
dob         date
);

create table if not exists stg_order
(
order_id     bigint primary key,
customer_id  bigint not null,
order_date   date,
amount       numeric(14,2),
status       varchar(30)
);
