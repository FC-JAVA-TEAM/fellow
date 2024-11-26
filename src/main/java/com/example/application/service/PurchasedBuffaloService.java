package com.example.application.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.entity.PurchasedBuffalo;
import com.example.application.repo.PurchasedBuffaloRepository;

@Service
public class PurchasedBuffaloService {

    @Autowired
    private PurchasedBuffaloRepository purchasedBuffaloRepository;

    public void saveBuffalo(PurchasedBuffalo buffalo) {
        purchasedBuffaloRepository.save(buffalo);
    }

    public List<PurchasedBuffalo> findAll() {
        return purchasedBuffaloRepository.findAll();
    }
}

