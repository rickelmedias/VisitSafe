package br.com.visitsafe.service.release;

import br.com.visitsafe.model.enums.ReleaseApprovalStatusEnum;
import br.com.visitsafe.model.enums.ReleaseStatusEnum;
import br.com.visitsafe.model.release.ServiceProviderRelease;
import br.com.visitsafe.repository.release.ServiceProviderReleaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceProviderReleaseApprovalService {

    private final ServiceProviderReleaseRepository releaseRepo;

    public void approve(UUID id, boolean approve) {
        ServiceProviderRelease release = releaseRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Liberação não encontrada"));

        if (!Boolean.TRUE.equals(release.getIsExceptional())) {
            throw new IllegalStateException(
                "Somente liberações excepcionais requerem aprovação."
            );
        }

        release.setApprovalStatus(
            approve ? ReleaseApprovalStatusEnum.APPROVED : ReleaseApprovalStatusEnum.REJECTED
        );
        release.setStatus(
            approve ? ReleaseStatusEnum.PENDING_CHECKIN : ReleaseStatusEnum.UNAUTHORIZED
        );
        releaseRepo.save(release);
    }

    public void submitJustification(UUID id, String justification) {
        ServiceProviderRelease release = releaseRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Liberação não encontrada"));

        if (release.getApprovalStatus() != ReleaseApprovalStatusEnum.REJECTED) {
            throw new IllegalStateException(
                "Somente liberações rejeitadas aceitam justificativa"
            );
        }

        release.setRejectionJustification(justification);
        releaseRepo.save(release);
    }
}