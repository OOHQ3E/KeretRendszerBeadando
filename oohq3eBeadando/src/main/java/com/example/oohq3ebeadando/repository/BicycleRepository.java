package com.example.oohq3ebeadando.repository;

import com.example.oohq3ebeadando.model.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BicycleRepository extends JpaRepository<Bicycle,Long> {
    @Query(value = "select * from bicycles where type = ?1",nativeQuery = true)
    List<Bicycle> findBicycleByType(String type);
    @Query(value = "select * from bicycles where tyreSize = ?1",nativeQuery = true)
    List<Bicycle> findBicycleByTyreSize(int tyreSize);
}
