-- Update ALL users with the correct password hash for "StrongPass123!"
UPDATE "user" 
SET password = '$2a$10$Okkc0c3sxHl2.5vw1oFYB.ee8OP7LQCqg6HWFkr65SDBSAxlJNKT';

-- Verify the update
SELECT id, email, role, full_name, 
       LEFT(password, 20) || '...' as password_preview 
FROM "user" 
ORDER BY id 
LIMIT 10;
