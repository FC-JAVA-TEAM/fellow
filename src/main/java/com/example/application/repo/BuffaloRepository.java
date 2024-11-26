package com.example.application.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.application.entity.Buffalo;

@Repository
public interface BuffaloRepository extends JpaRepository<Buffalo, Long> {
}

