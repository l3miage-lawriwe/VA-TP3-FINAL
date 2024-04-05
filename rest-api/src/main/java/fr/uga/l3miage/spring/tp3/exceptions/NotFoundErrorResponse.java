package fr.uga.l3miage.spring.tp3.exceptions;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotFoundErrorResponse {
    @Schema(description = "end point call", example = "/api/drone/")
    private final String uri;
    @Schema(description = "error message", example = "n'existe pas")
    private final String errorMessage;
}
