package com.example.application.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.entity.NewbornBuffalo;
import com.example.application.repo.NewbornBuffaloRepository;

@Service
public class NewbornBuffaloService {

    @Autowired
    private NewbornBuffaloRepository newbornBuffaloRepository;

    public void saveBuffalo(NewbornBuffalo buffalo) {
        newbornBuffaloRepository.save(buffalo);
    }

    public List<NewbornBuffalo> findAll() {
        return newbornBuffaloRepository.findAll();
    }
}
