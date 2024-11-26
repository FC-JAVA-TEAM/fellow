package com.example.application.xxx.service;


import com.example.application.xxx.entity.DailyMoodXX;
import com.example.application.xxx.repo.DailyMoodRepositoryXX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyMoodServiceXX {

    @Autowired
    private DailyMoodRepositoryXX dailyMoodRepository;

    public DailyMoodXX save(DailyMoodXX mood) {
        return dailyMoodRepository.save(mood);
    }

    public void delete(DailyMoodXX mood) {
        dailyMoodRepository.delete(mood);
    }

    public DailyMoodXX findByDate(LocalDate date) {
        return dailyMoodRepository.findByDate(date);
    }
    
    public void update(DailyMoodXX mood) {
        dailyMoodRepository.save(mood); // Use the save method to update as well
    }

    public List<DailyMoodXX> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return dailyMoodRepository.findByDateBetween(startDate, endDate);
    }
}
