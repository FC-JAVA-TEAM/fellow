// src/main/java/com/example/application/service/DailyNoteService.java
package com.example.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.entity.DailyNote;
import com.example.application.repo.DailyNoteRepository;

@Service
public class DailyNoteService {

    @Autowired
    private DailyNoteRepository dailyNoteRepository;

    public DailyNote save(DailyNote note) {
        return dailyNoteRepository.save(note);
    }

    public void delete(DailyNote note) {
        dailyNoteRepository.delete(note);
    }

    public List<DailyNote> findByDate(LocalDate date) {
        return dailyNoteRepository.findByDate(date);
    }

    public List<DailyNote> findByMonth(LocalDate month) {
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        return dailyNoteRepository.findByDateBetween(startDate, endDate);
    }
}
