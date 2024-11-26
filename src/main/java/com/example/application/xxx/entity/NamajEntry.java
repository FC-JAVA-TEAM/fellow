package com.example.application.xxx.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "namaj_entries", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"namazDate", "namazType"})
})
public class NamajEntry {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private LocalDate namazDate;

	    @Column(nullable = false, length = 20)
	    private String namazType;

	    @Column(nullable = false, length = 20)
	    private String jamatType;

	    @Column(nullable = false, length = 10) // Assuming the length for the string
	    private String missed; // Change to String to hold "Yes" or "No"

	    private String kaza;

    // Getters and Setters

    public String  isKaza() {
		return kaza;
	}

	public void setKaza(String  kaza) {
		this.kaza = kaza;
	}

	public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getNamazDate() {
        return namazDate;
    }

    public void setNamazDate(LocalDate namazDate) {
        this.namazDate = namazDate;
    }

    public String getNamazType() {
        return namazType;
    }

    public void setNamazType(String namazType) {
        this.namazType = namazType;
    }

    public String getJamatType() {
        return jamatType;
    }
 
    public void setJamatType(String jamatType) {
        this.jamatType = jamatType;
    }

	public String getMissed() {
		return missed;
	}

	public void setMissed(String missed) {
		this.missed = missed;
	}

	public String getKaza() {
		return kaza;
	}

   
}
