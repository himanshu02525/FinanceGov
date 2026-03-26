
package com.financegov.service;

import java.util.ArrayList;
import java.util.List;

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

	private final ResourceRepository resourceRepository;

	private ResourceStatus mapStatus(String status) {
		try {
			return ResourceStatus.valueOf(status.toUpperCase());
		} catch (Exception ex) {
			throw new InvalidResourceStatusException("Resource status must be AVAILABLE or UTILIZED");
		}
	}

	@Override
	public ResourceResponseDTO createResource(ResourceRequestDTO dto) {
		Resource resource = Resource.builder().programId(dto.getProgramId()).type(dto.getType())
				.quantity(dto.getQuantity()).status(mapStatus(dto.getStatus())).build();

		Resource saved = resourceRepository.save(resource);

		return ResourceResponseDTO.builder().resourceId(saved.getResourceId()).programId(saved.getProgramId())
				.type(saved.getType()).quantity(saved.getQuantity()).status(saved.getStatus().name()).build();
	}

	@Override
	public List<ResourceResponseDTO> getResourcesByProgramId(Long programId) {
		List<ResourceResponseDTO> responseList = new ArrayList<>();

		for (Resource resource : resourceRepository.findByProgramId(programId)) {
			responseList.add(ResourceResponseDTO.builder().resourceId(resource.getResourceId())
					.programId(resource.getProgramId()).type(resource.getType()).quantity(resource.getQuantity())
					.status(resource.getStatus().name()).build());
		}
		return responseList;
	}

	@Override
	public List<ResourceResponseDTO> getAllocatedResources() {
		List<ResourceResponseDTO> responseList = new ArrayList<>();

		for (Resource resource : resourceRepository.findByStatus(ResourceStatus.UTILIZED)) {
			responseList.add(ResourceResponseDTO.builder().resourceId(resource.getResourceId())
					.programId(resource.getProgramId()).type(resource.getType()).quantity(resource.getQuantity())
					.status(resource.getStatus().name()).build());
		}
		return responseList;
	}

	@Override
	public String deleteResource(Long resourceId) {
		resourceRepository.deleteById(resourceId);
		return "Resource deleted successfully";
	}
}
