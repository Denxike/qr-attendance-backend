-- Update ALL users with the correct password hash for "StrongPass123!"
UPDATE "user" 
SET password = '$2b$10$MKp5gQdGVBFDTmHChVnI3Ogv2ujxZAC6MIGB5Xs4JaU77.WZGr.Ku';

-- Verify the update
SELECT id, email, role, full_name, 
       LEFT(password, 20) || '...' as password_preview 
FROM "user" 
ORDER BY id 
LIMIT 10;
