package api.services;

import api.models.*;
import api.repositories.*;
import api.utils.error.EntityNotFoundException;
import api.utils.error.PermissionDeniedException;
import api.utils.info.*;
import api.utils.pair.Pair;
import api.utils.response.SubjectResponse;
import api.utils.response.UserClass;
import api.utils.response.UserResponseBody;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static api.controllers.SessionController.USER_ID;

/**
 * Created by Vileven on 14.05.17.
 */

@Service
public class AdminService {
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final JdbcTemplate template;

    @Autowired
    public AdminService(ClassRepository classRepository, UserRepository userRepository, SubjectRepository subjectRepository,
                        GroupRepository groupRepository, CourseRepository courseRepository, JdbcTemplate template) {
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.template = template;
    }

    private boolean isAdmin(HttpSession session) {
        Object id = session.getAttribute(USER_ID);
        User user = null;
        if (id != null) {
            user = userRepository.find((Long) id);
        }
        return user != null && user.getRole() == 0;
    }

    public Course createCourse(CourseInfo courseData, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return courseRepository.create(new Course(courseData.getName()));

    }

    @Nullable
    public Group createGroup(GroupInfo groupData, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return groupRepository.create(new Group(groupData.getCourseId(), groupData.getCourseName(), groupData.getName()));
    }

    @Nullable
    public SubjectResponse createSubject(SubjectInfo subjectData, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return subjectRepository.create(new SubjectResponse(subjectData.getCourseId(),null, subjectData.getName()));
    }

    @Nullable
    public ClassModel createClass(ClassInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return classRepository.create(new ClassModel(info.getTopic(), info.getSubject(), info.getGroup(), info.getProf(), info.getBegin(),
                info.getEnd(), info.getLocation()));
    }

    @Transactional
    public void createBatchClass(ClassCreationInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }


        info.getGroups().forEach(group -> IntStream.range(0, info.getAmount())
                .forEach(el -> {
                    final String beginTime = LocalDateTime.parse(info.getBegin())
                            .plusWeeks(el).toString();
                    final String endTime = LocalDateTime.parse(info.getEnd())
                            .plusWeeks(el).toString();

                    final ClassModel classModel = classRepository.create(new ClassModel(info.getTopic(), info.getSubject(), group,
                            info.getProf(), beginTime, endTime, info.getLocation()));
                    System.out.println(classModel.getLocation() + "   " + classModel.getId());
                })
        );


    }
    @Nullable
    public Course getCourse(long id) {
        return courseRepository.find(id);
    }

    @Nullable
    public Group getGroup(long id) {
        return groupRepository.find(id);
    }

    @Nullable
    public SubjectResponse getSubject(long id) {
        return subjectRepository.find(id);
    }

    @Nullable
    public ClassModel getClassModel(long id) {
        return classRepository.find(id);
    }

    public Course updateCourse(CourseInfo courseData, HttpSession session) throws PermissionDeniedException, EntityNotFoundException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return courseRepository.update(new Course(courseData.getId(), courseData.getName()));
    }

    @Nullable
    public Group updateGroup(GroupInfo info, HttpSession session) throws PermissionDeniedException, EntityNotFoundException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return groupRepository.update(new Group(info.getId(), info.getCourseId(), info.getCourseName(), info.getName()));
    }

    @Nullable
    public SubjectResponse updateSubject(SubjectInfo info, HttpSession session) throws PermissionDeniedException, EntityNotFoundException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return subjectRepository.update(new SubjectResponse(info.getId(), info.getCourseId(), info.getCourseName(), info.getName()));
    }

    public ClassModel updateClass(ClassInfo info, HttpSession session) throws PermissionDeniedException, EntityNotFoundException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return classRepository.update(new ClassModel(info.getId(), info.getTopic(), info.getSubject(), info.getGroup(),
                info.getProf(), info.getBegin(), info.getEnd(), info.getLocation()));
    }

    public void deleteCourse(CourseInfo courseData, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        courseRepository.delete(courseData.getId());
    }

    public void deleteGroup(GroupInfo info, HttpSession session)throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        groupRepository.delete(info.getId());
    }

    public void deleteSubject(IdInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        subjectRepository.delete(info.getId());
    }

    public void deleteClass(ClassInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        classRepository.delete(info.getId());
    }

    public List<Course> selectCourseWithParams(SelectParametersInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }
        return courseRepository.selectWithParams(info.getLimit(), info.getOffset(), info.getOrders(), info.getFilters());
    }

    public List<Group> selectGroupWithParams(SelectParametersInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return groupRepository.selectWithParams(info.getLimit(), info.getOffset(), info.getOrders(), info.getFilters());
    }

    public List<SubjectResponse> selectSubjectsWithParams(SelectParametersInfo info, HttpSession session)throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return subjectRepository.selectWithParams(info.getLimit(), info.getOffset(), info.getOrders(), info.getFilters());
    }

    public List<ClassModel> selectClassesWithParams(SelectParametersInfo info, HttpSession session)throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return classRepository.selectWithParams(info.getLimit(), info.getOffset(), info.getOrders(), info.getFilters());
    }

    public Integer getCount(String tableName, List<Pair<String, String>> filters) {
        final StringBuilder sqlBuilder = new StringBuilder().append("SELECT count(*) FROM ").append(tableName);

        if (filters != null && !filters.isEmpty()) {
            sqlBuilder.append(" WHERE  ");
            for (int i = 0; i < filters.size(); i++) {
                sqlBuilder
                        .append(filters.get(i).getKey())
                        .append(" ~* '")
                        .append(filters.get(i).getValue())
                        .append('\'')
                ;
                if (i != filters.size() - 1) {
                    sqlBuilder.append(" AND ");
                }
            }
        }

        return template.queryForObject(sqlBuilder.toString(), Integer.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public void acceptRequest(AcceptRequestInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        template.update("INSERT INTO applications (student_id, group_id)  VALUES " +
                "  ((SELECT student_id FROM requests WHERE id = ?), ?) ",  info.id, info.groupId);

        template.update(" DELETE FROM requests WHERE id = ? ", info.id);

    }

    public void deleteRequest(IdInfo idInfo, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        template.update("DELETE FROM requests WHERE id = ? ", idInfo.getId());
    }

    public List<Group> getCourseGroups(long id) {
        return courseRepository.getGroups(id);
    }

    public List<SubjectResponse> getCourseSubjects(long id) {
        return courseRepository.getSubjects(id);
    }

    public List<UserResponseBody> getSubjectProfs(long id) {
        return subjectRepository.getProfessors(id);
    }
}
