-- liquibase formatted sql

-- changeset virtusystems:001
CREATE TABLE insured
(
    id               BIGSERIAL PRIMARY KEY,
    -- ФИО
    last_name        VARCHAR(100)  NOT NULL,
    first_name       VARCHAR(100)  NOT NULL,
    middle_name      VARCHAR(100)  NULL,
    -- Дата/место рождения
    birth_date       DATE          NOT NULL,
    birth_place      VARCHAR(255)  NULL,
    -- Гражданство, пол
    citizenship      VARCHAR(32)   NOT NULL,
    gender           VARCHAR(16)   NOT NULL, -- 'M' или 'Ж'
    -- Идентификаторы
    inn              VARCHAR(16)   NULL,
    snils            VARCHAR(20)   NULL,
    -- Контакты
    phone            VARCHAR(50)   NULL,
    email            VARCHAR(255)  NULL,
    -- Документ
    doc_type         VARCHAR(64)   NULL,
    doc_series       VARCHAR(20)   NULL,
    doc_number       VARCHAR(30)   NULL,
    doc_issued_by    VARCHAR(255)  NULL,
    doc_issued_date  DATE          NULL,
    doc_dept_code    VARCHAR(16)   NULL,
    -- Адрес регистрации (по полям)
    reg_country      VARCHAR(100)  NULL,
    reg_region       VARCHAR(100)  NULL,
    reg_district     VARCHAR(100)  NULL,
    reg_index        VARCHAR(20)   NULL,
    reg_city         VARCHAR(100)  NULL,
    reg_street       VARCHAR(200)  NULL,
    reg_house        VARCHAR(20)   NULL,
    reg_building     VARCHAR(20)   NULL,
    reg_apartment    VARCHAR(20)   NULL,
    reg_address_line VARCHAR(4096) NULL,
    created_at       TIMESTAMP DEFAULT NOW(),
    updated_at       TIMESTAMP DEFAULT NOW()
);


-- changeset virtusystems:002
CREATE TABLE product
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(256) UNIQUE NOT NULL,
    description VARCHAR(8192)
);

-- changeset virtusystems:003
CREATE TABLE contract
(
    id          BIGSERIAL PRIMARY KEY,
    calc_id     VARCHAR(100) NULL,
    number      VARCHAR(256) NULL,
    calc_date   TIMESTAMP                    DEFAULT NULL,
    premium     NUMERIC(15, 2)      NOT NULL DEFAULT 0,
    insured_sum NUMERIC(15, 2)      NOT NULL DEFAULT 0,
    issue_date  TIMESTAMP                    DEFAULT NULL,
    start_date  TIMESTAMP                    DEFAULT NULL,
    end_date    TIMESTAMP                    DEFAULT NULL,
    status      VARCHAR(64)         NOT NULL DEFAULT 'PROJECT',
    params      JSONB               NOT NULL,
    insured_id  BIGINT              NULL REFERENCES insured (id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES product(id),
    CONSTRAINT uniq_product_calc_id UNIQUE (product_id, calc_id),
    CONSTRAINT uniq_product_number UNIQUE (product_id, number)
);

CREATE INDEX idx_calc_date ON contract (calc_date);
CREATE INDEX idx_issue_date ON contract (issue_date);
-- Можно индексировать прямо поля JSON
-- CREATE INDEX idx_contract_params_premium ON contract
--     USING gin ((params->'premium'));

-- changeset virtusystems:004
CREATE TABLE calc_counter
(
    day        DATE   NOT NULL,
    product_id BIGINT NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    counter    BIGINT NOT NULL,
    PRIMARY KEY (day, product_id)
);

-- changeset virtusystems:005
CREATE TABLE contract_number_counter
(
    product_id BIGSERIAL PRIMARY KEY REFERENCES product (id) ON DELETE CASCADE,
    counter    BIGINT NOT NULL
);
