package fon.njt.mockfon.service;

import fon.njt.mockfon.dto.AuthenticationResponse;
import fon.njt.mockfon.dto.LoginRequest;
import fon.njt.mockfon.dto.RegisterRequest;
import fon.njt.mockfon.dto.VerificationDTO;
import fon.njt.mockfon.exception.EmailAlreadyInUseException;
import fon.njt.mockfon.exception.MockFonException;
import fon.njt.mockfon.model.NotificationEmail;
import fon.njt.mockfon.model.User;
import fon.njt.mockfon.model.VerificationToken;
import fon.njt.mockfon.repository.UserRepository;
import fon.njt.mockfon.repository.VerificationTokenRepository;
import fon.njt.mockfon.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;




    public void signup(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyInUseException("Email already in use.");
        }

        User user = new User();
//        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
//        user.setAddress(registerRequest.getAddress());
        user.setName(registerRequest.getName());
        user.setSurname(registerRequest.getSurname());
//        user.setUmcn(registerRequest.getUmcn());
        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Please activate your account", user.getEmail(), "http://localhost:4200/verification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(VerificationDTO verificationDTO) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(verificationDTO.getToken());
        verificationToken.orElseThrow(() -> new MockFonException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
        User user = userRepository.findById(verificationToken.get().getUser().getUserId()).get();
        user.setUmcn(verificationDTO.getUmcn());
        user.setAddress(verificationDTO.getAddress());
        user.setPassword(passwordEncoder.encode(verificationDTO.getPassword()));
        userRepository.save(user);

    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String email = verificationToken.getUser().getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new MockFonException("User not found " +
                email));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) throws UsernameNotFoundException, DisabledException {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail()));

        if (!user.isEnabled()) {
            throw new DisabledException("User account is not active.");
        }

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationMillis()))
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .umcn(user.getUmcn())
                .isAdmin(user.isAdmin())
                .build();
    }

}
