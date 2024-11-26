// src/main/java/com/example/application/service/ActivityStatusService.java
package com.example.application.service;

import com.example.application.entity.Activity;
import com.example.application.entity.ActivityStatus;
import com.example.application.repo.ActivityStatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityStatusService {

    @Autowired
    private ActivityStatusRepository activityStatusRepository;

    public ActivityStatus save(ActivityStatus status) {
        return activityStatusRepository.save(status);
    }

    public void delete(ActivityStatus status) {
        activityStatusRepository.delete(status);
    }

    public List<ActivityStatus> findByActivityAndDate(Activity activity, LocalDate date) {
        return activityStatusRepository.findByActivityAndDate(activity, date);
    }

    public List<ActivityStatus> findByDate(LocalDate date) {
        return activityStatusRepository.findByDate(date);
    }

//    public List<ActivityStatus> findByDateBetween(LocalDate startDate, LocalDate endDate) {
//        return activityStatusRepository.findByDateBetween(startDate, endDate);
//    }
}
