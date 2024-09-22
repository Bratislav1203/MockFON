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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public ExamRegistration saveRegistration(Long examId, String userEmail) {
        ExamRegistration examRegistration = new ExamRegistration();

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Exam exam = examRepository.getById(examId);

        examRegistration.setExam(exam);
        examRegistration.setUser(user);
        examRegistration.setRegistrationDate(LocalDate.now());

        return registrationRepository.save(examRegistration);
    }

    public List<ExamRegistration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public ExamRegistration getRegistrationById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    public void deleteRegistration(String email, Long examId) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var registration = registrationRepository.findByUser_UserIdAndExam_ExamId(user.getUserId(), examId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registrationRepository.delete(registration);
    }

    public List<Exam> getUserExams(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getUserId();

        List<ExamRegistration> registrations = registrationRepository.findByUser_UserId(userId);

        return registrations.stream()
                .map(ExamRegistration::getExam)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<User> getUsersForExam(Long examId) {
        return registrationRepository.findByExam_ExamId(examId).stream()
                .map(ExamRegistration::getUser)
                .collect(Collectors.toList());
    }
}
