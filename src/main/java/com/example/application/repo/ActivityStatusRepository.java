// src/main/java/com/example/application/repository/ActivityStatusRepository.java
package com.example.application.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.application.entity.Activity;
import com.example.application.entity.ActivityStatus;

public interface ActivityStatusRepository extends JpaRepository<ActivityStatus, Long> {
    List<ActivityStatus> findByActivityAndDate(Activity activity, LocalDate date);
    List<ActivityStatus> findByDate(LocalDate date);
}
