package fon.njt.mockfon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationDTO {
    private String password;
    private String address;
    private String umcn;
    private String token;
}
