// src/main/java/com/example/application/repository/ActivityRepository.java
package com.example.application.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.application.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByName(String name);
}
