package api.controllers;

import api.models.Mark;
import api.models.User;
import api.repositories.MarkRepository;
import api.repositories.UserRepository;
import api.utils.ErrorCodes;
import api.utils.error.EntityNotFoundException;
import api.utils.error.PermissionDeniedException;
import api.utils.info.IdInfo;
import api.utils.info.SelectParametersInfo;
import api.utils.response.Response;
import api.utils.response.SelectBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static api.controllers.SessionController.USER_ID;

/**
 * Created by Vileven on 27.05.17.
 */
@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/mark")
public class MarkController {

    private final JdbcTemplate template;
    private final UserRepository userRepository;
    private final MarkRepository markRepository;

    @Autowired
    public MarkController(JdbcTemplate template, UserRepository userRepository, MarkRepository markRepository) {
        this.template = template;
        this.userRepository = userRepository;
        this.markRepository = markRepository;
    }

    private boolean checkSession(Long subjectId, HttpSession session) {
        final Object id = session.getAttribute(USER_ID);
        if (id != null) {
            try {
                final User user = userRepository.find((Long) id);
                if (user != null) {
                    if (user.getRole() == 0) {
                        return true;
                    }

                    template.queryForObject("SELECT prof_id FROM professors " +
                            "WHERE prof_id = ? AND subject_id = ?",Integer.class, user.getId(), subjectId);
                    return true;
                }
            } catch (DataAccessException e) {
                return false;
            }
        }

        return false;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMarkById(@PathVariable Long id) {
        return ResponseEntity.ok(markRepository.find(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMark(@RequestBody Mark markInfo, HttpSession session) {
        if (!checkSession(markInfo.getSubjectId(), session)) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, "permission denied");
        }
        try {
            return ResponseEntity.ok(markRepository.create(markInfo));
        } catch (DataIntegrityViolationException e) {
            return Response.badRequest(ErrorCodes.BAD_VALIDATOR, "wrong subject id");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateMark(@RequestBody Mark markInfo, HttpSession session) {
        final Mark mark = markRepository.find(markInfo.getId());
        if (mark == null) {
            return Response.notFound(ErrorCodes.MARK_NOT_FOUND, "mark not found");
        }
        if (!checkSession(mark.getSubjectId(), session)) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, "permission denied");
        }
        try {
            return ResponseEntity.ok(markRepository.update(markInfo));
        } catch (DataIntegrityViolationException e) {
            return Response.badRequest(ErrorCodes.BAD_VALIDATOR, "wrong subject id");
        } catch (EntityNotFoundException e) {
            return Response.notFound(ErrorCodes.MARK_NOT_FOUND, e.message);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Mark markInfo, HttpSession session) {
        final Mark mark = markRepository.find(markInfo.getId());
        if (mark == null) {
            return Response.notFound(ErrorCodes.MARK_NOT_FOUND, "mark not found");
        }
        if (!checkSession(mark.getSubjectId(), session)) {
            return Response.badRequest(ErrorCodes.PERMISSION_DENIED, "permission denied");
        }
        markRepository.delete(markInfo.getId());
        return ResponseEntity.ok("{\"msg\":\"success\"}");
    }

    @PostMapping("/select")
    public ResponseEntity<?> selectSubjects(@RequestBody SelectParametersInfo info, HttpSession session) {
        return ResponseEntity.ok(new SelectBody(markRepository.selectWithParams(info.getLimit(), info.getOffset(),
                info.getOrders(), info.getFilters()), markRepository.getCount(info.getFilters())));
    }

}
