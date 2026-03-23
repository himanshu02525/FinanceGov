package com.financegov.model;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entity_document_NEW")
public class EntityDocument {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;
 
    @ManyToOne
    @JoinColumn(name = "entity_id")
    private CitizenBusiness citizenBusiness;
 
    private String docType;
    private String fileURI;
    private String uploadedDate;
    private String verificationStatus;
}
 