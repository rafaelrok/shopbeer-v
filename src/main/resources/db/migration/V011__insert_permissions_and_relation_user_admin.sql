INSERT INTO permission VALUES (1, 'ROLE_REGISTER_CITY');
INSERT INTO permission VALUES (2, 'ROLE_REGISTRATION_USER');

INSERT INTO user_employee_role (code_user_employee, roles) VALUES (1, 'ADMIN');
INSERT INTO user_employee_role (code_user_employee, roles) VALUES (1, 'USER');

INSERT INTO group_permission (code_group_employee, code_permission) VALUES (1, 1);
INSERT INTO group_permission (code_group_employee, code_permission) VALUES (1, 2);

INSERT INTO user_employee_group (code_user_employee, code_group_employee) VALUES (
    (SELECT code FROM user_employee WHERE email = 'admin@shopbeer.com'), 1);

-- INSERT INTO user_employee_group (code_user_employee, code_group_employee) VALUES (1, 1);