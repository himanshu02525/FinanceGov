package com.financegov.model;

//schema of db table

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

@Data // automatically get getter and setter
@Entity // used for creation of db table
@Table(name = "reports")
public class Report {
	@Id // stores unique value
	@GeneratedValue(strategy = GenerationType.IDENTITY) // automatically generates value from 1 to ...
	private int id;

	private String scope;
	private String metrics;

	@Column(name = "generated_date")
	@JsonFormat(pattern = "yyyy-MM-dd") // for specific format
	private LocalDate generatedDate;

	private String status;
	private double amount;
}
