package fon.njt.mockfon.service;

import fon.njt.mockfon.model.Exam;
import fon.njt.mockfon.model.ExamRegistration;
import fon.njt.mockfon.model.User;
import fon.njt.mockfon.repository.ExamRegistrationRepository;
import fon.njt.mockfon.repository.ExamRepository;
import fon.njt.mockfon.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExamRegistrationService {

    private final ExamRegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;

    public ExamRegistrationService(ExamRegistrationRepository registrationRepository, UserRepository userRepository, ExamRepository examRepository) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.examRepository = examRepository;
    }

    public ExamRegistration saveRegistration(ExamRegistration registration, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        if (registration.getExam() != null && registration.getExam().getExamId() != null) {
            Exam exam = examRepository.findById(registration.getExam().getExamId())
                    .orElseThrow(() -> new RuntimeException("Exam not found"));
            registration.setExam(exam);
        } else {
            throw new IllegalArgumentException("Exam information is missing.");
        }

        registration.setUser(user);

        return registrationRepository.save(registration);
    }

    public List<ExamRegistration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public ExamRegistration getRegistrationById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    public void deleteRegistration(Long id) {
        registrationRepository.deleteById(id);
    }
}
