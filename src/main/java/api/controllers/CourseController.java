package api.controllers;

import api.models.Course;
import api.services.AdminService;
import api.utils.ErrorCodes;
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
    public ResponseEntity<?> createCourse(@RequestBody CourseInfo courseData) {
        final Course course = adminService.createCourse(courseData);
        return Response.okWithCourse(course);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCourse(@RequestBody CourseInfo courseData) {
        final Course course = adminService.updateCourse(courseData);
        return Response.okWithCourse(course);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCourse(@RequestBody IdInfo idInfo) {
        adminService.deleteCourse(idInfo);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/select")
    public ResponseEntity<?> selectCourses(@RequestBody SelectParametersInfo info) {
        return ResponseEntity.ok(adminService.selectWithParams(info));
    }
}
