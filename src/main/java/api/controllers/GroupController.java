package api.controllers;

import api.models.Group;
import api.services.AdminService;
import api.utils.ErrorCodes;
import api.utils.error.EntityNotFoundException;
import api.utils.error.PermissionDeniedException;
import api.utils.info.GroupInfo;
import api.utils.info.SelectParametersInfo;
import api.utils.response.Response;
import api.utils.response.SelectBody;
import api.utils.response.SubjectResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/group")
public class GroupController {
    private final AdminService adminService;

    @Autowired
    public GroupController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Group.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseBody.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        final Group group = adminService.getGroup(id);

        if (group == null) {
            return Response.badRequest(ErrorCodes.GROUP_NOT_FOUND,"Group not found");
        }

        return ResponseEntity.ok(group);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Group.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseBody.class)
    })
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupInfo groupData, HttpSession session) {
        try {
            final Group group = adminService.createGroup(groupData, session);

            if (group == null) {
                Response.badRequest(ErrorCodes.BAD_VALIDATOR, "course id not valid");
            }

            return ResponseEntity.ok(group);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Group.class),
            @ApiResponse(code = 400, message = "Bad request", response = ResponseBody.class)
    })
    @PostMapping("/update")
    public ResponseEntity<?> updateGroup(@RequestBody GroupInfo groupData, HttpSession session) {
        try {
            final Group group = adminService.updateGroup(groupData, session);
            if (group == null) {
                return Response.badRequest(ErrorCodes.BAD_VALIDATOR, "wrong course id");
            }
            return ResponseEntity.ok(group);
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        } catch (EntityNotFoundException e) {

            return Response.notFound(ErrorCodes.GROUP_NOT_FOUND, e.message);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteGroup(@RequestBody GroupInfo info, HttpSession session) {
        try {
            adminService.deleteGroup(info, session);

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
    public ResponseEntity<?> selectGroups(@RequestBody SelectParametersInfo info, HttpSession session) {
        try {
            return ResponseEntity.ok(new SelectBody(adminService.selectGroupWithParams(info, session), adminService.getCount("groups", info.getFilters())));
        } catch (PermissionDeniedException e) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }
}
