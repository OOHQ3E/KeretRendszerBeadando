package com.example.oohq3ebeadando.repository;
import com.example.oohq3ebeadando.model.RollerScooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RollerScooterRepository extends JpaRepository<RollerScooter,Long> {
    @Query(value = "select COUNT(*) from rollerscooters where type = ?1",nativeQuery = true)
    int findRollerScooterByType(String type);
}
