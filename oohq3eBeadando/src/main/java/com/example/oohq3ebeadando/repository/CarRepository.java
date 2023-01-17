package com.example.oohq3ebeadando.repository;
import com.example.oohq3ebeadando.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {
        @Query(value = "select * from cars where model = ?1",nativeQuery = true)
        List<Car> findCarModel(String model);

}

