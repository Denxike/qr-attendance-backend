-- ============================================
-- QR ATTENDANCE SYSTEM - DATABASE POPULATION
-- ============================================

-- Clear existing data (in correct order due to foreign keys)
DELETE FROM attendance;
DELETE FROM qr_session;
DELETE FROM student_course_enrollment;
DELETE FROM course;
DELETE FROM student;
DELETE FROM teacher;
DELETE FROM department;
DELETE FROM "user";

-- Reset sequences
ALTER SEQUENCE user_id_seq RESTART WITH 1;
ALTER SEQUENCE department_id_seq RESTART WITH 1;
ALTER SEQUENCE student_id_seq RESTART WITH 1;
ALTER SEQUENCE teacher_id_seq RESTART WITH 1;
ALTER SEQUENCE course_id_seq RESTART WITH 1;
ALTER SEQUENCE qr_session_id_seq RESTART WITH 1;
ALTER SEQUENCE attendance_id_seq RESTART WITH 1;
ALTER SEQUENCE student_course_enrollment_id_seq RESTART WITH 1;

-- ============================================
-- 1. DEPARTMENTS (8 departments)
-- ============================================
INSERT INTO department (department_name, description) VALUES
('Computer Science', 'Study of computation, algorithms, and information processing'),
('Business Administration', 'Management, finance, marketing, and entrepreneurship'),
('Civil Engineering', 'Design and construction of infrastructure and buildings'),
('Electrical Engineering', 'Study of electricity, electronics, and electromagnetism'),
('Mechanical Engineering', 'Design and manufacturing of mechanical systems'),
('Education', 'Teacher training and educational leadership'),
('Nursing', 'Healthcare and patient care'),
('Agriculture', 'Crop production, animal science, and agribusiness');

-- ============================================
-- 2. USERS (Super Admin, Admin, Teachers, Students)
-- Password for all: StrongPass123!
-- BCrypt hash: $2a$10$YourHashHere
-- ============================================

-- SUPER ADMIN (COD - Chief of Department)
INSERT INTO "user" (email, password, full_name, role, is_active, created_at, updated_at) VALUES
('cod@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Dr. James Kimani', 'SUPER_ADMIN', true, NOW(), NOW());

-- ADMIN
INSERT INTO "user" (email, password, full_name, role, is_active, created_at, updated_at) VALUES
('admin@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Mary Wanjiku', 'ADMIN', true, NOW(), NOW());

-- TEACHERS (16 teachers - 2 per department)
INSERT INTO "user" (email, password, full_name, role, is_active, created_at, updated_at) VALUES
-- Computer Science
('prof.omondi@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Prof. Peter Omondi', 'TEACHER', true, NOW(), NOW()),
('dr.mutua@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Dr. Grace Mutua', 'TEACHER', true, NOW(), NOW()),

-- Business Administration
('prof.njoroge@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Prof. Samuel Njoroge', 'TEACHER', true, NOW(), NOW()),
('dr.akinyi@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Dr. Faith Akinyi', 'TEACHER', true, NOW(), NOW()),

-- Civil Engineering
('eng.kamau@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Eng. Joseph Kamau', 'TEACHER', true, NOW(), NOW()),
('dr.chebet@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Dr. Ruth Chebet', 'TEACHER', true, NOW(), NOW()),

-- Electrical Engineering
('prof.otieno@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Prof. David Otieno', 'TEACHER', true, NOW(), NOW()),
('dr.mwangi@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Dr. Jane Mwangi', 'TEACHER', true, NOW(), NOW()),

-- Mechanical Engineering
('eng.kipchoge@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Eng. Daniel Kipchoge', 'TEACHER', true, NOW(), NOW()),
('dr.wambui@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Dr. Ann Wambui', 'TEACHER', true, NOW(), NOW()),

-- Education
('prof.kihara@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Prof. Michael Kihara', 'TEACHER', true, NOW(), NOW()),
('dr.njeri@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Dr. Lucy Njeri', 'TEACHER', true, NOW(), NOW()),

-- Nursing
('dr.koech@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Dr. Sarah Koech', 'TEACHER', true, NOW(), NOW()),
('nurse.adhiambo@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Nurse Esther Adhiambo', 'TEACHER', true, NOW(), NOW()),

-- Agriculture
('prof.kariuki@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Prof. John Kariuki', 'TEACHER', true, NOW(), NOW()),
('dr.chepkorir@university.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Dr. Nancy Chepkorir', 'TEACHER', true, NOW(), NOW());

