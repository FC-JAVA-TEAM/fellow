package com.example.application.repo;


import com.example.application.entity.PurchasedBuffalo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedBuffaloRepository extends JpaRepository<PurchasedBuffalo, Long> {
}
