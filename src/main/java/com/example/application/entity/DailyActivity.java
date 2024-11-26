package com.example.application.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class DailyActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Lob // Use @Lob if you expect long text
    private String description;
boolean isComplete;
    public boolean isComplete() {
	return isComplete;
}

public void setComplete(boolean isComplete) {
	this.isComplete = isComplete;
}

public void setId(Long id) {
	this.id = id;
}

	// Constructors
    public DailyActivity() {
    }

    public DailyActivity(LocalDate date, String description) {
        this.date = date;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	
}
