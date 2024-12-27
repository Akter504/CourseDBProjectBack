CREATE OR REPLACE FUNCTION log_deleted_project()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO Deleted_Projects (id, name_project, description, deleted_at, deleted_by)
    VALUES (OLD.id, OLD.name_project, OLD.description, NOW(), OLD.created_by);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_log_deleted_project
    BEFORE DELETE ON Projects
    FOR EACH ROW
EXECUTE FUNCTION log_deleted_project();