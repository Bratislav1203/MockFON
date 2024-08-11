package fon.njt.mockfon.service;

import fon.njt.mockfon.dto.RegisterRequest;
import fon.njt.mockfon.model.User;
import fon.njt.mockfon.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;


    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setAddress(registerRequest.getAddress());
        user.setName(registerRequest.getName());
        user.setSurname(registerRequest.getSurname());
        user.setUmcn(registerRequest.getUcmn());
        user = userRepository.save(user);

    }

}
