package com.ntd.calculator.record.controller;

import com.ntd.calculator.record.dto.RecordDTO;
import com.ntd.calculator.record.entity.Record;
import com.ntd.calculator.record.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping
    public ResponseEntity<?> getUserRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int perPage,
            @RequestParam(defaultValue = "") String search,
            Authentication authentication) {

        String username = authentication.getName();
        List<RecordDTO> records = recordService.getUserRecords(username, page, perPage, search);
        long total = recordService.getTotalRecordsCount(username, search);

        return ResponseEntity.ok().body(new RecordsResponse(records, total));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        boolean deleted = recordService.deleteRecord(id, username);

        if (deleted) {
            return ResponseEntity.ok().body("Record deleted successfully.");
        } else {
            return ResponseEntity.status(403).body("You are not authorized to delete this record.");
        }
    }

    // Classe auxiliar para a resposta com paginação
    static class RecordsResponse {
        private List<RecordDTO> records;
        private long total;

        public RecordsResponse(List<RecordDTO> records2, long total) {
                            this.records = records2;
            this.total = total;
        }

        public List<RecordDTO> getRecords() {
            return records;
        }

        public long getTotal() {
            return total;
        }
    }
}
