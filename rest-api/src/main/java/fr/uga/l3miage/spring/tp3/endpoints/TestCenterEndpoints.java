package fr.uga.l3miage.spring.tp3.endpoints;

import fr.uga.l3miage.spring.tp3.errors.AddTestCollectionError;
import fr.uga.l3miage.spring.tp3.errors.ajoutEtudiantTestCenterBadrequest;
import fr.uga.l3miage.spring.tp3.responses.TestCenterResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "gestion des testCenters")
@RestController
@RequestMapping("/api/testCenters")
public interface TestCenterEndpoints {


    @Operation(description = "Ajouter une collection d'étudiants à un testCenter")
    @ApiResponse(responseCode = "202",description = "La collection à été ajouté au test center")
    @ApiResponse(responseCode = "404", description = "Une erreur c'est produit, le centre de test ou l'étudiant n'a pas été trouvé",content = @Content(schema = @Schema(implementation = AddTestCollectionError.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "mauvaise requette",content = @Content(schema = @Schema(implementation = ajoutEtudiantTestCenterBadrequest.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{testCenterId}/add")
    TestCenterResponseDTO addEtudiantInTestCenter(@PathVariable(name = "testCenterId")Long idTestcenter, @RequestParam Long idEtudiant);
}
