-- Insert sample users
-- Password: 'password' (bcrypt encoded)
INSERT INTO users (id, username, password, created_at, updated_at) 
VALUES 
(1, 'user1', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'user2', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert user roles
INSERT INTO user_roles (user_id, role) 
VALUES 
(1, 'USER'),
(1, 'ADMIN'),
(2, 'USER');

-- Insert sample accounts
INSERT INTO accounts (id, account_number, balance, account_type, user_id, created_at, updated_at) 
VALUES 
(1, '1000001', 5000.00, 'SAVINGS', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '1000002', 2500.00, 'CHECKING', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '1000003', 10000.00, 'SAVINGS', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, '1000004', 3500.00, 'CHECKING', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample transactions for user1's savings account
INSERT INTO transactions (transaction_type, amount, description, transaction_date, account_id, created_at, updated_at) 
VALUES 
('CREDIT', 1000.00, 'Salary deposit', DATEADD('DAY', -25, CURRENT_DATE), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEBIT', 200.00, 'ATM withdrawal', DATEADD('DAY', -20, CURRENT_DATE), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CREDIT', 500.00, 'Refund', DATEADD('DAY', -15, CURRENT_DATE), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEBIT', 300.00, 'Transfer to checking', DATEADD('DAY', -10, CURRENT_DATE), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CREDIT', 2000.00, 'Bonus deposit', DATEADD('DAY', -5, CURRENT_DATE), 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample transactions for user1's checking account
INSERT INTO transactions (transaction_type, amount, description, transaction_date, account_id, created_at, updated_at) 
VALUES 
('CREDIT', 300.00, 'Transfer from savings', DATEADD('DAY', -10, CURRENT_DATE), 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEBIT', 150.00, 'Grocery shopping', DATEADD('DAY', -8, CURRENT_DATE), 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEBIT', 65.00, 'Utility bill payment', DATEADD('DAY', -6, CURRENT_DATE), 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEBIT', 120.00, 'Online shopping', DATEADD('DAY', -4, CURRENT_DATE), 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CREDIT', 50.00, 'Refund from online store', DATEADD('DAY', -1, CURRENT_DATE), 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample transactions for user2's savings account
INSERT INTO transactions (transaction_type, amount, description, transaction_date, account_id, created_at, updated_at) 
VALUES 
('CREDIT', 5000.00, 'Initial deposit', DATEADD('DAY', -30, CURRENT_DATE), 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CREDIT', 3000.00, 'Salary deposit', DATEADD('DAY', -25, CURRENT_DATE), 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEBIT', 500.00, 'Transfer to checking', DATEADD('DAY', -20, CURRENT_DATE), 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CREDIT', 2500.00, 'Investment return', DATEADD('DAY', -15, CURRENT_DATE), 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample transactions for user2's checking account
INSERT INTO transactions (transaction_type, amount, description, transaction_date, account_id, created_at, updated_at) 
VALUES 
('CREDIT', 500.00, 'Transfer from savings', DATEADD('DAY', -20, CURRENT_DATE), 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEBIT', 200.00, 'Rent payment', DATEADD('DAY', -15, CURRENT_DATE), 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEBIT', 150.00, 'Electricity bill', DATEADD('DAY', -10, CURRENT_DATE), 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEBIT', 80.00, 'Phone bill', DATEADD('DAY', -5, CURRENT_DATE), 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEBIT', 70.00, 'Internet bill', DATEADD('DAY', -2, CURRENT_DATE), 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 