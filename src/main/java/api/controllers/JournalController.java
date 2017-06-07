package api.controllers;

import api.repositories.GroupRepository;
import api.repositories.SubjectRepository;
import api.services.JournalService;
import api.utils.ErrorCodes;
import api.utils.info.AttendancesInfo;
import api.utils.info.JournalInfo;
import api.utils.response.Response;
import api.utils.response.journal.JournalFinalResponse;
import api.utils.response.journal.JournalResponseRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Vileven on 22.05.17.
 */
@CrossOrigin(origins = {"http://localhost:3000", "*", "http://127.0.0.1:3000"})
@RestController
@RequestMapping(path = "/journal")
public class JournalController {
    private final JournalService journalService;
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public JournalController(JournalService journalService, GroupRepository groupRepository, SubjectRepository subjectRepository) {
        this.journalService = journalService;
        this.groupRepository = groupRepository;
        this.subjectRepository = subjectRepository;
    }

    @PostMapping("/show")
    public ResponseEntity<?> showJournal(@RequestBody JournalInfo journalInfo) {
        final List<JournalResponseRow> res = journalService.getGroupJournal(journalInfo.groupId, journalInfo.subjectId);
        try {
            return ResponseEntity.ok(new JournalFinalResponse(groupRepository.find(journalInfo.groupId).getName(),
                    subjectRepository.find(journalInfo.subjectId).getName(), res));
        } catch (NullPointerException e) {
            return Response.badRequest(ErrorCodes.GROUP_NOT_FOUND, "group or subject not found");
        }

    }

    @PostMapping("/addAttendance")
    public ResponseEntity<?> addAttendance(@RequestBody AttendancesInfo info) {
        journalService.addAttendance(info.classId, info.studentId, info.attendance, info.mark, info.comment);
        return ResponseEntity.ok("{\"msg\":\"success\"}");
    }
}
