ALTER TABLE shopbeer.city ADD COLUMN country VARCHAR(255) NOT NULL;
UPDATE shopbeer.city SET country = '1';