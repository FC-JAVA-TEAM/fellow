package com.example.application.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.application.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDate(LocalDate date);
    List<Task> findByDateBetween(LocalDate startDate, LocalDate endDate);

}
