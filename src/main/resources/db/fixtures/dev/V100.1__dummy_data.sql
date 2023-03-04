INSERT INTO users
(id, email,             first_name, blocked,        email_verified,  phone,             phone_verified,   created_at,         referral_code,  deactivated,       password                                                     )
VALUES
(12, 'test2@gmail.com', 'test2',     'false',         'true',        '+91-1234567891',   'true',            CURRENT_TIMESTAMP,  'AXTRF',        false,    '{bcrypt}$2a$10$PSkMjVGsoJTpv4ogXKFXKOhZmxx2J5HpkVWIvzWS2L8Xbc8ct0GIG'),
(13, 'test3@gmail.com', 'test3',     'false',         'true',        '+91-9876543210',   'true',            CURRENT_TIMESTAMP,  'AXTSP',        false,    '{bcrypt}$2a$10$PSkMjVGsoJTpv4ogXKFXKOhZmxx2J5HpkVWIvzWS2L8Xbc8ct0GIG');

