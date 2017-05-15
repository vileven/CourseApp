CREATE TABLE IF NOT EXISTS requests (
  student_id BIGINT REFERENCES users (id),
  group_id   BIGINT REFERENCES groups (id)
);

CREATE INDEX idx_requests_on_student_group
  ON requests (student_id, group_id);
