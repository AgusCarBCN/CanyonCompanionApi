-- =========================
-- USERS (si necesitas uno básico)
-- =========================


INSERT INTO roles ( role) VALUES('ROLE_USER');
INSERT INTO roles ( role) VALUES ('ROLE_ADMIN');
-- =========================
INSERT INTO maps(name, mbtiles_path, image_path)
VALUES (
           'Huesca-Guara',
           'maps/mbtiles/guara.mbtiles',
           'maps/previews/Guara.jpg'
       );