package fon.njt.mockfon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String name;
    private String surname;
    private String umcn;
    private String authenticationToken;
    private String refreshToken;
    private Instant expiresAt;
    private String email;
    private boolean isAdmin;
}
