package api.controllers;

import api.services.AdminService;
import api.utils.ErrorCodes;
import api.utils.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by Vileven on 07.06.17.
 */
@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/info")
    public ResponseEntity<?> getInfo(HttpSession session) {
        if(!adminService.isAdmin(session)) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, "permission denied");
        }

        return ResponseEntity.ok(adminService.getInfo());
    }
}
