package api.controllers;

import api.services.JournalService;
import api.utils.info.AttendancesInfo;
import api.utils.info.JournalInfo;
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

    @Autowired
    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @PostMapping("/show")
    public ResponseEntity<?> showJournal(@RequestBody JournalInfo journalInfo) {
        final List<JournalResponseRow> res = journalService.getGroupJournal(journalInfo.groupId, journalInfo.subjectId);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/addAttendance")
    public ResponseEntity<?> addAttendance(@RequestBody AttendancesInfo info) {
        journalService.addAttendance(info.classId, info.studentId, info.attendance, info.mark, info.comment);
        return ResponseEntity.ok("{\"msg\":\"success\"}");
    }
}
