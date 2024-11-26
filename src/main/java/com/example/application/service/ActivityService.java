// src/main/java/com/example/application/service/ActivityService.java
package com.example.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.entity.Activity;
import com.example.application.repo.ActivityRepository;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    public void delete(Activity activity) {
        activityRepository.delete(activity);
    }

    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    public Activity findByName(String name) {
        return activityRepository.findByName(name);
    }

    public Activity findById(Long id) {
        return activityRepository.findById(id).orElse(null);
    }

	public List<Activity> findByMonth(LocalDate month) {
		// TODO Auto-generated method stub
		return null;
	}
}
