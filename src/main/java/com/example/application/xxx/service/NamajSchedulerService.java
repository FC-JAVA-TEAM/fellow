package com.example.application.xxx.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.application.xxx.entity.NamajEntry;
import com.example.application.xxx.repo.NamajEntryRepository;

@Service
public class NamajSchedulerService {

    private final NamajEntryRepository namajEntryRepository;

    public NamajSchedulerService(NamajEntryRepository namajEntryRepository) {
        this.namajEntryRepository = namajEntryRepository;
    }

    // This will run every day at 5 AM
  //  @Scheduled(cron = "0 0 5 * * *") 
    @Scheduled(cron = "0 59 11 * * *")
    public void addDailyNamazEntries() {
        LocalDate today = LocalDate.now();
        
        // List of Namaz types to add
        List<String> namazTypes = Arrays.asList("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha", "Tahajjud");

        for (String namazType : namazTypes) {
            NamajEntry entry = new NamajEntry();
            entry.setNamazDate(today);            // Set current date
            entry.setNamazType(namazType);        // Set Namaz type
            entry.setJamatType("N/A");            // Default jamat type
            entry.setMissed("Yes");               // Default missed value
            entry.setKaza("Kaza Not Done");       // Default kaza status
            
            namajEntryRepository.save(entry);     // Save the entry to the database
        }
    }
}

