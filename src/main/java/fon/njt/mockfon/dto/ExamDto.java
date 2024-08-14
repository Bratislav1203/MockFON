package fon.njt.mockfon.dto;

import fon.njt.mockfon.model.ExamSubject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDto {
    private ExamSubject examSubject;
    private LocalDate dateAndTime;
    private LocalDate registrationStart;
    private LocalDate registrationEnd;
}