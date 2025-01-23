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
    name_system_role varchar(10) NOT NULL UNIQUE,
    user_id bigint NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    CONSTRAINT unique_user_role UNIQUE (user_id, name_system_role)
);

CREATE TABLE IF NOT EXISTS Groups (
    id bigserial PRIMARY KEY,
    name_group varchar(255) NOT NULL,
    count_users bigint NOT NULL,
    created_by bigint NOT NULL,
    created_at timestamp NOT NULL,
    FOREIGN KEY (created_by) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Users_Groups (
    id bigserial PRIMARY KEY,
    id_user bigint NOT NULL,
    id_group bigint NOT NULL,
    FOREIGN KEY (id_user) REFERENCES Users(id),
    FOREIGN KEY (id_group) REFERENCES Groups(id),
    CONSTRAINT unique_user_group UNIQUE (id_user, id_group)
);

CREATE TABLE IF NOT EXISTS Projects (
    id bigserial PRIMARY KEY,
    name_project varchar(50) NOT NULL,
    description varchar(255) NULL,
    created_by bigint NOT NULL,
    created_at timestamp NOT NULL,
    FOREIGN KEY (created_by) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Roles_In_Project (
    id bigserial PRIMARY KEY,
    name_role varchar(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Users_Project (
    id bigserial PRIMARY KEY,
    id_role bigint NOT NULL,
    id_user bigint NOT NULL,
    id_project bigint NOT NULL,
    FOREIGN KEY (id_role) REFERENCES Roles_In_Project(id),
    FOREIGN KEY (id_user) REFERENCES Users(id),
    FOREIGN KEY (id_project) REFERENCES Projects(id),
    CONSTRAINT unique_user_project UNIQUE (id_user, id_project)
);

CREATE TABLE IF NOT EXISTS Task (
    id bigserial PRIMARY KEY,
    name_task varchar(30) NOT NULL,
    title varchar(30) NOT NULL,
    description varchar(255) NULL,
    due_date timestamp NOT NULL,
    project_id bigint,
    CONSTRAINT fk_task_project
        FOREIGN KEY (project_id)
            REFERENCES Projects (id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Task_Status_History (
    id bigserial PRIMARY KEY,
    id_task bigint NOT NULL,
    status_date timestamp NOT NULL,
    status varchar(30) NOT NULL,
    FOREIGN KEY (id_task) REFERENCES Task(id),
    CONSTRAINT unique_task_status UNIQUE (id_task, status_date)
);

CREATE TABLE IF NOT EXISTS Comments (
    id bigserial PRIMARY KEY,
    comment_text text NOT NULL,
    created_at timestamp NOT NULL,
    created_by bigint NOT NULL,
    id_task bigint NOT NULL,
    FOREIGN KEY (created_by) REFERENCES Users(id),
    FOREIGN KEY (id_task) REFERENCES  Task(id)
);

CREATE INDEX IF NOT EXISTS idx_task_comments ON Comments(id_task);


