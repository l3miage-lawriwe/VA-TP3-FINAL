package fr.uga.l3miage.spring.tp3.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddTestCollectionError {

    @Schema(description = "end point call", example = "/api/drone/")
    private final String uri;
    @Schema(description = "error message", example = "Pas touvé")
    private final String errorMessage;
}

