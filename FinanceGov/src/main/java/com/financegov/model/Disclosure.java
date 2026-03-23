package com.financegov.model;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "disclosures")
@Data
public class Disclosure {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long disclosureId;

	
	@ManyToOne(fetch = FetchType.LAZY) // Many disclosures belong to one Citizen
	@JoinColumn(name = "entity_id", nullable = false) // Maps to the entity_id column in MySQL
	private CitizenBusiness citizenBusiness;

	@Column(nullable = false, length = 20)
	private String type;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false, length = 20)
	private String status;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String remarks;

	@Column(nullable = false, updatable = false)
	private LocalDateTime submissionDate = LocalDateTime.now();

	public Disclosure() {
	}




}
//public void setCitizenBusiness(CitizenBusiness citizenBusiness) {
//	this.citizenBusiness = citizenBusiness;
//}



// Standard Getters/Setters for other fields...
//    public Long getDisclosureId() { return disclosureId; }
//    public void setDisclosureId(Long id) { this.disclosureId = id; }
//    public String getType() { return type; }
//    public void setType(String type) { this.type = type; }
//    public BigDecimal getAmount() { return amount; }
//    public void setAmount(BigDecimal amt) { this.amount = amt; }
//    public String getStatus() { return status; }
//    public void setStatus(String st) { this.status = st; }
//    public String getRemarks() { return remarks; }
//    public void setRemarks(String rem) { this.remarks = rem; }



//    // Getters and Setters
//    public Long getDisclosureId() { return disclosureId; }
//    public void setDisclosureId(Long disclosureId) { this.disclosureId = disclosureId; }
//
//    public Long getEntityId() { return entityId; }
//    public void setEntityId(Long entityId) { this.entityId = entityId; }
//
//    public String getType() { return type; }
//    public void setType(String type) { this.type = type; }
//
//    public BigDecimal getAmount() { return amount; }
//    public void setAmount(BigDecimal amount) { this.amount = amount; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//
//    public String getRemarks() { return remarks; }
//    public void setRemarks(String remarks) { this.remarks = remarks; }
//
//    public LocalDateTime getSubmissionDate() { return submissionDate; }
//    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }