insert into member (email, name, password, phone_number, role) values
('user1@email.com', '사용자1', '$2a$10$ZnRKeZV.Js4eZBY49pd2o.i939rVvgvLszQ1Sg6BnEjmXWGaQ2k6C', '010-1111-1111', 'USER'),
('admin1@email.com', '관리자1', '$2a$10$R7ZL5RS.i3b2/Vjar.O4deFQsZa8ZOdJ16ZjtwXmCdh/WljgXRHeG', '010-1111-1111', 'ADMIN');

insert into board (member_id, title, content, created_at, modified_at) values
(1, 'Test Board 1', 'This is a test board content 1.', '2024-01-01 00:00:00', '2024-01-01 00:00:00'),
(1, 'Test Board 2', 'This is a test board content 2.', '2024-01-05 00:00:00', '2024-01-05 00:00:00'),
(1, 'Test Board 3', 'This is a test board content 3.', '2024-01-10 00:00:00', '2024-01-10 00:00:00');

insert into board (member_id, title, content, created_at, modified_at, deleted_at) values
(1, 'Deleted Board', 'This board has been deleted.', '2024-01-01 00:00:00', '2024-01-01 00:00:00', '2024-01-11 00:00:00');
