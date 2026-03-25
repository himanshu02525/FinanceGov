//communicates with entity class

package com.financegov.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.financegov.model.Report;

@Repository  //create bean component-->to communicate with database

//Jpa Repository take entity name of model and primary keys datatype
public interface ReportRepository extends JpaRepository<Report, Integer> { // to perform basic crud operation 
    List<Report> findByStatus(String status);
    List<Report> findByScope(String scope);
    List<Report> findByGeneratedDateBetween(LocalDate startDate, LocalDate endDate);//findby is the jpa repository formate 
}







































/*
 * package com.financegov.analytics.repository;
 * 
 * import java.time.LocalDate; import java.util.List; import
 * org.springframework.data.jpa.repository.JpaRepository; import
 * org.springframework.stereotype.Repository; import
 * com.financegov.analytics.model.Report;
 * 
 * @Repository public interface ReportRepository extends JpaRepository<Report,
 * Integer> {
 * 
 * List<Report> findByStatus(String status);
 * 
 * List<Report> findByScope(String scope);
 * 
 * List<Report> findByGeneratedDateBetween(LocalDate startDate, LocalDate
 * endDate); }
 */