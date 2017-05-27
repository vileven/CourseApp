CREATE UNIQUE INDEX idx_sub_stats_on_subject_student_ids
  ON subject_stats (subject_id, student_id);

CREATE OR REPLACE FUNCTION update_stats_after_insert()
  RETURNS TRIGGER AS '
BEGIN


  INSERT INTO subject_stats (subject_id, student_id, total, mark_id) VALUES
    ((
       SELECT s.id
       FROM subjects s
         JOIN classes cl ON s.id = cl.subject_id
       WHERE cl.id = NEW.class_id
       GROUP BY s.id
     ), NEW.student_id, NEW.mark, (SELECT id
                                   FROM marks
                                   WHERE NEW.mark > marks.min
                                         AND NEW.mark <= marks.max
                                         AND marks.subject_id = (SELECT s.id
                                                                 FROM subjects s
                                                                   JOIN classes cl ON s.id = cl.subject_id
                                                                 WHERE cl.id = NEW.class_id
                                                                 GROUP BY s.id)
     )
    )
  ON CONFLICT (subject_id, student_id)
    DO UPDATE
      SET
        mark_id = (SELECT id
                   FROM marks
                   WHERE subject_stats.total + NEW.mark > marks.min AND subject_stats.total + NEW.mark <= marks.max
                         AND marks.subject_id = (SELECT s.id
                                                 FROM subjects s
                                                   JOIN classes cl ON s.id = cl.subject_id
                                                 WHERE cl.id = NEW.class_id
                                                 GROUP BY s.id)),
        total = subject_stats.total + NEW.mark;

  RETURN NULL;
END
' LANGUAGE plpgsql;


CREATE TRIGGER add_to_total_stats_after_insert
AFTER INSERT ON attendances
FOR EACH ROW EXECUTE PROCEDURE update_stats_after_insert();


CREATE OR REPLACE FUNCTION update_stats_after_update()
  RETURNS TRIGGER AS '
BEGIN

  UPDATE subject_stats
  SET
    mark_id = (SELECT id
               FROM marks
               WHERE total + NEW.mark - OLD.mark > marks.min
                     AND total + NEW.mark - OLD.mark <= marks.max
                     AND marks.subject_id = (SELECT s.id
                                             FROM subjects s
                                               JOIN classes cl ON s.id = cl.subject_id
                                             WHERE cl.id = NEW.class_id
                                             GROUP BY s.id)),
    total   = total + NEW.mark - OLD.mark
  WHERE student_id = NEW.student_id AND subject_id = (
    SELECT s.id
    FROM subjects s
      JOIN classes cl ON s.id = cl.subject_id
    WHERE cl.id = NEW.class_id
    GROUP BY s.id
  );

  RETURN NULL;
END
' LANGUAGE plpgsql;


CREATE TRIGGER add_to_total_stats_after_update
AFTER UPDATE ON attendances
FOR EACH ROW EXECUTE PROCEDURE update_stats_after_update();


CREATE OR REPLACE FUNCTION update_stats_after_delete()
  RETURNS TRIGGER AS '
BEGIN

  UPDATE subject_stats
  SET
    mark_id = (SELECT id
               FROM marks
               WHERE total - OLD.mark > marks.min AND total - OLD.mark <= marks.max
                     AND marks.subject_id = (SELECT s.id
                                             FROM subjects s
                                               JOIN classes cl ON s.id = cl.subject_id
                                             WHERE cl.id = OLD.class_id
                                             GROUP BY s.id)),
    total   = total - OLD.mark
  WHERE student_id = OLD.student_id AND subject_id = (
    SELECT s.id
    FROM subjects s
      JOIN classes cl ON s.id = cl.subject_id
    WHERE cl.id = OLD.class_id
    GROUP BY s.id
  );

  RETURN NULL;
END
' LANGUAGE plpgsql;


CREATE TRIGGER add_to_total_stats_after_delete
AFTER DELETE ON attendances
FOR EACH ROW EXECUTE PROCEDURE update_stats_after_delete();


