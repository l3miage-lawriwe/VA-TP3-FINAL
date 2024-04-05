package fr.uga.l3miage.spring.tp3.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TestCenterResponseDTO {
    @Schema(description = "resulat requette ")
    private Boolean worked;
}
