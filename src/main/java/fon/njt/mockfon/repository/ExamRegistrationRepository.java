package fon.njt.mockfon.repository;

import fon.njt.mockfon.model.ExamRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamRegistrationRepository extends JpaRepository<ExamRegistration, Long> {
    List<ExamRegistration> findByUser_UserId(Long userId);
    Optional<ExamRegistration> findByUser_UserIdAndExam_ExamId(Long userId, Long examId);
    List<ExamRegistration> findByExam_ExamId(Long examId);


}
