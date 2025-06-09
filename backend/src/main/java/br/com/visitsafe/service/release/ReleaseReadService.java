package br.com.visitsafe.service.release;

import br.com.visitsafe.dto.release.ReleaseSummaryResponseDTO;
import br.com.visitsafe.model.condominium.Condominium;
import br.com.visitsafe.model.enums.ReleaseStatusEnum;
import br.com.visitsafe.model.release.Release;
import br.com.visitsafe.model.unit.Unit;
import br.com.visitsafe.model.user.EmployeeUser;
import br.com.visitsafe.model.user.User;
import br.com.visitsafe.repository.release.ReleaseRepository;
import br.com.visitsafe.repository.unit.UnitRepository;
import br.com.visitsafe.validation.impl.unit.UnitOwnershipValidator;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReleaseReadService {

    private final ReleaseRepository releaseRepository;
    private final UnitRepository unitRepository;
    private final UnitOwnershipValidator unitOwnershipValidator;

    public List<ReleaseSummaryResponseDTO> findByOwnerAndPeriod(User user, UUID propertyId, LocalDate startDate, LocalDate endDate) {
    Unit unit = unitRepository.findById(propertyId)
            .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada"));

    unitOwnershipValidator.validate(new UnitOwnershipValidator.OwnershipCheck(unit, user));

    OffsetDateTime startOfDay = startDate.atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
    OffsetDateTime endOfDay = endDate.plusDays(1).atStartOfDay().atOffset(OffsetDateTime.now().getOffset());

    return releaseRepository.findAllByUnitIdAndValidFromBetween(unit.getId(), startOfDay, endOfDay)
            .stream()
            .map(ReleaseSummaryResponseDTO::fromEntity)
            .toList();
    }
    
    public Page<ReleaseSummaryResponseDTO> findAllTodayByCondominiumAndStatus(EmployeeUser employee, ReleaseStatusEnum status, Pageable pageable) {
        Condominium condominium = employee.getCondominium();
        OffsetDateTime todayStart = LocalDate.now().atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime todayEnd = todayStart.plusDays(1);

        Page<Release> releases = releaseRepository.findByUnit_CondominiumAndStatusAndCreatedAtBetween(
                condominium, status, todayStart, todayEnd, pageable
        );

        return releases.map(ReleaseSummaryResponseDTO::fromEntity);
    }
}
