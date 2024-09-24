package fon.njt.mockfon.controller;

import com.paypal.api.payments.Refund;
import fon.njt.mockfon.dto.ExamRegistrationRequest;
import fon.njt.mockfon.model.Exam;
import fon.njt.mockfon.model.ExamRegistration;
import fon.njt.mockfon.model.NotificationEmail;
import fon.njt.mockfon.model.User;
import fon.njt.mockfon.service.ExamRegistrationService;
import fon.njt.mockfon.service.MailService;
import fon.njt.mockfon.service.PayPalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class ExamRegistrationController {

    private final ExamRegistrationService registrationService;
    private final PayPalService payPalService;
    private final MailService mailService;

    public ExamRegistrationController(ExamRegistrationService registrationService, PayPalService payPalService, MailService mailService) {
        this.registrationService = registrationService;
        this.payPalService = payPalService;
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<String> registerForExam(@RequestBody ExamRegistrationRequest request) {
        try {
            ExamRegistration registration = registrationService.saveRegistration(request);

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
    public ResponseEntity<String> cancelRegistration(
            @RequestParam String email,
            @RequestParam Long examId) {
        try {
            ExamRegistration registration = registrationService.findRegistration(email, examId)
                    .orElseThrow(() -> new RuntimeException("Registration not found"));

            Refund refund = payPalService.refundPayment(registration.getTransactionId());

            if (!"completed".equalsIgnoreCase(refund.getState())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Refundacija nije uspela. Proverite transakciju i pokušajte ponovo.");
            }

            registrationService.updateRegistrationStatus(email, examId, "CANCELLED");
            mailService.sendMail(new NotificationEmail("PayPal Refund", email,""), "refundTemplate");
            return ResponseEntity.ok("Registracija je uspešno otkazana i refundacija je izvršena.");
        } catch (Exception e) {
            System.err.println("Error during cancellation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Došlo je do greške prilikom otkazivanja rezervacije.");
        }
    }

    @GetMapping("/{examId}/users")
    public ResponseEntity<List<User>> getUsersForExam(@PathVariable Long examId) {
        List<User> users = registrationService.getUsersForExam(examId);
        return ResponseEntity.ok(users);
    }
}
