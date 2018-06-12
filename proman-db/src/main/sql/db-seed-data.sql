-- Copyright 2018-2019, https://beingtechie.io

-- Script: db-seed.sql
-- Description: Setup seed data that is essential to bring up applications in running state.
-- Version: 1.0
-- Author: Thribhuvan Krishnamurthy

INSERT INTO proman.GROUPS (ID, UUID, NAME, DESCRIPTION, CREATED_BY)
		VALUES 
		(1, 101, 'Manage Users', 'User Management', CURRENT_USER),
		(2, 102, 'Manage Boards', 'Board Management', CURRENT_USER),
		(3, 103, 'Manage Projects', 'Project Management', CURRENT_USER),
		(4, 104, 'Manage Tasks', 'Task Management', CURRENT_USER),
		(5, 105, 'Manage Reports', 'Report Management', CURRENT_USER);

INSERT INTO proman.PERMISSIONS (ID, UUID, NAME, DESCRIPTION, GROUP_ID, CREATED_BY)
		VALUES 
		(1, 101, 'Add User', 'Add new user', 1, CURRENT_USER),
		(2, 102, 'Modify User', 'Modify an existing user', 1, CURRENT_USER),
		(3, 103, 'Remove User', 'Remove an existing user', 1, CURRENT_USER),
		(4, 104, 'Add Board', 'Add new board', 2, CURRENT_USER),
		(5, 105, 'Modify Board', 'Modify an existing board', 2, CURRENT_USER),
		(6, 106, 'Remove Board', 'Remove an existing board', 2, CURRENT_USER),
		(7, 107, 'Add Project to Board', 'Add an existing project to the board', 2, CURRENT_USER),
		(8, 108, 'Remove Project from Board', 'Remove an existing project from the board', 2, CURRENT_USER),
		(9, 109, 'Add Project', 'Add new project', 3, CURRENT_USER),
        (10, 110, 'Modify Project', 'Modify an existing Project', 3, CURRENT_USER),
        (11, 111, 'Remove Project', 'Remove an existing Project', 3, CURRENT_USER),
        (12, 112, 'Add Member(s) to Project', 'Add member(s) to the Project', 3, CURRENT_USER),
        (13, 113, 'Remove Member(s) from Project', 'Remove existing member(s) from the Project', 3, CURRENT_USER),
		(14, 114, 'Add Task', 'Add new task in the Project', 4, CURRENT_USER),
		(15, 115, 'Modify Task', 'Modify an existing task in the Project', 4, CURRENT_USER),
		(16, 116, 'Remove Task', 'Remove an existing Task', 4, CURRENT_USER),
		(17, 117, 'View Reports', 'View various reports', 5, CURRENT_USER);

INSERT INTO proman.ROLES (ID, UUID, NAME, DESCRIPTION, ACTIVE, CREATED_BY)
		VALUES 
		(1, 101, 'Administrator', 'Administrator', true, CURRENT_USER),
		(2, 102, 'Project Manager', 'Project Manager', true, CURRENT_USER),
		(3, 103, 'Architect', 'Technical Architect', true, CURRENT_USER),
		(4, 104, 'Team Leader', 'Team Leader', true, CURRENT_USER),
		(5, 105, 'Developer', 'Developer', true, CURRENT_USER),
		(6, 106, 'Tester', 'Tester', true, CURRENT_USER);

INSERT INTO proman.ROLE_PERMISSIONS (ROLE_ID, PERMISSION_ID, CREATED_BY)
		VALUES 
		(1,1, CURRENT_USER), (1,2, CURRENT_USER), (1,3, CURRENT_USER), (1,4, CURRENT_USER), (1,5, CURRENT_USER), (1,6, CURRENT_USER), (1,7, CURRENT_USER),
		(1,8, CURRENT_USER), (1,9, CURRENT_USER), (1,10, CURRENT_USER), (1,11, CURRENT_USER), (1,12, CURRENT_USER), (1,13, CURRENT_USER), (1,14, CURRENT_USER),
		(1,15, CURRENT_USER), (1,16, CURRENT_USER), (1,17, CURRENT_USER),
		(2,4, CURRENT_USER), (2,5, CURRENT_USER), (2,6, CURRENT_USER), (2,7, CURRENT_USER),(2,8, CURRENT_USER),
        (2,9, CURRENT_USER), (2,10, CURRENT_USER), (2,11, CURRENT_USER), (2,12, CURRENT_USER), (2,13, CURRENT_USER), (2,14, CURRENT_USER),
        (2,15, CURRENT_USER), (2,16, CURRENT_USER), (2,17, CURRENT_USER),
        (3,9, CURRENT_USER), (3,10, CURRENT_USER), (3,11, CURRENT_USER), (3,12, CURRENT_USER), (3,13, CURRENT_USER), (3,14, CURRENT_USER),
        (3,15, CURRENT_USER),(3,16, CURRENT_USER),
        (4,12, CURRENT_USER), (4,13, CURRENT_USER), (4,14, CURRENT_USER), (4,15, CURRENT_USER), (4,16, CURRENT_USER), (4,17, CURRENT_USER),
        (5,15, CURRENT_USER), (5,16, CURRENT_USER),
        (6,15, CURRENT_USER), (6,16, CURRENT_USER);

-- ********** End of Roles and Permissions setup **********

-- ********** Begin of Users setup **********

INSERT INTO proman.USERS (ID, ROLE_ID, UUID, EMAIL, PASSWORD, SALT, FIRST_NAME, LAST_NAME,
					MOBILE_PHONE, STATUS, FAILED_LOGIN_COUNT, LAST_LOGIN_AT, LAST_PASSWORD_CHANGE_AT, CREATED_BY)
		VALUES 
		(nextval('proman.users_id_seq'), 1, '7d174a25-ba31-45a8-85b4-b06ffc9d5f8f', 'admin@proman.io', 'DF8582C633D66A48', 'sHrhX9BtMOnUpSKFHz8mltet4gXGfGR33JdvH0fst5I=', 'Proman', 'Administrator',
		'(111) 111-1111', 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_USER);

		
-- ********** End of Users setup **********	