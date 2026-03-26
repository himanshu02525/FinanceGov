package com.financegov.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financegov.dto.ResourceRequestDTO;
import com.financegov.dto.ResourceResponseDTO;
import com.financegov.service.ResourceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
    private final ResourceService resourceService;

    @PostMapping("/createResource")
    public ResponseEntity<ResourceResponseDTO> createResource(@Valid @RequestBody ResourceRequestDTO dto) {
        logger.info("Received request to create resource for programId: {}", dto.getProgramId());

        ResponseEntity<ResourceResponseDTO> response =
                new ResponseEntity<>(resourceService.createResource(dto), HttpStatus.CREATED);

        logger.info("Resource created successfully for programId: {}", dto.getProgramId());
        return response;
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<ResourceResponseDTO>> getResourcesByProgram(@PathVariable Long programId) {
        logger.info("Received request to fetch resources for programId: {}", programId);

        ResponseEntity<List<ResourceResponseDTO>> response =
                ResponseEntity.ok(resourceService.getResourcesByProgramId(programId));

        logger.info("Resources fetched successfully for programId: {}", programId);
        return response;
    }

    @GetMapping("/getAllallocated")
    public ResponseEntity<List<ResourceResponseDTO>> getAllocatedResources() {
        logger.info("Received request to fetch all allocated (UTILIZED) resources");

        ResponseEntity<List<ResourceResponseDTO>> response =
                ResponseEntity.ok(resourceService.getAllocatedResources());

        logger.info("Allocated resources fetched successfully");
        return response;
    }

    @DeleteMapping("deleteResource/{resourceId}")
    public ResponseEntity<Map<String, String>> deleteResource(@PathVariable Long resourceId) {
        logger.info("Received request to delete resource with resourceId: {}", resourceId);

        Map<String, String> response = new HashMap<>();
        response.put("message", resourceService.deleteResource(resourceId));

        logger.info("Resource deleted successfully with resourceId: {}", resourceId);
        return ResponseEntity.ok(response);
    }
}