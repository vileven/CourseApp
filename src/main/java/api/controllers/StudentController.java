package api.controllers;

import api.models.Course;
import api.models.Group;
import api.services.StudentService;
import api.utils.info.IdInfo;
import api.utils.info.TimeInfo;
import api.utils.info.ValueInfo;
import api.utils.response.Response;
import api.utils.response.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static api.controllers.SessionController.USER_ID;

/**
 * Created by Vileven on 16.05.17.
 */
@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}/groups")
    public List<Group> getStudentGroups(@PathVariable Long id) {
        return studentService.getStudentGroups(id);
    }

    @GetMapping("/{id}/courses")
    public List<Course> getStudentCourses(@PathVariable Long id) {
        return studentService.getStudentCourses(id);
    }

    @GetMapping("/{id}/classes")
    public List<UserClass> getStudentClasses(@PathVariable Long id, @RequestParam("from") String from,
                                             @RequestParam("to") String to) {
        return studentService.getStudentClasses(id, from, to);
    }

    @PostMapping("/createRequest")
    public final ResponseEntity<?> createRequest(@RequestBody IdInfo info, HttpSession session) {
        final Object userId = session.getAttribute(USER_ID);
        if (userId != null) {
            studentService.createRequest((Long)userId, info.getId());
            return ResponseEntity.ok("success");
        }

        return Response.invalidSession();
    }

}
