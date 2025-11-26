CREATE TABLE usda_food_raw (
  fdc_id             VARCHAR PRIMARY KEY,
  data_type          VARCHAR,
  description        TEXT,
  food_category_id   VARCHAR,
  publication_date   VARCHAR
);

CREATE TABLE usda_nutrient_raw (
    id              VARCHAR PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    unit_name       VARCHAR(50),
    nutrient_nbr    VARCHAR,
    rank            VARCHAR
);

CREATE TABLE usda_food_nutrient_raw (
    id                VARCHAR PRIMARY KEY,
    fdc_id            VARCHAR NOT NULL,
    nutrient_id       VARCHAR NOT NULL,
    amount            VARCHAR,
    data_points       VARCHAR,
    derivation_id     VARCHAR,
    min               VARCHAR,
    max               VARCHAR,
    median            VARCHAR,
    footnote          TEXT,
    min_year_acquired VARCHAR
);

CREATE TABLE usda_measure_unit_raw (
    id      VARCHAR PRIMARY KEY,
    name    VARCHAR(100) NOT NULL
);

CREATE TABLE usda_food_portion_raw (
    id                  VARCHAR PRIMARY KEY,
    fdc_id              VARCHAR NOT NULL,
    seq_num             VARCHAR,
    amount              VARCHAR,
    measure_unit_id     VARCHAR,
    portion_description TEXT,
    modifier            VARCHAR(255),
    gram_weight         VARCHAR,
    data_points         VARCHAR,
    footnote            TEXT,
    min_year_acquired   VARCHAR
);

CREATE TABLE food (
  id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  usda_fdc_id        VARCHAR,             -- lien vers usda_food_raw.fdc_id
  name               TEXT NOT NULL,
  category           TEXT,
  kcal_per_100g      NUMERIC(10,2) NOT NULL,
  protein_per_100g   NUMERIC(10,2),
  carbs_per_100g     NUMERIC(10,2),
  fat_per_100g       NUMERIC(10,2),
  source             TEXT NOT NULL DEFAULT 'USDA',
  created_at         TIMESTAMPTZ DEFAULT now()
);

INSERT INTO food (
  usda_fdc_id,
  name,
  category,
  kcal_per_100g,
  protein_per_100g,
  carbs_per_100g,
  fat_per_100g
)
SELECT
  f.fdc_id,
  f.description,
  NULL,
  MAX(CASE WHEN TRIM(fn.nutrient_id) = '1008' THEN NULLIF(fn.amount, '')::numeric END),
  MAX(CASE WHEN TRIM(fn.nutrient_id) = '1003' THEN NULLIF(fn.amount, '')::numeric END),
  MAX(CASE WHEN TRIM(fn.nutrient_id) = '1005' THEN NULLIF(fn.amount, '')::numeric END),
  MAX(CASE WHEN TRIM(fn.nutrient_id) = '1004' THEN NULLIF(fn.amount, '')::numeric END)
FROM usda_food_raw f
JOIN usda_food_nutrient_raw fn ON TRIM(fn.fdc_id) = TRIM(f.fdc_id)
GROUP BY f.fdc_id, f.description
HAVING MAX(CASE WHEN TRIM(fn.nutrient_id) = '1008' THEN NULLIF(fn.amount, '')::numeric END) IS NOT NULL;

-- 
CREATE TABLE food_portion (
  id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  food_id    UUID NOT NULL REFERENCES food(id) ON DELETE CASCADE,
  label      TEXT NOT NULL,          -- "cup", "slice", "tasse", etc.
  grams      NUMERIC(10,2) NOT NULL, -- poids en grammes
  created_at TIMESTAMPTZ DEFAULT now()
);

INSERT INTO food_portion (food_id, label, grams)
SELECT
    f.id AS food_id,
    TRIM(
        CONCAT(
            -- amount : “1”, “0.5” etc.
            COALESCE(NULLIF(fp.amount, '')::text, ''),
            ' ',
            -- portion_description : “cup”, “slice”, “tbsp”
            COALESCE(NULLIF(fp.portion_description, ''), ''),
            -- modifier : “shredded”, “cooked”, “small”
            CASE 
                WHEN fp.modifier IS NULL OR fp.modifier = '' THEN '' 
                ELSE ' ' || fp.modifier 
            END
        )
    ) AS label,
    
    -- grams
    NULLIF(fp.gram_weight, '')::numeric AS grams

FROM usda_food_portion_raw fp
JOIN food f ON f.usda_fdc_id = fp.fdc_id;

--               -------------------------------------


-- Combien de lignes ont le nutriment "Energy (kcal)" ?
SELECT COUNT(*) 
FROM usda_food_nutrient_raw
WHERE TRIM(nutrient_id) = '1008';

-- Combien d'aliments distincts ont au moins une valeur de calories ?
SELECT COUNT(DISTINCT TRIM(fdc_id))
FROM usda_food_nutrient_raw
WHERE TRIM(nutrient_id) = '1008';