-- STUDENTS (40 students - 5 per department)
INSERT INTO "user" (email, password, full_name, role, is_active, created_at, updated_at) VALUES
-- Computer Science Students
('brian.kiprop@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Brian Kiprop', 'STUDENT', true, NOW(), NOW()),
('mercy.wanjiru@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Mercy Wanjiru', 'STUDENT', true, NOW(), NOW()),
('kevin.ochieng@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Kevin Ochieng', 'STUDENT', true, NOW(), NOW()),
('faith.chemutai@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Faith Chemutai', 'STUDENT', true, NOW(), NOW()),
('dennis.maina@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Dennis Maina', 'STUDENT', true, NOW(), NOW()),

-- Business Administration Students
('alice.nyambura@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Alice Nyambura', 'STUDENT', true, NOW(), NOW()),
('victor.onyango@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Victor Onyango', 'STUDENT', true, NOW(), NOW()),
('christine.kibet@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Christine Kibet', 'STUDENT', true, NOW(), NOW()),
('george.wekesa@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'George Wekesa', 'STUDENT', true, NOW(), NOW()),
('jane.muthoni@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Jane Muthoni', 'STUDENT', true, NOW(), NOW()),

-- Civil Engineering Students
('peter.mutai@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Peter Mutai', 'STUDENT', true, NOW(), NOW()),
('eva.wairimu@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Eva Wairimu', 'STUDENT', true, NOW(), NOW()),
('mark.kiprotich@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Mark Kiprotich', 'STUDENT', true, NOW(), NOW()),
('beatrice.wambui@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Beatrice Wambui', 'STUDENT', true, NOW(), NOW()),
('stephen.chege@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Stephen Chege', 'STUDENT', true, NOW(), NOW()),

-- Electrical Engineering Students
('collins.rotich@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Collins Rotich', 'STUDENT', true, NOW(), NOW()),
('martha.njoki@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Martha Njoki', 'STUDENT', true, NOW(), NOW()),
('edwin.korir@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Edwin Korir', 'STUDENT', true, NOW(), NOW()),
('rachel.auma@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Rachel Auma', 'STUDENT', true, NOW(), NOW()),
('daniel.muriithi@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Daniel Muriithi', 'STUDENT', true, NOW(), NOW()),

-- Mechanical Engineering Students
('alex.tanui@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Alex Tanui', 'STUDENT', true, NOW(), NOW()),
('winnie.achieng@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Winnie Achieng', 'STUDENT', true, NOW(), NOW()),
('humphrey.ndungu@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Humphrey Ndungu', 'STUDENT', true, NOW(), NOW()),
('lydia.cherono@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Lydia Cherono', 'STUDENT', true, NOW(), NOW()),
('simon.mugo@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Simon Mugo', 'STUDENT', true, NOW(), NOW()),

-- Education Students
('paul.kiplagat@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Paul Kiplagat', 'STUDENT', true, NOW(), NOW()),
('susan.gathoni@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Susan Gathoni', 'STUDENT', true, NOW(), NOW()),
('andrew.langat@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Andrew Langat', 'STUDENT', true, NOW(), NOW()),
('patricia.wangui@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Patricia Wangui', 'STUDENT', true, NOW(), NOW()),
('francis.kosgei@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Francis Kosgei', 'STUDENT', true, NOW(), NOW()),

-- Nursing Students
('catherine.mutiso@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Catherine Mutiso', 'STUDENT', true, NOW(), NOW()),
('james.kipsang@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'James Kipsang', 'STUDENT', true, NOW(), NOW()),
('rose.njambi@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Rose Njambi', 'STUDENT', true, NOW(), NOW()),
('timothy.bett@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Timothy Bett', 'STUDENT', true, NOW(), NOW()),
('eunice.waweru@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Eunice Waweru', 'STUDENT', true, NOW(), NOW()),

-- Agriculture Students
('joseph.sang@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Joseph Sang', 'STUDENT', true, NOW(), NOW()),
('elizabeth.kamau@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Elizabeth Kamau', 'STUDENT', true, NOW(), NOW()),
('isaac.rotich@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Isaac Rotich', 'STUDENT', true, NOW(), NOW()),
('agnes.nduta@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Agnes Nduta', 'STUDENT', true, NOW(), NOW()),
('david.kiplimo@student.edu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'David Kiplimo', 'STUDENT', true, NOW(), NOW());

-- ============================================
-- 3. TEACHERS (Link users to departments)
-- ============================================
INSERT INTO teacher (user_id, employee_id, department_id, phone_number) VALUES
-- Computer Science
(3, 'EMP001', 1, '+254712345001'),
(4, 'EMP002', 1, '+254712345002'),
-- Business Administration
(5, 'EMP003', 2, '+254712345003'),
(6, 'EMP004', 2, '+254712345004'),
-- Civil Engineering
(7, 'EMP005', 3, '+254712345005'),
(8, 'EMP006', 3, '+254712345006'),
-- Electrical Engineering
(9, 'EMP007', 4, '+254712345007'),
(10, 'EMP008', 4, '+254712345008'),
-- Mechanical Engineering
(11, 'EMP009', 5, '+254712345009'),
(12, 'EMP010', 5, '+254712345010'),
-- Education
(13, 'EMP011', 6, '+254712345011'),
(14, 'EMP012', 6, '+254712345012'),
-- Nursing
(15, 'EMP013', 7, '+254712345013'),
(16, 'EMP014', 7, '+254712345014'),
-- Agriculture
(17, 'EMP015', 8, '+254712345015'),
(18, 'EMP016', 8, '+254712345016');

-- ============================================
-- 4. STUDENTS (Link users to departments)
-- ============================================
INSERT INTO student (user_id, student_id, year_of_study, department_id, phone_number) VALUES
-- Computer Science (Year 1-4)
(19, 'CS22/00001/22', 3, 1, '+254723000001'),
(20, 'CS23/00002/23', 2, 1, '+254723000002'),
(21, 'CS23/00003/23', 2, 1, '+254723000003'),
(22, 'CS24/00004/24', 1, 1, '+254723000004'),
(23, 'CS24/00005/24', 1, 1, '+254723000005'),

-- Business Administration
(24, 'BA22/00001/22', 3, 2, '+254723000006'),
(25, 'BA23/00002/23', 2, 2, '+254723000007'),
(26, 'BA23/00003/23', 2, 2, '+254723000008'),
(27, 'BA24/00004/24', 1, 2, '+254723000009'),
(28, 'BA24/00005/24', 1, 2, '+254723000010'),

-- Civil Engineering
(29, 'CE22/00001/22', 3, 3, '+254723000011'),
(30, 'CE23/00002/23', 2, 3, '+254723000012'),
(31, 'CE23/00003/23', 2, 3, '+254723000013'),
(32, 'CE24/00004/24', 1, 3, '+254723000014'),
(33, 'CE24/00005/24', 1, 3, '+254723000015'),

-- Electrical Engineering
(34, 'EE22/00001/22', 3, 4, '+254723000016'),
(35, 'EE23/00002/23', 2, 4, '+254723000017'),
(36, 'EE23/00003/23', 2, 4, '+254723000018'),
(37, 'EE24/00004/24', 1, 4, '+254723000019'),
(38, 'EE24/00005/24', 1, 4, '+254723000020'),

-- Mechanical Engineering
(39, 'ME22/00001/22', 3, 5, '+254723000021'),
(40, 'ME23/00002/23', 2, 5, '+254723000022'),
(41, 'ME23/00003/23', 2, 5, '+254723000023'),
(42, 'ME24/00004/24', 1, 5, '+254723000024'),
(43, 'ME24/00005/24', 1, 5, '+254723000025'),

-- Education
(44, 'ED22/00001/22', 3, 6, '+254723000026'),
(45, 'ED23/00002/23', 2, 6, '+254723000027'),
(46, 'ED23/00003/23', 2, 6, '+254723000028'),
(47, 'ED24/00004/24', 1, 6, '+254723000029'),
(48, 'ED24/00005/24', 1, 6, '+254723000030'),

-- Nursing
(49, 'NU22/00001/22', 3, 7, '+254723000031'),
(50, 'NU23/00002/23', 2, 7, '+254723000032'),
(51, 'NU23/00003/23', 2, 7, '+254723000033'),
(52, 'NU24/00004/24', 1, 7, '+254723000034'),
(53, 'NU24/00005/24', 1, 7, '+254723000035'),

-- Agriculture
(54, 'AG22/00001/22', 3, 8, '+254723000036'),
(55, 'AG23/00002/23', 2, 8, '+254723000037'),
(56, 'AG23/00003/23', 2, 8, '+254723000038'),
(57, 'AG24/00004/24', 1, 8, '+254723000039'),
(58, 'AG24/00005/24', 1, 8, '+254723000040');

-- ============================================
-- 5. COURSES (32 courses - 4 per department)
-- ============================================

-- Computer Science Courses
INSERT INTO course (course_code, course_name, description, credits, semester, teacher_id, department_id, is_active, created_at) VALUES
('CS101', 'Introduction to Programming', 'Fundamentals of programming using Java', 4, '1', 1, 1, true, NOW()),
('CS201', 'Data Structures & Algorithms', 'Advanced data structures and algorithm design', 4, '2', 1, 1, true, NOW()),
('CS301', 'Database Systems', 'Relational databases, SQL, and database design', 3, '1', 2, 1, true, NOW()),
('CS401', 'Software Engineering', 'Software development lifecycle and methodologies', 3, '2', 2, 1, true, NOW()),

-- Business Administration Courses
('BA101', 'Principles of Management', 'Introduction to management concepts and practices', 3, '1', 3, 2, true, NOW()),
('BA201', 'Financial Accounting', 'Basic accounting principles and financial statements', 3, '2', 3, 2, true, NOW()),
('BA301', 'Marketing Management', 'Marketing strategies and consumer behavior', 3, '1', 4, 2, true, NOW()),
('BA401', 'Strategic Management', 'Strategic planning and business policy', 3, '2', 4, 2, true, NOW()),

-- Civil Engineering Courses
('CE101', 'Engineering Mechanics', 'Statics and dynamics of engineering structures', 4, '1', 5, 3, true, NOW()),
('CE201', 'Structural Analysis', 'Analysis of beams, frames, and trusses', 4, '2', 5, 3, true, NOW()),
('CE301', 'Concrete Technology', 'Properties and design of concrete structures', 3, '1', 6, 3, true, NOW()),
('CE401', 'Highway Engineering', 'Design and construction of roads and highways', 3, '2', 6, 3, true, NOW()),

-- Electrical Engineering Courses
('EE101', 'Circuit Theory', 'Basic electrical circuits and network analysis', 4, '1', 7, 4, true, NOW()),
('EE201', 'Electronics', 'Semiconductor devices and electronic circuits', 4, '2', 7, 4, true, NOW()),
('EE301', 'Power Systems', 'Generation, transmission, and distribution of power', 3, '1', 8, 4, true, NOW()),
('EE401', 'Control Systems', 'Feedback control and automation systems', 3, '2', 8, 4, true, NOW()),

-- Mechanical Engineering Courses
('ME101', 'Engineering Drawing', 'Technical drawing and CAD fundamentals', 3, '1', 9, 5, true, NOW()),
('ME201', 'Thermodynamics', 'Heat transfer and energy conversion', 4, '2', 9, 5, true, NOW()),
('ME301', 'Manufacturing Processes', 'Machining, welding, and fabrication techniques', 3, '1', 10, 5, true, NOW()),
('ME401', 'Machine Design', 'Design of mechanical components and systems', 4, '2', 10, 5, true, NOW()),

-- Education Courses
('ED101', 'Foundations of Education', 'Educational philosophy and psychology', 3, '1', 11, 6, true, NOW()),
('ED201', 'Curriculum Development', 'Designing and evaluating educational curricula', 3, '2', 11, 6, true, NOW()),
('ED301', 'Educational Technology', 'Using technology in teaching and learning', 3, '1', 12, 6, true, NOW()),
('ED401', 'Classroom Management', 'Strategies for effective classroom control', 3, '2', 12, 6, true, NOW()),

-- Nursing Courses
('NU101', 'Anatomy & Physiology', 'Human body structure and function', 4, '1', 13, 7, true, NOW()),
('NU201', 'Fundamentals of Nursing', 'Basic nursing skills and patient care', 4, '2', 13, 7, true, NOW()),
('NU301', 'Medical-Surgical Nursing', 'Nursing care for adult patients', 4, '1', 14, 7, true, NOW()),
('NU401', 'Community Health Nursing', 'Public health and community care', 3, '2', 14, 7, true, NOW()),

-- Agriculture Courses
('AG101', 'Crop Production', 'Principles of crop cultivation and management', 3, '1', 15, 8, true, NOW()),
('AG201', 'Animal Science', 'Livestock production and management', 3, '2', 15, 8, true, NOW()),
('AG301', 'Soil Science', 'Soil properties, fertility, and conservation', 3, '1', 16, 8, true, NOW()),
('AG401', 'Agribusiness Management', 'Business principles in agricultural enterprises', 3, '2', 16, 8, true, NOW());

-- ============================================
-- 6. STUDENT ENROLLMENTS (Each student in 3-4 courses)
-- ============================================

-- Computer Science Students
INSERT INTO student_course_enrollment (student_id, course_id, enrollment_date, status) VALUES
(1, 1, '2024-09-01', 'ACTIVE'), (1, 2, '2024-09-01', 'ACTIVE'), (1, 3, '2024-09-01', 'ACTIVE'),
(2, 1, '2024-09-01', 'ACTIVE'), (2, 2, '2024-09-01', 'ACTIVE'), (2, 4, '2024-09-01', 'ACTIVE'),
(3, 1, '2024-09-01', 'ACTIVE'), (3, 3, '2024-09-01', 'ACTIVE'), (3, 4, '2024-09-01', 'ACTIVE'),
(4, 1, '2024-09-01', 'ACTIVE'), (4, 2, '2024-09-01', 'ACTIVE'),
(5, 1, '2024-09-01', 'ACTIVE'), (5, 3, '2024-09-01', 'ACTIVE'),

-- Business Administration Students
(6, 5, '2024-09-01', 'ACTIVE'), (6, 6, '2024-09-01', 'ACTIVE'), (6, 7, '2024-09-01', 'ACTIVE'),
(7, 5, '2024-09-01', 'ACTIVE'), (7, 6, '2024-09-01', 'ACTIVE'), (7, 8, '2024-09-01', 'ACTIVE'),
(8, 5, '2024-09-01', 'ACTIVE'), (8, 7, '2024-09-01', 'ACTIVE'), (8, 8, '2024-09-01', 'ACTIVE'),
(9, 5, '2024-09-01', 'ACTIVE'), (9, 6, '2024-09-01', 'ACTIVE'),
(10, 5, '2024-09-01', 'ACTIVE'), (10, 7, '2024-09-01', 'ACTIVE'),

-- Civil Engineering Students
(11, 9, '2024-09-01', 'ACTIVE'), (11, 10, '2024-09-01', 'ACTIVE'), (11, 11, '2024-09-01', 'ACTIVE'),
(12, 9, '2024-09-01', 'ACTIVE'), (12, 10, '2024-09-01', 'ACTIVE'), (12, 12, '2024-09-01', 'ACTIVE'),
(13, 9, '2024-09-01', 'ACTIVE'), (13, 11, '2024-09-01', 'ACTIVE'), (13, 12, '2024-09-01', 'ACTIVE'),
(14, 9, '2024-09-01', 'ACTIVE'), (14, 10, '2024-09-01', 'ACTIVE'),
(15, 9, '2024-09-01', 'ACTIVE'), (15, 11, '2024-09-01', 'ACTIVE'),

-- Electrical Engineering Students
(16, 13, '2024-09-01', 'ACTIVE'), (16, 14, '2024-09-01', 'ACTIVE'), (16, 15, '2024-09-01', 'ACTIVE'),
(17, 13, '2024-09-01', 'ACTIVE'), (17, 14, '2024-09-01', 'ACTIVE'), (17, 16, '2024-09-01', 'ACTIVE'),
(18, 13, '2024-09-01', 'ACTIVE'), (18, 15, '2024-09-01', 'ACTIVE'), (18, 16, '2024-09-01', 'ACTIVE'),
(19, 13, '2024-09-01', 'ACTIVE'), (19, 14, '2024-09-01', 'ACTIVE'),
(20, 13, '2024-09-01', 'ACTIVE'), (20, 15, '2024-09-01', 'ACTIVE'),

-- Mechanical Engineering Students
(21, 17, '2024-09-01', 'ACTIVE'), (21, 18, '2024-09-01', 'ACTIVE'), (21, 19, '2024-09-01', 'ACTIVE'),
(22, 17, '2024-09-01', 'ACTIVE'), (22, 18, '2024-09-01', 'ACTIVE'), (22, 20, '2024-09-01', 'ACTIVE'),
(23, 17, '2024-09-01', 'ACTIVE'), (23, 19, '2024-09-01', 'ACTIVE'), (23, 20, '2024-09-01', 'ACTIVE'),
(24, 17, '2024-09-01', 'ACTIVE'), (24, 18, '2024-09-01', 'ACTIVE'),
(25, 17, '2024-09-01', 'ACTIVE'), (25, 19, '2024-09-01', 'ACTIVE'),

-- Education Students
(26, 21, '2024-09-01', 'ACTIVE'), (26, 22, '2024-09-01', 'ACTIVE'), (26, 23, '2024-09-01', 'ACTIVE'),
(27, 21, '2024-09-01', 'ACTIVE'), (27, 22, '2024-09-01', 'ACTIVE'), (27, 24, '2024-09-01', 'ACTIVE'),
(28, 21, '2024-09-01', 'ACTIVE'), (28, 23, '2024-09-01', 'ACTIVE'), (28, 24, '2024-09-01', 'ACTIVE'),
(29, 21, '2024-09-01', 'ACTIVE'), (29, 22, '2024-09-01', 'ACTIVE'),
(30, 21, '2024-09-01', 'ACTIVE'), (30, 23, '2024-09-01', 'ACTIVE'),

-- Nursing Students
(31, 25, '2024-09-01', 'ACTIVE'), (31, 26, '2024-09-01', 'ACTIVE'), (31, 27, '2024-09-01', 'ACTIVE'),
(32, 25, '2024-09-01', 'ACTIVE'), (32, 26, '2024-09-01', 'ACTIVE'), (32, 28, '2024-09-01', 'ACTIVE'),
(33, 25, '2024-09-01', 'ACTIVE'), (33, 27, '2024-09-01', 'ACTIVE'), (33, 28, '2024-09-01', 'ACTIVE'),
(34, 25, '2024-09-01', 'ACTIVE'), (34, 26, '2024-09-01', 'ACTIVE'),
(35, 25, '2024-09-01', 'ACTIVE'), (35, 27, '2024-09-01', 'ACTIVE'),

-- Agriculture Students
(36, 29, '2024-09-01', 'ACTIVE'), (36, 30, '2024-09-01', 'ACTIVE'), (36, 31, '2024-09-01', 'ACTIVE'),
(37, 29, '2024-09-01', 'ACTIVE'), (37, 30, '2024-09-01', 'ACTIVE'), (37, 32, '2024-09-01', 'ACTIVE'),
(38, 29, '2024-09-01', 'ACTIVE'), (38, 31, '2024-09-01', 'ACTIVE'), (38, 32, '2024-09-01', 'ACTIVE'),
(39, 29, '2024-09-01', 'ACTIVE'), (39, 30, '2024-09-01', 'ACTIVE'),
(40, 29, '2024-09-01', 'ACTIVE'), (40, 31, '2024-09-01', 'ACTIVE');

-- ============================================
-- 7. SAMPLE QR SESSIONS (Last 2 weeks)
-- ============================================

-- Week 1 sessions
INSERT INTO qr_session (course_id, session_token, session_name, expiry_time, is_active, created_at) VALUES
(1, 'a1b2c3d4-e5f6-47a8-b9c0-d1e2f3a4b5c6', 'CS101 - Week 1 Lecture', NOW() - INTERVAL '13 days' + INTERVAL '30 minutes', false, NOW() - INTERVAL '13 days'),
(2, 'b2c3d4e5-f6a7-48b9-c0d1-e2f3a4b5c6d7', 'CS201 - Week 1 Lab', NOW() - INTERVAL '12 days' + INTERVAL '30 minutes', false, NOW() - INTERVAL '12 days'),
(5, 'c3d4e5f6-a7b8-49c0-d1e2-f3a4b5c6d7e8', 'BA101 - Week 1 Lecture', NOW() - INTERVAL '11 days' + INTERVAL '30 minutes', false, NOW() - INTERVAL '11 days'),
(9, 'd4e5f6a7-b8c9-40d1-e2f3-a4b5c6d7e8f9', 'CE101 - Week 1 Practical', NOW() - INTERVAL '10 days' + INTERVAL '30 minutes', false, NOW() - INTERVAL '10 days'),

-- Week 2 sessions
(1, 'e5f6a7b8-c9d0-41e2-f3a4-b5c6d7e8f9a0', 'CS101 - Week 2 Lecture', NOW() - INTERVAL '6 days' + INTERVAL '30 minutes', false, NOW() - INTERVAL '6 days'),
(3, 'f6a7b8c9-d0e1-42f3-a4b5-c6d7e8f9a0b1', 'CS301 - Week 2 Lab', NOW() - INTERVAL '5 days' + INTERVAL '30 minutes', false, NOW() - INTERVAL '5 days'),
(6, 'a7b8c9d0-e1f2-43a4-b5c6-d7e8f9a0b1c2', 'BA201 - Week 2 Lecture', NOW() - INTERVAL '4 days' + INTERVAL '30 minutes', false, NOW() - INTERVAL '4 days'),
(13, 'b8c9d0e1-f2a3-44b5-c6d7-e8f9a0b1c2d3', 'EE101 - Week 2 Practical', NOW() - INTERVAL '3 days' + INTERVAL '30 minutes', false, NOW() - INTERVAL '3 days');

-- ============================================
-- 8. SAMPLE ATTENDANCE RECORDS
-- ============================================

-- Attendance for CS101 Week 1
INSERT INTO attendance (student_id, qr_session_id, marked_at, status) VALUES
(1, 1, NOW() - INTERVAL '13 days', 'PRESENT'),
(2, 1, NOW() - INTERVAL '13 days', 'PRESENT'),
(3, 1, NOW() - INTERVAL '13 days', 'PRESENT'),
(4, 1, NOW() - INTERVAL '13 days' + INTERVAL '2 minutes', 'PRESENT'),

-- Attendance for CS201 Week 1
(1, 2, NOW() - INTERVAL '12 days', 'PRESENT'),
(2, 2, NOW() - INTERVAL '12 days', 'PRESENT'),
(3, 2, NOW() - INTERVAL '12 days', 'PRESENT'),

-- Attendance for BA101 Week 1
(6, 3, NOW() - INTERVAL '11 days', 'PRESENT'),
(7, 3, NOW() - INTERVAL '11 days', 'PRESENT'),
(8, 3, NOW() - INTERVAL '11 days' + INTERVAL '3 minutes', 'PRESENT'),
(9, 3, NOW() - INTERVAL '11 days', 'PRESENT'),

-- Attendance for CE101 Week 1
(11, 4, NOW() - INTERVAL '10 days', 'PRESENT'),
(12, 4, NOW() - INTERVAL '10 days', 'PRESENT'),
(13, 4, NOW() - INTERVAL '10 days' + INTERVAL '1 minute', 'PRESENT'),

-- Attendance for CS101 Week 2
(1, 5, NOW() - INTERVAL '6 days', 'PRESENT'),
(2, 5, NOW() - INTERVAL '6 days', 'PRESENT'),
(4, 5, NOW() - INTERVAL '6 days' + INTERVAL '4 minutes', 'PRESENT'),
(5, 5, NOW() - INTERVAL '6 days', 'PRESENT'),

-- Attendance for CS301 Week 2
(1, 6, NOW() - INTERVAL '5 days', 'PRESENT'),
(3, 6, NOW() - INTERVAL '5 days', 'PRESENT'),
(5, 6, NOW() - INTERVAL '5 days' + INTERVAL '2 minutes', 'PRESENT'),

-- Attendance for BA201 Week 2
(6, 7, NOW() - INTERVAL '4 days', 'PRESENT'),
(7, 7, NOW() - INTERVAL '4 days', 'PRESENT'),
(9, 7, NOW() - INTERVAL '4 days', 'PRESENT'),

-- Attendance for EE101 Week 2
(16, 8, NOW() - INTERVAL '3 days', 'PRESENT'),
(17, 8, NOW() - INTERVAL '3 days', 'PRESENT'),
(19, 8, NOW() - INTERVAL '3 days' + INTERVAL '1 minute', 'PRESENT');

-- ============================================
-- VERIFICATION QUERIES
-- ============================================
SELECT 'Database populated successfully!' AS status;
SELECT 'Total Users: ' || COUNT(*) FROM "user";
SELECT 'Total Departments: ' || COUNT(*) FROM department;
SELECT 'Total Teachers: ' || COUNT(*) FROM teacher;
SELECT 'Total Students: ' || COUNT(*) FROM student;
SELECT 'Total Courses: ' || COUNT(*) FROM course;
SELECT 'Total Enrollments: ' || COUNT(*) FROM student_course_enrollment;
SELECT 'Total QR Sessions: ' || COUNT(*) FROM qr_session;
SELECT 'Total Attendance Records: ' || COUNT(*) FROM attendance;
