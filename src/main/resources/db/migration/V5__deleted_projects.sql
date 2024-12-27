CREATE TABLE IF NOT EXISTS Deleted_Projects (
    id bigint PRIMARY KEY,
    name_project varchar(50),
    description text,
    deleted_at timestamp NOT NULL,
    deleted_by varchar(40) NOT NULL
);