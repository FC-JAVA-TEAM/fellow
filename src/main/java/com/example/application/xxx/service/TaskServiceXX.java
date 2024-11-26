package com.example.application.xxx.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.xxx.entity.TaskXX;
import com.example.application.xxx.repo.TaskRepositoryXX;

@Service
public class TaskServiceXX {
    @Autowired
    private TaskRepositoryXX taskRepository;

    public List<TaskXX> findByDate(LocalDate date) {
        return taskRepository.findByDate(date);
    }

    public List<TaskXX> findByMonth(LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.plusMonths(1).withDayOfMonth(1);
        return taskRepository.findByMonth(startOfMonth, endOfMonth);
    }

    public TaskXX save(TaskXX task) {
        return taskRepository.save(task);
    }

    public void delete(TaskXX task) {
        taskRepository.delete(task);
    }

    public List<TaskXX> findAll() {
        return taskRepository.findAll();
    }

    public void update(TaskXX task) {
        taskRepository.save(task);
    }
}
