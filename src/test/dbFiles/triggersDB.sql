--AUTO INJECT ROLE 'USER'
CREATE OR REPLACE FUNCTION assign_default_role()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO System_Roles (name_system_role, user_id)
    VALUES ('USER', NEW.id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_user_insert
AFTER INSERT ON Users
FOR EACH ROW
EXECUTE FUNCTION assign_default_role();
--