package api.controllers;

import api.models.Course;
import api.services.AdminService;
import api.utils.ErrorCodes;
import api.utils.error.PermissionDeniedException;
import api.utils.info.CourseInfo;
import api.utils.info.IdInfo;
import api.utils.info.SelectParametersInfo;
import api.utils.response.CourseBody;
import api.utils.response.Response;
import api.utils.response.generic.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/course")
public class CourseController {
    private final AdminService adminService;
    private final ApplicationContext applicationContext;

    @Autowired
    public CourseController(AdminService adminService, ApplicationContext applicationContext) {
        this.adminService = adminService;
        this.applicationContext = applicationContext;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        final Course course = adminService.getCourse(id);

        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        return Response.okWithCourse(course);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody CourseInfo courseData, HttpSession session) {
        try {
            final Course course = adminService.createCourse(courseData, session);

            return Response.okWithCourse(course);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCourse(@RequestBody CourseInfo courseData, HttpSession session) {
        try {
            final Course course = adminService.updateCourse(courseData, session);

            return Response.okWithCourse(course);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }

    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCourse(@RequestBody CourseInfo courseData, HttpSession session) {
        try {
            adminService.deleteCourse(courseData, session);

            return ResponseEntity.ok("success");
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }

    }

    @PostMapping("/select")
    public ResponseEntity<?> selectCourses(@RequestBody SelectParametersInfo info, HttpSession session) {
        try {
            return ResponseEntity.ok(adminService.selectWithParams(info, session));
        } catch (PermissionDeniedException e) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }
}
