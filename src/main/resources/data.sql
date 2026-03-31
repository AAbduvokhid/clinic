INSERT INTO roles (name, created_at, updated_at)
SELECT 'ROLE_USER', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name, created_at, updated_at)
SELECT 'ROLE_ADMIN', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO departments (name, created_at, updated_at)
SELECT 'Registration', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM departments WHERE name = 'Registration');

INSERT INTO departments (name, created_at, updated_at)
SELECT 'Laboratory', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM departments WHERE name = 'Laboratory');

INSERT INTO departments (name, created_at, updated_at)
SELECT 'Ultrasound', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM departments WHERE name = 'Ultrasound');

INSERT INTO departments (name, created_at, updated_at)
SELECT 'X-Ray', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM departments WHERE name = 'X-Ray');

INSERT INTO departments (name, created_at, updated_at)
SELECT 'MRI', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM departments WHERE name = 'MRI');

INSERT INTO departments (name, created_at, updated_at)
SELECT 'MSCT', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM departments WHERE name = 'MSCT');

INSERT INTO departments (name, created_at, updated_at)
SELECT 'Cardiology', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM departments WHERE name = 'Cardiology');

INSERT INTO departments (name, created_at, updated_at)
SELECT 'Therapy', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM departments WHERE name = 'Therapy');

INSERT INTO departments (name, created_at, updated_at)
SELECT 'Neurology', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM departments WHERE name = 'Neurology');

INSERT INTO departments (name, created_at, updated_at)
SELECT 'ENT', NOW(), NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM departments WHERE name = 'ENT');

INSERT INTO users (status, username, password, first_name, last_name, phone, department_id, created_at, updated_at)
SELECT 'ACTIVE', 'admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
       'Admin', 'User', '+998901234567', 1, NOW(), NOW()
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');
UPDATE users
SET password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.'
WHERE username = 'admin';