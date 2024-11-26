package com.example.application.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.application.entity.DailyActivity;

public interface DailyActivityRepository extends JpaRepository<DailyActivity, Long> {
    List<DailyActivity> findByDate(LocalDate date);
    List<DailyActivity> findByDateBetween(LocalDate startDate, LocalDate endDate);
	boolean existsByDateAndDescription(LocalDate date, String activity);
}

