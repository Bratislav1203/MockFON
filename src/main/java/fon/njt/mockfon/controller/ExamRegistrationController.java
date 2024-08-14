package fon.njt.mockfon.controller;

import fon.njt.mockfon.model.ExamRegistration;
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
    public ResponseEntity<ExamRegistration> createRegistration(@RequestBody ExamRegistration registration, Authentication authentication) {
        ExamRegistration savedRegistration = registrationService.saveRegistration(registration, authentication);
        return new ResponseEntity<>(savedRegistration, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<ExamRegistration>> getAllRegistrations() {
        return ResponseEntity.ok(registrationService.getAllRegistrations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamRegistration> getRegistrationById(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getRegistrationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return ResponseEntity.ok().build();
    }
}
