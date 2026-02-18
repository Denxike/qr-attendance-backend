-- Create tables (Postgres compatible)

-- User table
CREATE TABLE IF NOT EXISTS "user" (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Department table
CREATE TABLE IF NOT EXISTS department (
    id BIGSERIAL PRIMARY KEY,
    department_name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT
);

-- Teacher table
CREATE TABLE IF NOT EXISTS teacher (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES "user"(id) ON DELETE CASCADE,
    employee_id VARCHAR(50) UNIQUE NOT NULL,
    department_id BIGINT REFERENCES department(id),
    phone_number VARCHAR(20)
);

-- Student table
CREATE TABLE IF NOT EXISTS student (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES "user"(id) ON DELETE CASCADE,
    student_id VARCHAR(50) UNIQUE NOT NULL,
    year_of_study INT,
    department_id BIGINT REFERENCES department(id),
    phone_number VARCHAR(20)
);

-- Course table
CREATE TABLE IF NOT EXISTS course (
    id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(50) UNIQUE NOT NULL,
    course_name VARCHAR(255) NOT NULL,
    description TEXT,
    credits INT,
    semester VARCHAR(50),
    teacher_id BIGINT REFERENCES teacher(id),
    department_id BIGINT REFERENCES department(id),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- QR Session table
CREATE TABLE IF NOT EXISTS qr_session (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES course(id) ON DELETE CASCADE,
    session_token VARCHAR(255) UNIQUE NOT NULL,
    session_name VARCHAR(255),
    expiry_time TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enrollment table
CREATE TABLE IF NOT EXISTS student_course_enrollment (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES student(id) ON DELETE CASCADE,
    course_id BIGINT REFERENCES course(id) ON DELETE CASCADE,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'ENROLLED',
    UNIQUE(student_id, course_id)
);

-- Attendance table
CREATE TABLE IF NOT EXISTS attendance (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES student(id) ON DELETE CASCADE,
    qr_session_id BIGINT REFERENCES qr_session(id) ON DELETE CASCADE,
    marked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    UNIQUE(student_id, qr_session_id)
);

-- Insert admin user (password: StrongPass123!)
INSERT INTO "user" (email, password, full_name, role, is_active)
VALUES (
    'admin@university.edu',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi.',
    'System Admin',
    'ADMIN',
    true
) ON CONFLICT (email) DO NOTHING;

-- Insert sample departments
INSERT INTO department (department_name, description) VALUES
    ('Computer Science', 'CS Department'),
    ('Engineering', 'Engineering Department'),
    ('Business', 'Business Department'),
    ('Mathematics', 'Math Department')
ON CONFLICT (department_name) DO NOTHING;
