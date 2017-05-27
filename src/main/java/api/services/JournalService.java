package api.services;

import api.utils.response.journal.JournalClass;
import api.utils.response.journal.JournalResponseRow;
import api.utils.response.journal.Mark;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JournalService {
    private final JdbcTemplate template;

    @Autowired
    public JournalService(JdbcTemplate template) {
        this.template = template;
    }

    private RowMapper<MarkModel> markMapper = (rs, rowNum) -> new MarkModel(rs.getLong("student_id"),
            rs.getLong("class_id"), rs.getInt("mark"), rs.getString("comment"));

    public List<JournalResponseRow> getGroupJournal(long groupId, long subjectId) {

        final String query =
                "SELECT " +
                "  u.id as student_id, " +
                "  u.first_name as student_first_name, " +
                "  u.last_name as student_last_name, " +
                "  cl.id as class_id, " +
                "  cl.begin_time as date, " +
                "  att.mark, " +
                "  att.comment " +
                "FROM " +
                "  groups g " +
                "  JOIN applications app ON g.id = app.group_id " +
                "  JOIN users u ON app.student_id = u.id " +
                "  JOIN classes cl ON g.id = cl.group_id " +
                "  LEFT JOIN attendances att ON cl.id = att.class_id AND u.id = att.student_id " +
                "WHERE g.id = ? AND cl.subject_id = ? " +
                "ORDER BY u.last_name, u.first_name, u.id, cl.begin_time ";

        final long[] currentStudent = {0};
        final List<JournalResponseRow> result = new ArrayList<>();
        template.query(query, rs -> {
            final long id =  rs.getLong("student_id");
            if (id != currentStudent[0]) {
                currentStudent[0] = id;
                result.add(new JournalResponseRow(id, rs.getString("student_first_name"),
                        rs.getString("student_last_name"), new ArrayList<>()));
            }

            result.get(result.size() - 1).getClasses().add(new JournalClass(rs.getLong("class_id"),
                    rs.getString("date")));

            final int markValue = rs.getInt("mark");
            final Mark mark;
            if (rs.wasNull()) {
                 mark= null;
            } else  {
                mark = new Mark(markValue, rs.getString("comment"));
            }

            final int classSize = result.get(result.size() - 1).getClasses().size();
            result.get(result.size() - 1).getClasses().get(classSize - 1).setMark(mark);
        }, groupId, subjectId);

        return result;
    }

    public void addAttendance(long classId, long studentId, boolean attendance, Integer mark, @Nullable String comment) {
        if (attendance) {
            template.update("INSERT INTO attendances (class_id, student_id, mark, comment) VALUES " +
                    "(?,?,?,?) " +
                    "ON CONFLICT (class_id, student_id) DO UPDATE " +
                    "SET mark = ?, comment = ? ", classId, studentId, mark, comment, mark, comment);
        } else {

            try {
                final MarkModel markModel = template.queryForObject("SELECT * FROM attendances " +
                        "WHERE class_id = ? AND student_id = ?", markMapper, classId, studentId);

            } catch (EmptyResultDataAccessException e) {
                return;
            }


            template.update("DELETE FROM attendances WHERE class_id = ? AND student_id = ?", classId, studentId);
        }

    }

    public static class MarkModel {
        public final Long studentId;
        public final Long classId;
        public final Integer mark;
        public final String comment;

        public MarkModel(Long studentId, Long classId, Integer mark, String comment) {
            this.studentId = studentId;
            this.classId = classId;
            this.mark = mark;
            this.comment = comment;
        }
    }

}
