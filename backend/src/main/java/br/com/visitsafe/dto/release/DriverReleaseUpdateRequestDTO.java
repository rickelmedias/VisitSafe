package br.com.visitsafe.dto.release;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class DriverReleaseUpdateRequestDTO {

    @NotNull
    @Schema(description = "ID da unidade", required = true)
    private UUID unitId;

    @NotNull
    @Schema(description = "Início da liberação", required = true)
    private OffsetDateTime validFrom;

    @NotNull
    @Schema(description = "Fim da liberação", required = true)
    private OffsetDateTime validUntil;

    @NotNull
    @Schema(description = "Horário de entrada permitido por dia", required = true)
    private OffsetDateTime dailyStart;

    @NotNull
    @Schema(description = "Horário de saída permitido por dia", required = true)
    private OffsetDateTime dailyEnd;
}
