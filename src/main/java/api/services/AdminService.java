package api.services;

import api.models.*;
import api.repositories.*;
import api.utils.error.EntityNotFoundException;
import api.utils.error.PermissionDeniedException;
import api.utils.info.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

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

        return groupRepository.create(new Group(groupData.getCourseId(), groupData.getName()));
    }

    @Nullable
    public Subject createSubject(SubjectInfo subjectData, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return subjectRepository.create(new Subject(subjectData.getCourseId(), subjectData.getName()));
    }

    @Nullable
    public ClassModel createClass(ClassInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return classRepository.create(new ClassModel(info.getTopic(), info.getSubject(), info.getGroup(), info.getBegin(),
                info.getEnd()));
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
    public Subject getSubject(long id) {
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

        return groupRepository.update(new Group(info.getId(), info.getCourseId(), info.getName()));
    }

    @Nullable
    public Subject updateSubject(SubjectInfo info, HttpSession session) throws PermissionDeniedException, EntityNotFoundException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return subjectRepository.update(new Subject(info.getId(), info.getCourseId(), info.getName()));
    }

    public ClassModel updateClass(ClassInfo info, HttpSession session) throws PermissionDeniedException, EntityNotFoundException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return classRepository.update(new ClassModel(info.getId(), info.getTopic(), info.getSubject(), info.getGroup(),
                info.getBegin(), info.getEnd()));
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

    public void deleteSubject(SubjectInfo info, HttpSession session) throws PermissionDeniedException {
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

    public List<Subject> selectSubjectsWithParams(SelectParametersInfo info, HttpSession session)throws PermissionDeniedException {
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

    public Integer getCount(String tableName) {

        return template.queryForObject("SELECT count(*) FROM " + tableName, Integer.class);
    }
}
