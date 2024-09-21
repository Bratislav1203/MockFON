package fon.njt.mockfon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private int price;
    private LocalDateTime dateAndTime;
    private LocalDate registrationStart;
    private LocalDate registrationEnd;
}
