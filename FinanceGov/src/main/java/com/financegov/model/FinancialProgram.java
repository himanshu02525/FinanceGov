package com.financegov.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.financegov.enums.ProgramStatus;

@Entity
@Table(name = "financial_program")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programId;

    @NotNull
    private String title;

    private String description;

    @Min(value = 0, message = "Budget must be non-negative")
    private Double budget;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProgramStatus status;
}
