CREATE TABLE IF NOT EXISTS courses (
  id   BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS subjects (
  id        BIGSERIAL PRIMARY KEY,
  course_id BIGINT REFERENCES courses (id),
  name      TEXT NOT NULL
);


CREATE INDEX idx_subjects_on_courses
  ON subjects (course_id, name);


CREATE TABLE IF NOT EXISTS professors (
  prof_id    BIGINT REFERENCES users (id),
  subject_id BIGINT REFERENCES subjects (id)
);

CREATE INDEX idx_professors_on_users_subjects
  ON professors (prof_id, subject_id);

CREATE TABLE IF NOT EXISTS groups (
  id        BIGSERIAL PRIMARY KEY,
  course_id BIGINT REFERENCES courses (id),
  name      TEXT NOT NULL
);

CREATE INDEX idx_groups_on_courses
  ON groups (course_id, name);

CREATE TABLE IF NOT EXISTS applications (
  student_id BIGINT REFERENCES users (id),
  group_id   BIGINT REFERENCES groups (id)
);

CREATE INDEX idx_applications_on_users_groups
  ON applications (student_id, group_id);

CREATE TABLE IF NOT EXISTS classes (
  id         BIGSERIAL PRIMARY KEY,
  topic      TEXT        NOT NULL,
  subject_id BIGINT REFERENCES subjects (id),
  group_id   BIGINT REFERENCES groups (id),
  begin_time TIMESTAMPTZ NOT NULL,
  end_time   TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_classes_on_subject_group
  ON classes (subject_id, group_id);

CREATE TABLE IF NOT EXISTS attendances (
  class_id   BIGINT REFERENCES classes (id),
  student_id BIGINT REFERENCES users (id),
  mark       INTEGER NOT NULL,
  comment    TEXT
);

CREATE INDEX idx_attendances_on_class_student
  ON attendances (class_id, student_id, mark, comment);

CREATE TABLE IF NOT EXISTS marks (
  id         BIGSERIAL PRIMARY KEY,
  min        INTEGER NOT NULL,
  max        INTEGER NOT NULL,
  name       TEXT NOT NULL ,
  subject_id BIGINT REFERENCES subjects (id)
);

CREATE INDEX idx_marks_on_subject
  ON marks (subject_id, min, max, name);

CREATE TABLE IF NOT EXISTS subject_stats (
  subject_id BIGINT REFERENCES subjects (id),
  student_id BIGINT REFERENCES users (id),
  total      INTEGER DEFAULT 0,
  mark_id    BIGINT REFERENCES marks (id)
);

CREATE INDEX idx_sub_stats_on_subject_student
  ON subject_stats(subject_id, student_id, total,mark_id);
