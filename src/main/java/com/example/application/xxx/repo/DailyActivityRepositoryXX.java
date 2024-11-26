package com.example.application.xxx.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.application.xxx.entity.DailyActivityXX;

public interface DailyActivityRepositoryXX extends JpaRepository<DailyActivityXX, Long> {
    List<DailyActivityXX> findByDate(LocalDate date);
    List<DailyActivityXX> findByDateBetween(LocalDate startDate, LocalDate endDate);
    boolean existsByDateAndDescription(LocalDate date, String description);
    @Modifying
    @Transactional
    @Query("DELETE FROM DailyActivityXX d WHERE d.description = ?1 AND d.date = ?2")
    void deleteByDescriptionAndDate(String description, LocalDate date);
}
