package api.controllers;

import api.models.Course;
import api.services.AdminService;
import api.utils.ErrorCodes;
import api.utils.error.EntityNotFoundException;
import api.utils.error.PermissionDeniedException;
import api.utils.info.CourseInfo;
import api.utils.info.SelectParametersInfo;
import api.utils.response.Response;
import api.utils.response.SelectBody;
import api.utils.response.SubjectAndGroupsResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Course.class),
            @ApiResponse(code = 400, message = "Bad request", response = org.springframework.web.bind.annotation.ResponseBody.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        final Course course = adminService.getCourse(id);

        if (course == null) {
            return Response.badRequest(ErrorCodes.COURSE_NOT_FOUND, "Course not found");
        }

        return Response.okWithCourse(course);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Course.class),
            @ApiResponse(code = 400, message = "Bad request", response = org.springframework.web.bind.annotation.ResponseBody.class)
    })
    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody CourseInfo courseData, HttpSession session) {
        try {
            final Course course = adminService.createCourse(courseData, session);

            return Response.okWithCourse(course);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Course.class),
            @ApiResponse(code = 400, message = "Bad request", response = org.springframework.web.bind.annotation.ResponseBody.class)
    })
    @PostMapping("/update")
    public ResponseEntity<?> updateCourse(@RequestBody CourseInfo courseData, HttpSession session) {
        try {
            final Course course = adminService.updateCourse(courseData, session);

            return Response.okWithCourse(course);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        } catch (EntityNotFoundException e) {

            return Response.notFound(ErrorCodes.COURSE_NOT_FOUND, e.message);
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

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SelectBody.class),
            @ApiResponse(code = 400, message = "Bad request", response = org.springframework.web.bind.annotation.ResponseBody.class)
    })
    @PostMapping("/select")
    public ResponseEntity<?> selectCourses(@RequestBody SelectParametersInfo info, HttpSession session) {
        try {
            return ResponseEntity.ok(new SelectBody(adminService.selectCourseWithParams(info, session),
                    adminService.getCount("courses", info.getFilters())));
        } catch (PermissionDeniedException e) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SubjectAndGroupsResponse.class),
    })
    @GetMapping("/{id}/subjectsAndGroups")
    public ResponseEntity<?> getSubjectsAndGroup(@PathVariable Long id) {
        return ResponseEntity.ok(new SubjectAndGroupsResponse(adminService.getCourseGroups(id),
                adminService.getCourseSubjects(id)));
    }
}
