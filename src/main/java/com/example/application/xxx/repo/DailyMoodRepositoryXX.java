package com.example.application.xxx.repo;

import com.example.application.xxx.entity.DailyMoodXX;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyMoodRepositoryXX extends JpaRepository<DailyMoodXX, Long> {
    DailyMoodXX findByDate(LocalDate date); // Find mood by date
    List<DailyMoodXX> findByDateBetween(LocalDate startDate, LocalDate endDate); // Find moods between dates
}
