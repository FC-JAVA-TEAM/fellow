package com.example.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.entity.DailyActivity;
import com.example.application.repo.DailyActivityRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DailyActivityService {

    @Autowired
    private DailyActivityRepository dailyActivityRepository;

    public DailyActivity save(DailyActivity dailyActivity) {
        return dailyActivityRepository.save(dailyActivity);
       
    }

    public List<DailyActivity> findByDate(LocalDate date) {
        return dailyActivityRepository.findByDate(date);
    }

    public List<DailyActivity> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return dailyActivityRepository.findByDateBetween(startDate, endDate);
    }

    public void delete(DailyActivity dailyActivity) {
        dailyActivityRepository.delete(dailyActivity);
    }
    public void update(DailyActivity dailyActivity) {
        // Optionally, check if the activity exists before updating
        Optional<DailyActivity> existingActivity = dailyActivityRepository.findById(dailyActivity.getId());
        if (existingActivity.isPresent()) {
            // Update the existing activity's fields
            DailyActivity activityToUpdate = existingActivity.get();
            activityToUpdate.setDescription(dailyActivity.getDescription());
            activityToUpdate.setComplete(dailyActivity.isComplete());
            // Add more fields as needed
            
            // Save the updated activity
            dailyActivityRepository.save(activityToUpdate);
        } else {
            throw new EntityNotFoundException("Daily activity not found with id: " + dailyActivity.getId());
        }
    }

	public boolean findByDateAndActivity(LocalDate date, String activity) {
		        return dailyActivityRepository.existsByDateAndDescription(date, activity);
		    }
	
}
