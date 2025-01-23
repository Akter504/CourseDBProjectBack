CREATE TABLE IF NOT EXISTS Task (
    id bigserial PRIMARY KEY,
    name_task varchar(30) NOT NULL,
    title varchar(30) NOT NULL,
    description varchar(255) NULL,
    due_date timestamp NOT NULL,
    project_id bigint NOT NULL,
    status_id bigint DEFAULT 1,
    FOREIGN KEY (project_id) REFERENCES Projects(id),
    FOREIGN KEY (status_id) REFERENCES Task_status(id)
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