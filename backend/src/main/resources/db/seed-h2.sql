-- ── Seed data for H2 dev profile ────────────────────────────
-- Password for all users is: Password@123 (BCrypt strength 12)

INSERT INTO ticket_categories (name, description, active) VALUES
('Technical Support',  'Hardware, software and connectivity issues',      TRUE),
('Billing & Payments', 'Invoice disputes, payment failures, refunds',     TRUE),
('Account Management', 'Password resets, profile updates, access issues', TRUE),
('Product Feedback',   'Feature requests and product improvement ideas',  TRUE),
('General Inquiry',    'Any other questions or information requests',     TRUE);

INSERT INTO users (name, email, password, role, phone, active) VALUES
('Admin User',      'admin@serviceflow.com',    '$2a$12$K8.S8z7yIwz9Mv8MHiYy7.p8Gj0HbVJJ2w4wXVkJmkpKqLkCrG6Wy', 'ROLE_ADMIN',    '+91-9000000001', TRUE),
('Support Agent 1', 'support1@serviceflow.com', '$2a$12$K8.S8z7yIwz9Mv8MHiYy7.p8Gj0HbVJJ2w4wXVkJmkpKqLkCrG6Wy', 'ROLE_SUPPORT',  '+91-9000000002', TRUE),
('Support Agent 2', 'support2@serviceflow.com', '$2a$12$K8.S8z7yIwz9Mv8MHiYy7.p8Gj0HbVJJ2w4wXVkJmkpKqLkCrG6Wy', 'ROLE_SUPPORT',  '+91-9000000003', TRUE),
('Alice Customer',  'alice@example.com',        '$2a$12$K8.S8z7yIwz9Mv8MHiYy7.p8Gj0HbVJJ2w4wXVkJmkpKqLkCrG6Wy', 'ROLE_CUSTOMER', '+91-9111111111', TRUE),
('Bob Customer',    'bob@example.com',          '$2a$12$K8.S8z7yIwz9Mv8MHiYy7.p8Gj0HbVJJ2w4wXVkJmkpKqLkCrG6Wy', 'ROLE_CUSTOMER', '+91-9222222222', TRUE);
