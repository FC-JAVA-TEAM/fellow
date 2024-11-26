package com.example.application.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class NewbornBuffalo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String breed;
    private float birthWeight;
    private LocalDate dateOfBirth;
    private String motherBuffaloId;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public float getBirthWeight() { return birthWeight; }
    public void setBirthWeight(float birthWeight) { this.birthWeight = birthWeight; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getMotherBuffaloId() { return motherBuffaloId; }
    public void setMotherBuffaloId(String motherBuffaloId) { this.motherBuffaloId = motherBuffaloId; }
}
