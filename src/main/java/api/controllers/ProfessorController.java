package api.controllers;

import api.models.ClassModel;
import api.services.ProfessorService;
import api.utils.ErrorCodes;
import api.utils.error.PermissionDeniedException;
import api.utils.info.AttendancesInfo;
import api.utils.response.GroupAndSubjectResponse;
import api.utils.response.Response;
import api.utils.response.UserClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Vileven on 16.05.17.
 */
@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/professor")
public class ProfessorController {
    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping("/{id}/subjectsGroups")
    public List<GroupAndSubjectResponse> getProfSubjectGroups(@PathVariable Long id) {
        return professorService.getGroupsAndSubjects(id);
    }

    @GetMapping("/{id}/classes")
    public List<ClassModel> getProfClasses(@PathVariable Long id, @RequestParam("from") String from,
                                           @RequestParam("to") String to) {
        return professorService.getProfClasses(id, from, to);
    }

    @PostMapping("/createAttendencies")
    public ResponseEntity<?> addAttendencies(@RequestBody AttendancesInfo info, HttpSession session) {
        try {
            professorService.addAttendencies(info, session);
            return ResponseEntity.ok("success");
        } catch (PermissionDeniedException e) {

            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, e.message);
        } catch (DataIntegrityViolationException e) {

            return Response.badRequest(ErrorCodes.BAD_VALIDATOR, "not valid");
        }
    }
}
