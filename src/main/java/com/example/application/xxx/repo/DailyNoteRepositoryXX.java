package com.example.application.xxx.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.application.xxx.entity.DailyNoteXX;

public interface DailyNoteRepositoryXX extends JpaRepository<DailyNoteXX, Long> {
    List<DailyNoteXX> findByDate(LocalDate date);
    List<DailyNoteXX> findByDateBetween(LocalDate startDate, LocalDate endDate);
}