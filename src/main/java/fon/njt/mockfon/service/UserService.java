 package fon.njt.mockfon.service;

import fon.njt.mockfon.model.User;
import fon.njt.mockfon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

 @Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserbByID(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
    }



}
