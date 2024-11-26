package com.example.application.xxx.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.application.xxx.entity.DailyNoteXX;
import com.example.application.xxx.entity.NamajEntry;

@Repository
public interface NamajEntryRepository extends JpaRepository<NamajEntry, Long> {
    List<NamajEntry> findByNamazDate(LocalDate date); // Find entries by date
    Optional<NamajEntry> findByNamazDateAndNamazType(LocalDate date, String namazType);
    List<NamajEntry> findByNamazDateBetween(LocalDate startDate, LocalDate endDate);

    
}

