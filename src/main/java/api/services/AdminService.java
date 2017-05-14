package api.services;

import api.models.Course;
import api.models.Group;
import api.models.Subject;
import api.repositories.CourseRepository;
import api.repositories.GroupRepository;
import api.repositories.SubjectRepository;
import api.repositories.UserRepository;
import api.utils.info.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Course createCourse(CourseInfo courseData) {
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

    public Course updateCourse(CourseInfo courseData) {
        return courseRepository.update(new Course(courseData.getName()));
    }

    public void deleteCourse(IdInfo idInfo) {
        courseRepository.delete(idInfo.getId());
    }

    public List<Course> selectWithParams(SelectParametersInfo info) {
        return courseRepository.selectWithParams(info.getLimit(), info.getOffset(), info.getOrders(), info.getFilters());
    }
}
