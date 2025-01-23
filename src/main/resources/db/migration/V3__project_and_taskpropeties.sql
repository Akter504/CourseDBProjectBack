CREATE TABLE IF NOT EXISTS Projects (
    id bigserial PRIMARY KEY,
    name_project varchar(50) NOT NULL,
    description text NULL,
    created_by varchar(40) NOT NULL,
    created_at timestamp NOT NULL
);

CREATE TABLE Task_status (
    id bigserial PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(100) NOT NULL,
    color VARCHAR(7) NOT NULL
);