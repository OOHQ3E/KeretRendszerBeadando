package com.example.oohq3ebeadando.repository;

import com.example.oohq3ebeadando.model.RollerScooter;
import com.example.oohq3ebeadando.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUname(String uname);
    @Query(value = "select * from users where uname = ?1",nativeQuery = true)
    List<User> findUserByUname(String type);
    User findById(long id);
}
