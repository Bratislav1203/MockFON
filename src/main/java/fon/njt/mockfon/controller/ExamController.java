package fon.njt.mockfon.controller;

import fon.njt.mockfon.dto.ExamDto;
import fon.njt.mockfon.model.Exam;
import fon.njt.mockfon.service.ExamService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
@AllArgsConstructor
public class ExamController {

    private final ExamService examService;

    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        List<Exam> exams = examService.findAllExams();
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getExamById(@PathVariable Long id) {
        Exam exam = examService.findExamById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Exam retrieved successfully");
        response.put("exam", exam);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createExam(@RequestBody ExamDto examDto) {
        Exam newExam = examService.createExam(examDto);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Exam created successfully");
        response.put("exam", newExam);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateExam(@PathVariable Long id, @RequestBody ExamDto examDto) {
        Exam updatedExam = examService.updateExam(id, examDto);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Exam updated successfully");
        response.put("exam", updatedExam);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Exam deleted successfully");
        return ResponseEntity.ok(response);
    }
}
