-- DROP TABLE IF EXISTS urls;
--
-- CREATE TABLE urls (
--   id bigint AUTO_INCREMENT,
--   "name" varchar(255),
--   createdAt date
-- );

INSERT INTO url
  ("name", createdAt)
VALUES
  ('google.com', '2022-08-03'),
  ('yandex.ru', '2022-08-03');
