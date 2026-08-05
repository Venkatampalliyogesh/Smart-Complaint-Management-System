-- Smart Complaint Management System - Seed Data
-- Default password for all users: password
-- BCrypt hash: $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi

-- Roles
INSERT IGNORE INTO roles (id, name, description, created_at, created_by) VALUES
(1, 'ROLE_ADMIN', 'System administrator with full access', NOW(6), 'system'),
(2, 'ROLE_STAFF', 'Staff member who handles complaints', NOW(6), 'system'),
(3, 'ROLE_USER', 'Regular user who can submit complaints', NOW(6), 'system');

-- Priorities
INSERT IGNORE INTO priorities (id, name, level, description, response_sla_hours, created_at, created_by) VALUES
(1, 'LOW',      1, 'Low priority - respond within 72 hours',  72,  NOW(6), 'system'),
(2, 'MEDIUM',   2, 'Medium priority - respond within 48 hours', 48, NOW(6), 'system'),
(3, 'HIGH',     3, 'High priority - respond within 24 hours', 24,  NOW(6), 'system'),
(4, 'CRITICAL', 4, 'Critical priority - respond within 4 hours',  4,  NOW(6), 'system');

-- Categories
INSERT IGNORE INTO categories (id, name, description, active, created_at, created_by) VALUES
(1, 'Infrastructure',   'Roads, water supply, electricity, and public infrastructure issues', 1, NOW(6), 'system'),
(2, 'Sanitation',       'Garbage collection, drainage, and cleanliness complaints',           1, NOW(6), 'system'),
(3, 'Public Safety',    'Safety concerns, street lighting, and emergency issues',             1, NOW(6), 'system'),
(4, 'Healthcare',       'Hospital, clinic, and public health service complaints',             1, NOW(6), 'system'),
(5, 'Education',        'Schools, colleges, and educational facility issues',                 1, NOW(6), 'system'),
(6, 'Other',            'General complaints not covered by other categories',                 1, NOW(6), 'system');

-- Users
INSERT IGNORE INTO users (id, email, password, first_name, last_name, phone, active, created_at, created_by) VALUES
(1, 'admin@scms.com',  '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'System',  'Admin',  '9876543210', 1, NOW(6), 'system'),
(2, 'staff@scms.com',  '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Support', 'Staff',  '9876543211', 1, NOW(6), 'system'),
(3, 'user@scms.com',   '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'John',    'Doe',    '9876543212', 1, NOW(6), 'system'),
(4, 'jane@scms.com',   '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Jane',    'Smith',  '9876543213', 1, NOW(6), 'system');

-- User Role Assignments
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 3);

-- Sample Complaints
INSERT IGNORE INTO complaints (id, ticket_number, title, description, status, user_id, assigned_to_id, category_id, priority_id, created_at, created_by) VALUES
(1, 'SCMS-001', 'Broken streetlight on Main Road',
 'The streetlight near block 12 has been non-functional for two weeks, causing safety concerns at night.',
 'SUBMITTED', 3, NULL, 3, 2, NOW(6), 'system'),

(2, 'SCMS-002', 'Garbage not collected for 5 days',
 'Garbage bins in Sector 7 have not been emptied for five consecutive days causing foul smell.',
 'IN_PROGRESS', 4, 2, 2, 3, NOW(6), 'system'),

(3, 'SCMS-003', 'Water supply disruption',
 'Intermittent water supply in Ward 5 since Monday morning affecting 200+ households.',
 'RESOLVED', 3, 2, 1, 4, NOW(6), 'system');

-- Complaint History
INSERT IGNORE INTO complaint_history (id, complaint_id, changed_by_id, previous_status, new_status, comment, created_at) VALUES
(1, 2, 2, 'ASSIGNED', 'IN_PROGRESS', 'Assigned to staff for investigation and resolution.', NOW(6)),
(2, 3, 2, 'IN_PROGRESS', 'RESOLVED', 'Water pipeline repaired. Supply restored to all affected households.', NOW(6));

-- Notifications
INSERT IGNORE INTO notifications (id, user_id, title, message, type, is_read, reference_id, created_at, created_by) VALUES
(1, 3, 'Complaint Assigned', 'Your complaint SCMS-002 has been assigned to a staff member.', 'COMPLAINT_ASSIGNED', 0, 2, NOW(6), 'system'),
(2, 3, 'Complaint Resolved', 'Your complaint SCMS-003 has been marked as resolved.', 'COMPLAINT_RESOLVED', 1, 3, NOW(6), 'system'),
(3, 4, 'Complaint Update',   'Your complaint SCMS-002 status changed to IN_PROGRESS.', 'COMPLAINT_UPDATE', 0, 2, NOW(6), 'system');

-- Audit Logs
INSERT IGNORE INTO audit_logs (id, user_id, action, entity_type, entity_id, details, ip_address, created_at) VALUES
(1, 1, 'LOGIN',           'USER',      1, 'Admin logged into the system',           '127.0.0.1', NOW(6)),
(2, 2, 'UPDATE_COMPLAINT','COMPLAINT', 2, 'Status changed from OPEN to IN_PROGRESS','127.0.0.1', NOW(6)),
(3, 2, 'UPDATE_COMPLAINT','COMPLAINT', 3, 'Status changed from IN_PROGRESS to RESOLVED','127.0.0.1', NOW(6));
