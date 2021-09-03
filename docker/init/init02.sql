-- CREATE TABLE IF NOT EXISTS public.users
-- (
--
--     insert_date_time text COLLATE pg_catalog."default",
--     insert_user_id text COLLATE pg_catalog."default",
--     is_deleted bool,
--     last_update_date_time text COLLATE pg_catalog."default",
--     last_update_user_id text COLLATE pg_catalog."default",
--     enabled bool,
--     first_name text COLLATE pg_catalog."default",
--     gender text COLLATE pg_catalog."default",
--     last_name text COLLATE pg_catalog."default",
--     user_name text COLLATE pg_catalog."default",
--     role_id int,
--     pass_word text COLLATE pg_catalog."default"
--
--     )
--
--     TABLESPACE pg_default;
--
-- ALTER TABLE public.users
--     OWNER to postgres;
--
--
-- CREATE TABLE IF NOT EXISTS public.roles
-- (
--
--     insert_date_time text COLLATE pg_catalog."default",
--     insert_user_id text COLLATE pg_catalog."default",
--     is_deleted bool,
--     last_update_date_time text COLLATE pg_catalog."default",
--     last_update_user_id text COLLATE pg_catalog."default",
--     description text COLLATE pg_catalog."default"
--
--     )
--
--     TABLESPACE pg_default;
--
-- ALTER TABLE public.roles
--     OWNER to postgres;


insert into roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, description)
VALUES ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1, 'Admin'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1, 'Manager'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1, 'Employee');
insert into users(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, enabled,
                  first_name, gender, last_name, user_name, role_id, pass_word)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1, true, 'admin', 'MALE', 'admin', 'admin@admin.com',
        1, '$2a$10$Q7ilQ6Hv11qpU0T7xfMzMeqxoPXkvhTVXxFqg0UL2xvLnhNqB7vba');
