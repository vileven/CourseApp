package api.controllers;

import api.services.AdminService;
import api.utils.ErrorCodes;
import api.utils.error.PermissionDeniedException;
import api.utils.info.AcceptRequestInfo;
import api.utils.info.IdInfo;
import api.utils.info.LimitOffsetInfo;
import api.utils.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;

/**
 * Created by Vileven on 18.05.17.
 */
@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/request")
public class RequestController {
    private final AdminService adminService;

    @Autowired
    public RequestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptRequest(@RequestBody AcceptRequestInfo info, HttpSession session) {
        try {
            adminService.acceptRequest(info, session);
            return ResponseEntity.ok("{\"msg\":\"success\"}");
        } catch (PermissionDeniedException e) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteRequest(@RequestBody IdInfo idInfo, HttpSession session) {
        try {
            adminService.deleteRequest(idInfo, session);
            return ResponseEntity.ok("{\"msg\":\"success\"}");
        } catch (PermissionDeniedException e) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        }
    }

    @PostMapping("/select")
    public ResponseEntity<?> selectRequests(@RequestBody LimitOffsetInfo info) {
        return ResponseEntity.ok(adminService.selectRequests(info.limit, info.offset));
    }

}
