package br.com.visitsafe.dto.release;

import java.time.OffsetDateTime;
import java.util.UUID;

import br.com.visitsafe.model.enums.ReleaseTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseResponseDTO{
    UUID id;
    OffsetDateTime validFrom;
    OffsetDateTime validUntil;
    ReleaseTypeEnum releaseType;
}
