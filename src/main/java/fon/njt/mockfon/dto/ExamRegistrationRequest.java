package fon.njt.mockfon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamRegistrationRequest {
    private Long examId;
    private String email;
}
