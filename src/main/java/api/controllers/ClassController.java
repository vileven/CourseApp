package api.controllers;

import api.models.ClassModel;
import api.services.AdminService;
import api.utils.ErrorCodes;
import api.utils.error.EntityNotFoundException;
import api.utils.error.PermissionDeniedException;
import api.utils.info.ClassInfo;
import api.utils.info.SelectParametersInfo;
import api.utils.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by Vileven on 15.05.17.
 */
@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/class")
public class ClassController {

    private final AdminService adminService;

    @Autowired
    public ClassController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        final ClassModel classModel = adminService.getClassModel(id);

        if (classModel == null) {
            return Response.badRequest(ErrorCodes.GROUP_NOT_FOUND,"Group not found");
        }

        return ResponseEntity.ok(classModel);
    }



    @PostMapping("/create")
    public ResponseEntity<?> createClass(@RequestBody ClassInfo info, HttpSession session) {
        try {
            final ClassModel classModel = adminService.createClass(info, session);

            if (classModel == null) {
                Response.badRequest(ErrorCodes.BAD_VALIDATOR, "subject_id or group_id not valid");
            }

            return ResponseEntity.ok(classModel);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateClass(@RequestBody ClassInfo info, HttpSession session) {
        try {
            final ClassModel classModel = adminService.updateClass(info, session);
            return ResponseEntity.ok(classModel);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        } catch (EntityNotFoundException e) {

            return Response.notFound(ErrorCodes.SUBJECT_NOT_FOUND, e.message);
        } catch (DataIntegrityViolationException e) {

            return Response.badRequest(ErrorCodes.BAD_VALIDATOR, "subject_id or group_id not valid");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteClass(@RequestBody ClassInfo info, HttpSession session) {
        try {
            adminService.deleteClass(info, session);

            return ResponseEntity.ok("success");
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @PostMapping("/select")
    public ResponseEntity<?> selectClasses(@RequestBody SelectParametersInfo info, HttpSession session) {
        try {
            return ResponseEntity.ok(adminService.selectClassesWithParams(info, session));
        } catch (PermissionDeniedException e) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

}
