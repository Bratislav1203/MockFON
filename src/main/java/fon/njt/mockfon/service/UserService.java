 package fon.njt.mockfon.service;

import fon.njt.mockfon.model.User;
import fon.njt.mockfon.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User registerUser(User user){
        return userRepository.save(user);
    }

    public User findUserbByID(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
    }



}
