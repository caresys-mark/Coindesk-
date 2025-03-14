
CREATE TABLE IF NOT EXISTS currency (
    id int AUTO_INCREMENT PRIMARY KEY,
    updated VARCHAR(255),
    updated_iso VARCHAR(255),
    updated_uk VARCHAR(255),
    disclaimer VARCHAR(255),
    chart_name VARCHAR(255),
    code VARCHAR(10),
    symbol VARCHAR(10),
    rate DOUBLE,
    description VARCHAR(255),
    rate_float DOUBLE
);

INSERT INTO currency (updated, updated_iso, updated_uk, disclaimer, chart_name, code, symbol, rate, description, rate_float)
VALUES 
('Sep 2, 2024 07:07:20 UTC', '2024-09-02T07:07:20+00:00', 'Sep 2, 2024 at 08:07 BST', 'just for test', 'Bitcoin', 'USD', '&#36;', 57756.298, 'United States Dollar', 57756.2984),
('Sep 2, 2024 07:07:20 UTC', '2024-09-02T07:07:20+00:00', 'Sep 2, 2024 at 08:07 BST', 'just for test', 'Bitcoin', 'GBP', '&pound;', 43984.02, 'British Pound Sterling', 43984.0203),
('Sep 2, 2024 07:07:20 UTC', '2024-09-02T07:07:20+00:00', 'Sep 2, 2024 at 08:07 BST', 'just for test', 'Bitcoin', 'EUR', '&euro;', 52243.287, 'Euro', 52243.2865);
