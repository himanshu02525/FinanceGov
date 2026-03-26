package com.financegov.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "reports")
// Entity class for storing generated reports
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportId;   // Primary key

    private String scope;   // Program/Subsidy/Tax
    private String metrics; // Aggregated values
    private LocalDate generatedDate; // Date when report was generated
}

