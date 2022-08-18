INSERT INTO url
  (name, created_at)
VALUES
  ('https://www.example.com', '2020-12-12'),
  ('https://www.yahoo.com', '2020-12-12'),
  ('https://www.wikipedia.org', '2020-12-12'),
  ('https://github.com/', '2020-12-12'),
  ('https://about.gitlab.com/', '2020-12-12'),
  ('https://docs.oracle.com', '2020-12-12'),
  ('https://www.google.com', '2020-12-12');


INSERT INTO url_check
  (status_code, title, h1, description, url_id, created_at)
VALUES
  (200, 'title', 'h1', 'description', 1, '2020-12-12'),
  (200, 'title', 'h1', 'description', 1, '2020-12-12'),
  (200, 'title', 'h1', 'description', 1, '2020-12-12'),
  (200, 'title', 'h1', 'description', 1, '2020-12-12'),
  (200, 'title', 'h1', 'description', 1, '2020-12-12');
