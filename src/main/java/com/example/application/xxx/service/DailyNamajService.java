package com.example.application.xxx.service;


import com.example.application.xxx.entity.DailyNoteXX;
import com.example.application.xxx.entity.NamajEntry;
import com.example.application.xxx.repo.NamajEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DailyNamajService {
    @Autowired
    private NamajEntryRepository namajEntryRepository;

    public void save(NamajEntry entry) {
        namajEntryRepository.save(entry);
    }

    
    public void update(NamajEntry entry) {
        namajEntryRepository.save(entry);
    }
    public List<NamajEntry> findByDate(LocalDate date) {
        return namajEntryRepository.findByNamazDate(date);
    }
    
    public void delete(NamajEntry entry) { 
        namajEntryRepository.delete(entry);
    } 

    
    public Optional<NamajEntry> findByDateAndType(LocalDate date, String namazType) { 
        return namajEntryRepository.findByNamazDateAndNamazType(date, namazType);
    }
    
    public List<NamajEntry> findByMonth(LocalDate month) {
        LocalDate startDate = month.withDayOfMonth(1); 
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        return namajEntryRepository.findByNamazDateBetween(startDate, endDate);
    }
} 

