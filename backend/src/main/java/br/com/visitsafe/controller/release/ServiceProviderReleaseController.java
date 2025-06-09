package br.com.visitsafe.controller.release;

import br.com.visitsafe.dto.release.ReleaseResponseDTO;
import br.com.visitsafe.dto.release.ServiceProviderReleaseCreateRequestDTO;
import br.com.visitsafe.dto.release.ServiceProviderReleaseUpdateRequestDTO;
import br.com.visitsafe.model.enums.ReleaseTypeEnum;
import br.com.visitsafe.model.release.ServiceProviderRelease;
import br.com.visitsafe.service.release.ServiceProviderReleaseApprovalService;
import br.com.visitsafe.service.release.ServiceProviderReleaseCreateService;
import br.com.visitsafe.service.release.ServiceProviderReleaseUpdateService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/releases/service-provider")
@RequiredArgsConstructor
public class ServiceProviderReleaseController {

    private final ServiceProviderReleaseCreateService releaseService;
    private final ServiceProviderReleaseUpdateService updateService;
    private final ServiceProviderReleaseApprovalService approvalService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ReleaseResponseDTO createServiceProviderRelease(@RequestBody ServiceProviderReleaseCreateRequestDTO dto) {
        ServiceProviderRelease serviceProviderRelease = releaseService.create(dto);
        return new ReleaseResponseDTO(
            serviceProviderRelease.getId(),
            serviceProviderRelease.getValidFrom(),
            serviceProviderRelease.getValidUntil(),
            ReleaseTypeEnum.SERVICEPROVIDER
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ReleaseResponseDTO updateServiceProviderRelease(@PathVariable UUID id,
                                     @RequestBody ServiceProviderReleaseUpdateRequestDTO dto) {
        ServiceProviderRelease updatServiceProviderRelease = updateService.update(id, dto);
        return new ReleaseResponseDTO(updatServiceProviderRelease.getId(), updatServiceProviderRelease.getValidFrom(), updatServiceProviderRelease.getValidUntil(), ReleaseTypeEnum.SERVICEPROVIDER);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public void deleteServiceProviderRelease(@PathVariable UUID id) {
        updateService.delete(id);
    }

    // Aprovação pelo morador (OWNER)
    @PutMapping("/{id}/approval")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> approveServiceProviderRelease(
        @PathVariable UUID id,
        @RequestParam boolean approve
    ) {
        approvalService.approve(id, approve);
        return ResponseEntity.ok().build();
    }

    // Justificativa pelo prestador (SERVICE_PROVIDER)
    @PutMapping("/{id}/justification")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<Void> justifyServiceProviderRelease(
        @PathVariable UUID id,
        @RequestBody String justification
    ) {
        approvalService.submitJustification(id, justification);
        return ResponseEntity.ok().build();
}
}
