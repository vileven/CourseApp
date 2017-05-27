DROP INDEX idx_attendances_on_class_student;

CREATE UNIQUE INDEX idx_attendances_on_class_student
  ON attendances (class_id, student_id);