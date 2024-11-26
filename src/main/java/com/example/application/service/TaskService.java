package com.example.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.application.entity.Task;
import com.example.application.enums.TaskStatus;
import com.example.application.repo.TaskRepository;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findByDate(LocalDate date) {
        return taskRepository.findByDate(date);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }
    
    public List<Task> findByMonth(LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());
        return taskRepository.findByDateBetween(startOfMonth, endOfMonth);
    }
    public void delete(Task task) {
        taskRepository.delete(task);
    }
    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight every day
    public void updateOverdueTasks() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Task> tasks = taskRepository.findByDate(yesterday);

        tasks.forEach(task -> {
            if (task.getStatus() != TaskStatus.COMPLETED) {
                task.setStatus(TaskStatus.NOT_COMPLETED); // You may need to add this status
                taskRepository.save(task);
            }
        });
    }
}
