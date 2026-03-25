//write all business logic   // functionality of end 

package com.financegov.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.financegov.analytics.model.Report;
import com.financegov.analytics.dto.ReportDTO;
import com.financegov.analytics.dto.DashboardDTO;
import com.financegov.analytics.repository.ReportRepository;

@Service
public class ReportService {

    @Autowired   //inherit from repository
    private ReportRepository reportRepository;

    
    //Convert Entity to DTO
    private ReportDTO mapToDTO(Report report) {
        ReportDTO dto = new ReportDTO();  //create ReportDTO object 
        dto.setId(report.getId());
        dto.setScope(report.getScope());
        dto.setMetrics(report.getMetrics());
        dto.setGeneratedDate(report.getGeneratedDate());
        dto.setStatus(report.getStatus());
        dto.setAmount(report.getAmount());
        return dto;
    }

    // Dashboard Logic
    public DashboardDTO getDashboardStats() {
        List<Report> all = reportRepository.findAll();  //inbuilt method to fetch all the repository records
        long count = all.size();
        double total = all.stream().mapToDouble(Report::getAmount).sum();  //got all report from stream api-->report to get amount then sum all
        var statusMap = all.stream().collect(Collectors.groupingBy(Report::getStatus, Collectors.counting()));
        var scopeMap = all.stream().collect(Collectors.groupingBy(Report::getScope, Collectors.counting()));

        return new DashboardDTO(count,total, statusMap, scopeMap);
    }

    // Report Logic with DTO conversion
    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    

    public ReportDTO saveReport(Report report) {
        return mapToDTO(reportRepository.save(report));
    }

    
    public List<ReportDTO> getByTime(LocalDate start, LocalDate end) {
        return reportRepository.findByGeneratedDateBetween(start, end).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    
    public List<ReportDTO> getByStatus(String status) {
        return reportRepository.findByStatus(status).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    
    public List<ReportDTO> getByScope(String scope) {
        return reportRepository.findByScope(scope).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}
















































/*package com.financegov.analytics.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.financegov.analytics.model.Report;
import com.financegov.analytics.repository.ReportRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }
    
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public List<Report> getByTime(LocalDate start, LocalDate end) {
        return reportRepository.findByGeneratedDateBetween(start, end);
    }

    public List<Report> getByStatus(String status) {
        return reportRepository.findByStatus(status);
    }

    public List<Report> getByScope(String scope) {
        return reportRepository.findByScope(scope);
    }
}
*/

