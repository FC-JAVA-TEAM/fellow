// src/main/java/com/example/application/service/DailyNoteService.java
package com.example.application.xxx.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.xxx.entity.DailyNoteXX;
import com.example.application.xxx.repo.DailyNoteRepositoryXX;

@Service
public class DailyNoteServiceXX {
    @Autowired
    private DailyNoteRepositoryXX dailyNoteRepository;

    public DailyNoteXX save(DailyNoteXX note) {
        return dailyNoteRepository.save(note);
    }

    public void delete(DailyNoteXX note) {
        dailyNoteRepository.delete(note);
    }

    public List<DailyNoteXX> findByDate(LocalDate date) {
        return dailyNoteRepository.findByDate(date);
    }

    public List<DailyNoteXX> findByMonth(LocalDate month) {
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        return dailyNoteRepository.findByDateBetween(startDate, endDate);
    }

    public void update(DailyNoteXX note) {
        dailyNoteRepository.save(note);
    }

    public List<DailyNoteXX> findAll() {
        return dailyNoteRepository.findAll();
    }
}