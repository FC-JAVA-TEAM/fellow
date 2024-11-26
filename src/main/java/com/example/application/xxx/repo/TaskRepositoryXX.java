// src/main/java/com/example/application/repository/ActivityStatusRepository.java
package com.example.application.xxx.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.application.xxx.entity.TaskXX;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepositoryXX extends JpaRepository<TaskXX, Long> {
    List<TaskXX> findByDate(LocalDate date);
    List<TaskXX> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT t FROM Task t WHERE t.date >= :monthStart AND t.date < :nextMonthStart")
    List<TaskXX> findByMonth(@Param("monthStart") LocalDate monthStart, @Param("nextMonthStart") LocalDate nextMonthStart);
}
