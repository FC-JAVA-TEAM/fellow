package com.example.application.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.application.enums.TaskPriority;
import com.example.application.enums.TaskRecurrence;
import com.example.application.enums.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
   // private LocalTime startTime;
    //private LocalTime endTime;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.NOT_STARTED;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.MEDIUM;

   // @Enumerated(EnumType.STRING)
  //  private TaskRecurrence recurrence = TaskRecurrence.NONE;

    // Constructors
    public Task() {
    }

    public Task(LocalDate date, String description) {
        this.date = date;
        this.description = description;
        this.status = TaskStatus.NOT_STARTED;
        this.priority = TaskPriority.MEDIUM;
       // this.recurrence = TaskRecurrence.NONE;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    // No setter for id as it is auto-generated

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

	/*
	 * public LocalTime getStartTime() { return startTime; }
	 * 
	 * public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
	 * 
	 * public LocalTime getEndTime() { return endTime; }
	 * 
	 * public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
	 */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status != null ? status : TaskStatus.NOT_STARTED;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority != null ? priority : TaskPriority.MEDIUM;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

//    public TaskRecurrence getRecurrence() {
//        return recurrence != null ? recurrence : TaskRecurrence.NONE;
//    }
//
//    public void setRecurrence(TaskRecurrence recurrence) {
//        this.recurrence = recurrence;
//    }
}
