INSERT INTO courses (id, name) VALUES
  (-1,'first course');

INSERT INTO courses (id, name) VALUES
  (-2, 'second course');

INSERT INTO groups (id, course_id, name) VALUES
  (-1, -1, 'IU6-41');

INSERT INTO groups (id, course_id, name) VALUES
  (-2, -1, 'IU6-43');

INSERT INTO groups (id, course_id, name) VALUES
  (-3, -2, 'IU7-43');

INSERT INTO subjects (id, course_id, name) VALUES
  (-1, -1, 'Math');

INSERT INTO subjects (id, course_id, name) VALUES
  (-2,-1,'English');

INSERT INTO subjects (id, course_id, name) VALUES
  (-3, -1, 'Physics');

INSERT INTO users (id, role, email, password, first_name, last_name, avatar, about) VALUES
  (0, 0, 'kek@com', 'password', 'name', 'surname', NULL ,'about'),
  (-1, 1, '1@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-2, 1, '2@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-3, 1, '3@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-4, 1, '4@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-5, 1, '5@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-6, 1, '6@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-7, 1, '7@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-8, 1, '8@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-9, 1, '9@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-10, 1, '10@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-11, 1, '11@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-12, 1, '12@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-13, 1, '13@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-14, 1, '14@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-15, 1, '15@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-16, 1, '16@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-17, 1, '17@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-18, 1, '18@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-19, 1, '19@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-20, 1, '20@mail.com', 'password', 'name', 'surname', NULL ,'about');

INSERT INTO applications (student_id, group_id) VALUES
  (-1,-1),
  (-2,-1),
  (-3,-1),
  (-4,-1),
  (-5,-1),
  (-6,-1),
  (-7,-1),
  (-8,-1),
  (-9,-1),
  (-10,-1),
  (-11,-2),
  (-12,-2),
  (-13,-2),
  (-14,-2),
  (-15,-2),
  (-16,-2),
  (-17,-2),
  (-18,-2),
  (-19,-2),
  (-20,-2) ;


INSERT INTO users (id, role, email, password, first_name, last_name, avatar, about) VALUES
  (-21, 2, '21@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-22, 2, '22@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-23, 2, '23@mail.com', 'password', 'name', 'surname', NULL ,'about'),
  (-24, 2, '24@mail.com', 'password', 'name', 'surname', NULL ,'about');

INSERT INTO professors (prof_id, subject_id) VALUES
  (-21, -1),
  (-22, -1),
  (-23, -2),
  (-24, -3);

INSERT INTO classes (id, topic, subject_id, group_id, prof_id, begin_time, end_time, location) VALUES
  (-1, '1m', -1, -1,-21, '2017-05-09 16:30:00', '2017-05-09 18:00:00', '2313'),
  (-2, '2m', -1, -2, -22,'2017-05-09 18:00:00', '2017-05-09 19:30:00', '2313'),
  (-3, '3m', -1, -1, -21,'2017-05-10 16:30:00', '2017-05-10 18:00:00', '2313'),
  (-4, '4m', -1, -2, -21,'2017-05-11 16:30:00', '2017-05-11 18:00:00', '2313'),
  (-5, '1e', -2, -1, -23,'2017-05-12 16:30:00', '2017-05-12 18:00:00', '2313'),
  (-6, '2e', -2, -2, -23,'2017-05-12 18:00:00', '2017-05-12 19:30:00', '2313'),
  (-7, '3e', -2, -1, -23,'2017-05-15 16:30:00', '2017-05-15 18:00:00', '2313'),
  (-8, '4e', -2, -2, -23,'2017-05-16 16:30:00', '2017-05-16 18:00:00', '2313'),
  (-9, '1p', -3, -1, -24,'2017-05-14 16:30:00', '2017-05-14 18:00:00', '2313'),
  (-10, '2p', -3, -2, -24,'2017-05-17 16:30:00', '2017-05-17 18:00:00', '2313'),
  (-11, '3p', -3, -1, -24,'2017-05-18 16:30:00', '2017-05-18 18:00:00', '2313'),
  (-12, '4p', -3, -2, -24,'2017-05-18 18:00:00', '2017-05-18 19:30:00', '2313');

INSERT INTO attendances (class_id, student_id, mark, comment) VALUES
  (-1, -1, 5, NULL ),
  (-1, -2, 4, NULL ),
  (-1, -6, 2, NULL ),
  (-1, -8, 3, NULL ),
  (-1, -10, 5, NULL ),
  (-2, -11, 5, NULL ),
  (-2, -13, 3, NULL ),
  (-2, -15, 5, NULL ),
  (-2, -17, 2, NULL ),
  (-2, -20, 4, NULL ),
  (-5, -1, 5, NULL ),
  (-5, -3, 2, NULL ),
  (-5, -4, 3, NULL ),
  (-5, -6, 4, NULL ),
  (-5, -7, 5, NULL ),
  (-5, -8, 4, NULL ),
  (-5, -10, 3, NULL ),
  (-6, -11, 5, NULL ),
  (-6, -12, 3, NULL ),
  (-6, -15, 5, NULL ),
  (-6, -17, 4, NULL ),
  (-6, -18, 4, NULL ),
  (-6, -20, 2, NULL );

INSERT INTO requests (id, student_id, course_id) VALUES (-1, -1, -2);

INSERT INTO users (id, role, email, password, first_name, last_name, avatar, about) VALUES
  (-25, 0, 'kek@mail.ru', 'password', 'name', 'surname', NULL ,'about');