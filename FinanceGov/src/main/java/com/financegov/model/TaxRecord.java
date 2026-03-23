package com.financegov.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tax_records")
public class TaxRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long taxId;

	// PIN-TO-PIN JOIN IMPLEMENTATION
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_id", nullable = false)
	private CitizenBusiness citizenBusiness;

	@Column(nullable = false)
	private Integer year;

	@Column(precision = 19, scale = 2, nullable = false)
	private BigDecimal amount;

	@Column(length = 20, nullable = false)
	private String status; // Expected: "PAID", "PENDING", "OVERDUE"

	// 1. Default Constructor (Required by JPA)
	public TaxRecord() {
	}

	// 2. All-Args Constructor for Service Layer
	public TaxRecord(CitizenBusiness citizenBusiness, Integer year, BigDecimal amount, String status) {
		this.citizenBusiness = citizenBusiness;
		this.year = year;
		this.amount = amount;
		this.status = status;
	}

	// 3. Manual Getters and Setters

	public Long getTaxId() {
		return taxId;
	}

	public void setTaxId(Long taxId) {
		this.taxId = taxId;
	}

	public CitizenBusiness getCitizenBusiness() {
		return citizenBusiness;
	}

	public void setCitizenBusiness(CitizenBusiness citizenBusiness) {
		this.citizenBusiness = citizenBusiness;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
//    // Getters and Setters
//    public Long getTaxId() { return taxId; }
//    public void setTaxId(Long taxId) { this.taxId = taxId; }
//
//    public Long getEntityId() { return entityId; }
//    public void setEntityId(Long entityId) { this.entityId = entityId; }
//
//    public Integer getYear() { return year; }
//    public void setYear(Integer year) { this.year = year; }
//
//    public BigDecimal getAmount() { return amount; }
//    public void setAmount(BigDecimal amount) { this.amount = amount; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }