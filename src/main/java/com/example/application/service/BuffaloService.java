package com.example.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.entity.Buffalo;
import com.example.application.repo.BuffaloRepository;

@Service
public class BuffaloService {
    @Autowired
    private BuffaloRepository buffaloRepository;

    // Save or update a buffalo
    public Buffalo saveBuffalo(Buffalo buffalo) {
        return buffaloRepository.save(buffalo);
    }

    // Find a buffalo by ID
    public Optional<Buffalo> findBuffaloById(Long id) {
        return buffaloRepository.findById(id);
    }

    // Get all buffaloes
    public List<Buffalo> findAllBuffaloes() {
        return buffaloRepository.findAll();
    }

    // Delete a buffalo
    public void deleteBuffalo(Long id) {
        buffaloRepository.deleteById(id);
    }
}
