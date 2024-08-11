package fon.njt.mockfon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;
    @Enumerated(EnumType.STRING)
    private ExamSubject examSubject;
    private LocalDate date;
    private String timeSlot;
    private String topics;
    private LocalDate registrationStart;
    private LocalDate registrationEnd;
}
