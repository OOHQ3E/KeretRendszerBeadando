package com.example.oohq3ebeadando.service;

import com.example.oohq3ebeadando.model.User;
import com.example.oohq3ebeadando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    public void Register(User user){
        repository.save(user);
    }
    public String getUserAuth(String uname){
        return repository.findByUname(uname).getAuth();
    }
}
