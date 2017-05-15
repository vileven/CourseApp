package api.controllers;

import api.models.Group;
import api.models.Subject;
import api.services.AdminService;
import api.utils.ErrorCodes;
import api.utils.error.EntityNotFoundException;
import api.utils.error.PermissionDeniedException;
import api.utils.info.GroupInfo;
import api.utils.info.SelectParametersInfo;
import api.utils.info.SubjectInfo;
import api.utils.response.Response;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        final Subject subject = adminService.getSubject(id);

        if (subject == null) {
            return Response.badRequest(ErrorCodes.GROUP_NOT_FOUND,"Group not found");
        }

        return ResponseEntity.ok(subject);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSubject(@RequestBody SubjectInfo info, HttpSession session) {
        try {
            final Subject subject = adminService.createSubject(info, session);

            if (subject == null) {
                Response.badRequest(ErrorCodes.BAD_VALIDATOR, "course id not valid");
            }

            return ResponseEntity.ok(subject);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateSubject(@RequestBody SubjectInfo info, HttpSession session) {
        try {
            final Subject subject = adminService.updateSubject(info, session);
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
    public ResponseEntity<?> deleteSubject(@RequestBody SubjectInfo info, HttpSession session) {
        try {
            adminService.deleteSubject(info, session);

            return ResponseEntity.ok("success");
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @PostMapping("/select")
    public ResponseEntity<?> selectSubjects(@RequestBody SelectParametersInfo info, HttpSession session) {
        try {
            return ResponseEntity.ok(adminService.selectSubjectsWithParams(info, session));
        } catch (PermissionDeniedException e) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }
}
