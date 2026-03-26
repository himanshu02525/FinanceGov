package com.financegov.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demoNew.model.Report;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByScope(String scope);
}
