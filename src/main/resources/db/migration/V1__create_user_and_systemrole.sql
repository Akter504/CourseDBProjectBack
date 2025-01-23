CREATE TABLE IF NOT EXISTS Users (
    id bigserial PRIMARY KEY,
    username varchar(30) NOT NULL UNIQUE,
    email varchar(40) NOT NULL UNIQUE,
    phone_number varchar(20) NOT NULL UNIQUE,
    surname_user varchar(20) NOT NULL,
    password_hash varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS System_Roles (
    id bigserial PRIMARY KEY,
    name_system_role varchar(10) NOT NULL,
    user_id bigint NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT unique_user_role UNIQUE (user_id, name_system_role)
);