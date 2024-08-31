package fon.njt.mockfon.controller;
import fon.njt.mockfon.dto.AuthenticationResponse;
import fon.njt.mockfon.dto.LoginRequest;
import fon.njt.mockfon.dto.RegisterRequest;
import fon.njt.mockfon.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("auth/")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return ResponseEntity.status(HttpStatus.OK).body("User Registration Successful");
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully. Please go to Login page and try our app", OK); //HttpStatus.OK
    }

    @PostMapping("login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

}
