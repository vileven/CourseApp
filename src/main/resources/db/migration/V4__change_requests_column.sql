DROP INDEX idx_requests_on_student_group;

ALTER TABLE requests DROP COLUMN group_id, ADD COLUMN course_id BIGINT REFERENCES courses(id);

CREATE INDEX idx_requests_on_student_course
  ON requests(student_id, course_id);
