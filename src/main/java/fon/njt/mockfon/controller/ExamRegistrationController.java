package fon.njt.mockfon.controller;

import fon.njt.mockfon.dto.ExamRegistrationRequest;
import fon.njt.mockfon.model.Exam;
import fon.njt.mockfon.model.ExamRegistration;
import fon.njt.mockfon.model.User;
import fon.njt.mockfon.service.ExamRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class ExamRegistrationController {

    private final ExamRegistrationService registrationService;

    public ExamRegistrationController(ExamRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<String> registerForExam(@RequestBody ExamRegistrationRequest request) {
        try {
            registrationService.saveRegistration(request.getExamId(), request.getEmail());
            return ResponseEntity.ok("Registracija za ispit je uspešno obavljena.");
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            return ResponseEntity.status(500).body("Došlo je do greške prilikom registracije za ispit.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Exam>> getRegistrationsByEmail(@RequestParam String email) {
        List<Exam> registrations = registrationService.getUserExams(email);
        return ResponseEntity.ok(registrations);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteRegistration(
            @RequestParam String email,
            @RequestParam Long examId) {

        registrationService.deleteRegistration(email, examId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{examId}/users")
    public ResponseEntity<List<User>> getUsersForExam(@PathVariable Long examId) {
        List<User> users = registrationService.getUsersForExam(examId);
        return ResponseEntity.ok(users);
    }
}
