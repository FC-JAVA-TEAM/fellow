package com.example.application.repo;


import com.example.application.entity.NewbornBuffalo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewbornBuffaloRepository extends JpaRepository<NewbornBuffalo, Long> {
}

