package com.financegov.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subsidy_application")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubsidyApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @NotNull(message = "Entity ID is required")
    private Long entityId;

    @NotNull(message = "Submitted date is required")
    private LocalDate submittedDate;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    @JsonIgnore
    private FinancialProgram program;
}

