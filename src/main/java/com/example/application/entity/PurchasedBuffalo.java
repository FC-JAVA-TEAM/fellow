package com.example.application.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.example.application.enums.HealthStatus;

@Entity
public class PurchasedBuffalo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String breed;
    private float weight;
    private LocalDate dateOfPurchase;
    private int ageAtPurchase;
    private String purchaseSource;

    // Additional fields for health, pregnancy, and milk production
    @Enumerated(EnumType.STRING)
    private HealthStatus healthStatus;

    private Double totalMilkProduced;
    private Boolean isPregnant;
    private LocalDate lastBreedingDate;
    private LocalDate expectedCalvingDate;

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public LocalDate getDateOfPurchase() { return dateOfPurchase; }
    public void setDateOfPurchase(LocalDate dateOfPurchase) { this.dateOfPurchase = dateOfPurchase; }

    public int getAgeAtPurchase() { return ageAtPurchase; }
    public void setAgeAtPurchase(int ageAtPurchase) { this.ageAtPurchase = ageAtPurchase; }

    public String getPurchaseSource() { return purchaseSource; }
    public void setPurchaseSource(String purchaseSource) { this.purchaseSource = purchaseSource; }

    public HealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; }

    public Double getTotalMilkProduced() { return totalMilkProduced; }
    public void setTotalMilkProduced(Double totalMilkProduced) { this.totalMilkProduced = totalMilkProduced; }

    public Boolean getIsPregnant() { return isPregnant; }
    public void setIsPregnant(Boolean isPregnant) { this.isPregnant = isPregnant; }

    public LocalDate getLastBreedingDate() { return lastBreedingDate; }
    public void setLastBreedingDate(LocalDate lastBreedingDate) { this.lastBreedingDate = lastBreedingDate; }

    public LocalDate getExpectedCalvingDate() { return expectedCalvingDate; }
    public void setExpectedCalvingDate(LocalDate expectedCalvingDate) { this.expectedCalvingDate = expectedCalvingDate; }
}
