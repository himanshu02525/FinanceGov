package com.financegov.model;

import java.time.LocalDate;

import com.finance.enums.DocType;
import com.finance.enums.VerificationStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "entity_document",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"entity_id", "doc_type"}
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    /**
     * Many documents belong to one Citizen/Business
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false)
    private CitizenBusiness citizenBusiness;

    /**
     * Type of document (PAN, AADHAR, PASSPORT, etc.)
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false)
    private DocType docType;

    /**
     * File storage location (PDF only)
     */
    @NotNull
    @Pattern(regexp = ".*\\.pdf$", message = "Only PDF files are allowed")
    @Column(nullable = false)
    private String fileURI;

    /**
     * Date when document was uploaded
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate uploadedDate;

    /**
     * Verification status of document
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;
}