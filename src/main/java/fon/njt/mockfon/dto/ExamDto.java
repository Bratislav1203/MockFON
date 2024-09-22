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
    private Long id;
    private String subject;
    private String date;
    private String time;
    private String registrationStart;
    private String registrationEnd;
    private int price;
}
