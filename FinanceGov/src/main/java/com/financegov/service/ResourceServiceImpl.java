package com.financegov.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.financegov.dto.ResourceRequestDTO;
import com.financegov.dto.ResourceResponseDTO;
import com.financegov.enums.ResourceStatus;
import com.financegov.exceptions.InvalidResourceStatusException;
import com.financegov.model.Resource;
import com.financegov.repository.ResourceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
    private final ResourceRepository resourceRepository;

    private ResourceStatus mapStatus(String status) {
        logger.debug("Mapping resource status: {}", status);
        try {
            return ResourceStatus.valueOf(status.toUpperCase());
        } catch (Exception ex) {
            logger.error("Invalid resource status received: {}", status);
            throw new InvalidResourceStatusException("Resource status must be AVAILABLE or UTILIZED");
        }
    }

    @Override
    public ResourceResponseDTO createResource(ResourceRequestDTO dto) {
        logger.info("Creating resource for programId: {}", dto.getProgramId());

        Resource resource = Resource.builder()
                .programId(dto.getProgramId())
                .type(dto.getType())
                .quantity(dto.getQuantity())
                .status(mapStatus(dto.getStatus()))
                .build();

        Resource saved = resourceRepository.save(resource);

        logger.info("Resource created successfully with resourceId: {}", saved.getResourceId());

        return ResourceResponseDTO.builder()
                .resourceId(saved.getResourceId())
                .programId(saved.getProgramId())
                .type(saved.getType())
                .quantity(saved.getQuantity())
                .status(saved.getStatus().name())
                .build();
    }

    @Override
    public List<ResourceResponseDTO> getResourcesByProgramId(Long programId) {
        logger.info("Fetching resources for programId: {}", programId);

        List<ResourceResponseDTO> responseList = new ArrayList<>();

        for (Resource resource : resourceRepository.findByProgramId(programId)) {
            logger.debug("Processing resourceId: {}", resource.getResourceId());

            responseList.add(ResourceResponseDTO.builder()
                    .resourceId(resource.getResourceId())
                    .programId(resource.getProgramId())
                    .type(resource.getType())
                    .quantity(resource.getQuantity())
                    .status(resource.getStatus().name())
                    .build());
        }

        logger.info("Total resources fetched for programId {}: {}", programId, responseList.size());
        return responseList;
    }

    @Override
    public List<ResourceResponseDTO> getAllocatedResources() {
        logger.info("Fetching allocated (UTILIZED) resources");

        List<ResourceResponseDTO> responseList = new ArrayList<>();

        for (Resource resource : resourceRepository.findByStatus(ResourceStatus.UTILIZED)) {
            logger.debug("Allocated resource found: resourceId {}", resource.getResourceId());

            responseList.add(ResourceResponseDTO.builder()
                    .resourceId(resource.getResourceId())
                    .programId(resource.getProgramId())
                    .type(resource.getType())
                    .quantity(resource.getQuantity())
                    .status(resource.getStatus().name())
                    .build());
        }

        logger.info("Total allocated resources fetched: {}", responseList.size());
        return responseList;
    }

    @Override
    public String deleteResource(Long resourceId) {
        logger.info("Deleting resource with resourceId: {}", resourceId);

        resourceRepository.deleteById(resourceId);

        logger.info("Resource deleted successfully for resourceId: {}", resourceId);
        return "Resource deleted successfully";
    }
}