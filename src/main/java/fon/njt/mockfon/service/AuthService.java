package fon.njt.mockfon.service;

import fon.njt.mockfon.dto.AuthenticationResponse;
import fon.njt.mockfon.dto.LoginRequest;
import fon.njt.mockfon.dto.RegisterRequest;
import fon.njt.mockfon.exception.EmailAlreadyInUseException;
import fon.njt.mockfon.model.User;
import fon.njt.mockfon.repository.UserRepository;
import fon.njt.mockfon.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;


    public void signup(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyInUseException("Email already in use.");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setAddress(registerRequest.getAddress());
        user.setName(registerRequest.getName());
        user.setSurname(registerRequest.getSurname());
        user.setUmcn(registerRequest.getUcmn());
        userRepository.save(user);
    }
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationMillis()))
                .username(loginRequest.getUsername())
                .build();
    }


}
