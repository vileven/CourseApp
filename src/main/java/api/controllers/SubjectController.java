package api.controllers;

import api.models.Group;
import api.models.Subject;
import api.services.AdminService;
import api.utils.ErrorCodes;
import api.utils.error.EntityNotFoundException;
import api.utils.error.PermissionDeniedException;
import api.utils.info.*;
import api.utils.response.Response;
import api.utils.response.SelectBody;
import api.utils.response.SubjectResponse;
import api.utils.response.generic.*;
import api.utils.response.generic.ResponseBody;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by Vileven on 14.05.17.
 */
@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/subject")
public class SubjectController {

    private final AdminService adminService;

    @Autowired
    public SubjectController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SubjectResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseBody.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        final SubjectResponse subject = adminService.getSubject(id);

        if (subject == null) {
            return Response.badRequest(ErrorCodes.SUBJECT_NOT_FOUND,"Subject not found");
        }

        return ResponseEntity.ok(subject);
    }

    @GetMapping("/{id}/professors")
    public ResponseEntity<?> getProfessors(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getSubjectProfs(id));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SubjectResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseBody.class)
    })
    @PostMapping("/create")
    public ResponseEntity<?> createSubject(@RequestBody SubjectInfo info, HttpSession session) {
        try {
            final SubjectResponse subject = adminService.createSubject(info, session);

            if (subject == null) {
                return Response.badRequest(ErrorCodes.BAD_VALIDATOR, "course id not valid");
            }

            return ResponseEntity.ok(subject);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SubjectResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseBody.class)
    })
    @PostMapping("/update")
    public ResponseEntity<?> updateSubject(@RequestBody SubjectInfo info, HttpSession session) {
        try {
            final SubjectResponse subject = adminService.updateSubject(info, session);
            return ResponseEntity.ok(subject);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        } catch (EntityNotFoundException e) {

            return Response.notFound(ErrorCodes.SUBJECT_NOT_FOUND, e.message);
        } catch (DataIntegrityViolationException e) {

            return Response.badRequest(ErrorCodes.BAD_VALIDATOR, "wrong course id");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteSubject(@RequestBody IdInfo info, HttpSession session) {
        try {
            adminService.deleteSubject(info, session);

            return ResponseEntity.ok("success");
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SelectBody.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseBody.class)
    })
    @PostMapping("/select")
    public ResponseEntity<?> selectSubjects(@RequestBody SelectParametersInfo info, HttpSession session) {
        try {
            return ResponseEntity.ok(new SelectBody(adminService.selectSubjectsWithParams(info, session),
                    adminService.getCount("subjects", info.getFilters())));
        } catch (PermissionDeniedException e) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        } catch (DataIntegrityViolationException e) {
            return Response.badRequest(ErrorCodes.USER_NOT_FOUND, "wrong sql query");
        }
    }


    @PostMapping("/{id}/setProfessors")
    public ResponseEntity<?> setProfessors(@PathVariable Long id,@RequestBody IdsInfo info, HttpSession session) {
        try {
            adminService.setProfessors(id, info.getIds(), session);
            return ResponseEntity.ok("success");
        } catch (PermissionDeniedException e) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }
}
