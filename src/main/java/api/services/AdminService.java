package api.services;

import api.models.Course;
import api.models.Group;
import api.models.Subject;
import api.models.User;
import api.repositories.CourseRepository;
import api.repositories.GroupRepository;
import api.repositories.SubjectRepository;
import api.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final JdbcTemplate template;

    @Autowired
    public AdminService(UserRepository userRepository, SubjectRepository subjectRepository,
                        GroupRepository groupRepository, CourseRepository courseRepository, JdbcTemplate template) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.template = template;
    }

    private boolean isAdmin(HttpSession session) {
        final User user = userRepository.find((Long) session.getAttribute(USER_ID));
        return user != null && user.getRole() == 0;

    }

    public Course createCourse(CourseInfo courseData, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return courseRepository.create(new Course(courseData.getName()));

    }

    @Nullable
    public Group createGroup(GroupInfo groupData) {
        return groupRepository.create(new Group(groupData.getCourseId(), groupData.getName()));
    }

    @Nullable
    public Subject createSubject(SubjectInfo subjectData) {
        return subjectRepository.create(new Subject(subjectData.getCourseId(), subjectData.getName()));
    }

    @Nullable
    public Course getCourse(long id) {
        return courseRepository.find(id);
    }

    public Course updateCourse(CourseInfo courseData, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        return courseRepository.update(new Course(courseData.getId(), courseData.getName()));
    }

    public void deleteCourse(CourseInfo courseData, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }

        courseRepository.delete(courseData.getId());
    }

    public List<Course> selectWithParams(SelectParametersInfo info, HttpSession session) throws PermissionDeniedException {
        if (!isAdmin(session)) {
            throw new PermissionDeniedException("permission denied");
        }
        return courseRepository.selectWithParams(info.getLimit(), info.getOffset(), info.getOrders(), info.getFilters());
    }
}
