INSERT INTO users (email, password, name, role)
VALUES
('admin@example.com', 'adminpass', 'Admin User', 'ROLE_ADMIN'),
('user@example.com', 'userpass', 'Normal User', 'ROLE_USER');

INSERT INTO short_urls (short_key, original_url, created_by, created_at, expires_at, is_private, click_count)
VALUES
('qW1eR2', 'https://www.generic-blog.com/post/getting-started-with-docker', 1, '2025-01-10 10:00:00', NULL, FALSE, 25),
('zX3cD4', 'https://www.awesome-tutorials.com/guides/kubernetes-basics', 1, '2025-01-11 11:30:00', NULL, FALSE, 150),
('aB5vF6', 'https://www.tech-news.com/article/new-java-features', 1, '2025-02-05 09:00:00', NULL, TRUE, 5),
('pL9mK8', 'https://www.cooking-blog.com/recipes/best-pasta', 1, '2025-03-15 14:00:00', NULL, FALSE, 0),
('jH7gT4', 'https://www.travel-diary.com/my-trip-to-japan', 1, '2025-04-20 08:22:00', '2025-05-01 00:00:00', FALSE, 77),
('nB6vC1', 'https://www.finance-explained.com/intro-to-investing', 1, '2025-05-02 18:00:00', NULL, TRUE, 12),
('kI8uJ7', 'https://www.developer-hub.com/spring-boot-3-new-features', 1, '2025-05-15 12:00:00', NULL, FALSE, 42),
('mN0bV9', 'https://www.ai-today.com/news/future-of-large-language-models', 1, '2025-06-01 07:00:00', NULL, FALSE, 201),
('gT4rF2', 'https://www.project-management.com/tips-for-agile-sprints', 1, '2025-06-05 16:45:00', NULL, FALSE, 33),
('yU7iO8', 'https://www.personal-finance.com/budgeting-101', 1, '2025-07-10 09:15:00', NULL, TRUE, 3),
('eD1cS2', 'https://www.health-weekly.com/10-tips-for-better-sleep', 1, '2025-07-22 11:00:00', NULL, FALSE, 98),
('wA3sD4', 'https://www.coding-challenges.com/daily-problem-fizzbuzz', 1, '2025-08-01 10:00:00', NULL, FALSE, 19),
('fG5hJ6', 'https://www.devops-insider.com/cicd-pipelines-with-github-actions', 1, '2025-08-15 13:20:00', NULL, FALSE, 64),
('lK8jH9', 'https://www.data-science.com/pandas-vs-polars-comparison', 1, '2025-09-01 15:00:00', NULL, FALSE, 72),
('zX7cV6', 'https://www.web-design.com/css-tricks-for-flexbox', 1, '2025-09-10 17:00:00', NULL, FALSE, 55),
('bN4mV3', 'httpss://www.secure-coding.com/sql-injection-prevention', 1, '2025-09-20 10:30:00', NULL, TRUE, 8),
('pQ2wE1', 'https://www.game-dev.com/unreal-engine-5-beginners-guide', 1, '2025-10-01 19:00:00', NULL, FALSE, 110),
('rT5yU6', 'https://www.local-eats.com/best-restaurants-in-town', 1, '2025-10-15 20:00:00', '2025-10-20 00:00:00', FALSE, 30);