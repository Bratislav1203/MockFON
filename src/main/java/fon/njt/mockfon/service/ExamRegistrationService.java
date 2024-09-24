package fon.njt.mockfon.service;

import com.paypal.api.payments.Refund;
import fon.njt.mockfon.dto.ExamRegistrationRequest;
import fon.njt.mockfon.model.Exam;
import fon.njt.mockfon.model.ExamRegistration;
import fon.njt.mockfon.model.User;
import fon.njt.mockfon.repository.ExamRegistrationRepository;
import fon.njt.mockfon.repository.ExamRepository;
import fon.njt.mockfon.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamRegistrationService {

    private final ExamRegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final PayPalService payPalService;

    public ExamRegistrationService(ExamRegistrationRepository registrationRepository, UserRepository userRepository, ExamRepository examRepository, PayPalService payPalService) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.examRepository = examRepository;
        this.payPalService = payPalService;
    }

    public ExamRegistration saveRegistration(ExamRegistrationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Exam exam = examRepository.getById(request.getExamId());

        Optional<ExamRegistration> existingRegistration = registrationRepository.findByUserAndExam(user, exam);

        if (existingRegistration.isPresent()) {
            ExamRegistration examRegistration = existingRegistration.get();
            examRegistration.setTransactionId(request.getTransactionId());
            examRegistration.setCaptureId(request.getCaptureId());

           if ("CANCELLED".equalsIgnoreCase(examRegistration.getStatus())) {
                examRegistration.setStatus("COMPLETED");
            }
            return registrationRepository.save(examRegistration);
        }
        ExamRegistration newRegistration = new ExamRegistration();
        newRegistration.setTransactionId(request.getTransactionId());
        newRegistration.setCaptureId(request.getCaptureId());
        newRegistration.setAmount((double) exam.getPrice());
        newRegistration.setExam(exam);
        newRegistration.setUser(user);
        newRegistration.setPayPalEmail(request.getPayPalEmail());
        newRegistration.setRegistrationDate(LocalDate.now());
        newRegistration.setStatus("COMPLETED");

        return registrationRepository.save(newRegistration);
    }


    public List<ExamRegistration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public ExamRegistration getRegistrationById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    public Long deleteRegistration(String email, Long examId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExamRegistration registration = registrationRepository.findByUser_UserIdAndExam_ExamId(user.getUserId(), examId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registrationRepository.delete(registration);
        return registration.getRegistrationId();
    }

    public List<Exam> getUserExams(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getUserId();

        List<ExamRegistration> registrations = registrationRepository.findByUser_UserId(userId);

        LocalDateTime currentDateTime = LocalDateTime.now(); // Trenutni datum i vreme

        return registrations.stream()
                .filter(registration -> "COMPLETED".equalsIgnoreCase(registration.getStatus()))
                .map(ExamRegistration::getExam)
                .filter(exam -> exam.getDateAndTime().isAfter(currentDateTime)) // Filtriranje ispita koji nisu prošli
                .distinct()
                .sorted(Comparator.comparing(Exam::getDateAndTime)) // Sortiranje ispita po datumu i vremenu
                .collect(Collectors.toList());
    }


    public List<User> getUsersForExam(Long examId) {
        return registrationRepository.findByExam_ExamId(examId).stream()
                .filter(registration -> "COMPLETED".equalsIgnoreCase(registration.getStatus())) // Filtrira samo one sa statusom "COMPLETED"
                .map(ExamRegistration::getUser)
                .collect(Collectors.toList());
    }


    public void updateRegistrationStatus(String email, Long examId, String newStatus) {
        ExamRegistration registration = registrationRepository.findByUser_EmailAndExam_ExamId(email, examId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registration.setStatus(newStatus);

        registrationRepository.save(registration);
    }

    public Optional<ExamRegistration> findRegistration(String email, Long examId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        return registrationRepository.findByUserAndExam(user, exam);
    }

    public void delete(Long registrationId){
        this.registrationRepository.deleteById(registrationId);
    }

}
