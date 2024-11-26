package com.example.application.entity;

import java.time.LocalDate;

import com.example.application.enums.HealthStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Buffalo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Information
    private String name;
    private String breed;
    private int age;
    private float weight;

    // Born or Purchased
    private Boolean isBorn;  // true if born, false if purchased
    private LocalDate dateOfBirth;
    private float birthWeight;
    private LocalDate dateOfPurchase;
    private String purchaseSource;
    private int ageAtPurchase;

    // Mapping to the mother buffalo (self-referencing relationship)
    @ManyToOne
    @JoinColumn(name = "mother_buffalo_id")
    private Buffalo motherBuffalo;

    // Health and Medical Information
    private LocalDate lastVaccinationDate;
    private LocalDate nextVaccinationDate;
    private String medicalHistory;
    @Enumerated(EnumType.STRING)
    private HealthStatus healthStatus;

    // Milk Production (Optional)
    private Double averageDailyMilkProduction;
    private Double totalMilkProduced;

    // Feeding Information (Optional)
    private Double dailyFeedRequirement;
    private String feedType;

    // Reproduction (Optional)
    private Boolean isPregnant;
    private LocalDate lastBreedingDate;
    private LocalDate expectedCalvingDate;

    // Miscellaneous Notes
    private String notes;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Boolean getIsBorn() {
        return isBorn;
    }

    public void setIsBorn(Boolean isBorn) {
        this.isBorn = isBorn;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public float getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(float birthWeight) {
        this.birthWeight = birthWeight;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getPurchaseSource() {
        return purchaseSource;
    }

    public void setPurchaseSource(String purchaseSource) {
        this.purchaseSource = purchaseSource;
    }

    public int getAgeAtPurchase() {
        return ageAtPurchase;
    }

    public void setAgeAtPurchase(int ageAtPurchase) {
        this.ageAtPurchase = ageAtPurchase;
    }

    public Buffalo getMotherBuffalo() {
        return motherBuffalo;
    }

    public void setMotherBuffalo(Buffalo motherBuffalo) {
        this.motherBuffalo = motherBuffalo;
    }

    public LocalDate getLastVaccinationDate() {
        return lastVaccinationDate;
    }

    public void setLastVaccinationDate(LocalDate lastVaccinationDate) {
        this.lastVaccinationDate = lastVaccinationDate;
    }

    public LocalDate getNextVaccinationDate() {
        return nextVaccinationDate;
    }

    public void setNextVaccinationDate(LocalDate nextVaccinationDate) {
        this.nextVaccinationDate = nextVaccinationDate;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(HealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Double getAverageDailyMilkProduction() {
        return averageDailyMilkProduction;
    }

    public void setAverageDailyMilkProduction(Double averageDailyMilkProduction) {
        this.averageDailyMilkProduction = averageDailyMilkProduction;
    }

    public Double getTotalMilkProduced() {
        return totalMilkProduced;
    }

    public void setTotalMilkProduced(Double totalMilkProduced) {
        this.totalMilkProduced = totalMilkProduced;
    }

    public Double getDailyFeedRequirement() {
        return dailyFeedRequirement;
    }

    public void setDailyFeedRequirement(Double dailyFeedRequirement) {
        this.dailyFeedRequirement = dailyFeedRequirement;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public Boolean getIsPregnant() {
        return isPregnant;
    }

    public void setIsPregnant(Boolean isPregnant) {
        this.isPregnant = isPregnant;
    }

    public LocalDate getLastBreedingDate() {
        return lastBreedingDate;
    }

    public void setLastBreedingDate(LocalDate lastBreedingDate) {
        this.lastBreedingDate = lastBreedingDate;
    }

    public LocalDate getExpectedCalvingDate() {
        return expectedCalvingDate;
    }

    public void setExpectedCalvingDate(LocalDate expectedCalvingDate) {
        this.expectedCalvingDate = expectedCalvingDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
