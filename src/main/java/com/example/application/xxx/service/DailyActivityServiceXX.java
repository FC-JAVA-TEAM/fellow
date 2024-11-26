package com.example.application.xxx.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.application.xxx.entity.DailyActivityXX;
import com.example.application.xxx.repo.DailyActivityRepositoryXX;


@Service
@Transactional
public class DailyActivityServiceXX {
    @Autowired
    private DailyActivityRepositoryXX dailyActivityRepository;

    
    public void deleteActivity(String description, LocalDate date) {
        dailyActivityRepository.deleteByDescriptionAndDate(description, date);
    }
    
   
    public DailyActivityXX save(DailyActivityXX activity) {
        return dailyActivityRepository.save(activity);
    }

    public void delete(DailyActivityXX activity) {
        dailyActivityRepository.delete(activity);
    }

    public List<DailyActivityXX> findAll() {
        return dailyActivityRepository.findAll();
    }

    public List<DailyActivityXX> findByDate(LocalDate date) {
        return dailyActivityRepository.findByDate(date);
    }

    public List<DailyActivityXX> findByMonth(LocalDate month) {
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        return dailyActivityRepository.findByDateBetween(startDate, endDate);
    }

    public boolean existsByDateAndDescription(LocalDate date, String description) {
        return dailyActivityRepository.existsByDateAndDescription(date, description);
    }

    public void update(DailyActivityXX activity) {
        dailyActivityRepository.save(activity);
    }
    
    public List<DailyActivityXX> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return dailyActivityRepository.findByDateBetween(startDate, endDate);
    }

    public void deleteActivitiesInRange(String description, LocalDate startDate, LocalDate endDate) {
        // Fetch activities in the given date range
        List<DailyActivityXX> activities = dailyActivityRepository.findByDateBetween(startDate, endDate);
        
        // Iterate over the fetched activities
        for (DailyActivityXX activity : activities) {
            // Check if the activity description matches and delete it
            if (activity.getDescription().equalsIgnoreCase(description)) {
            	dailyActivityRepository.delete(activity);
            }
        }
    }
}