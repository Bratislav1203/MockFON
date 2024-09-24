package fon.njt.mockfon.repository;

import fon.njt.mockfon.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    @Query("SELECT e FROM Exam e WHERE e.dateAndTime > :currentDateTime ORDER BY e.dateAndTime ASC")
    List<Exam> findAllUpcomingExams(@Param("currentDateTime") LocalDateTime currentDateTime);

}
