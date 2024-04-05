package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.components.TestCenterComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.AddingCandidateRestException;
import fr.uga.l3miage.spring.tp3.mappers.TestCenterMapper;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.responses.TestCenterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;

@Service
@RequiredArgsConstructor
public class TestCenterService {
    private final TestCenterComponent testCenterComponent;
    private final CandidateComponent candidateComponent;
    private final TestCenterMapper testCenterMapper;

    public TestCenterResponseDTO addEtudiantInTestCenter(Long idTestcenter, Long idEtudiant){
        try {
            CandidateEntity candidateEntity = candidateComponent.getCandidatById(idEtudiant);
            TestCenterEntity testCenterEntity = testCenterComponent.addCandidate(idTestcenter, candidateEntity);

            //
            TestCenterResponseDTO testCenterResponseDTO = new TestCenterResponseDTO();
            testCenterResponseDTO.setWorked(true);
            return testCenterResponseDTO;
        } catch (CandidateNotFoundException | TestCenterNotFoundException e) {
            throw new AddingCandidateRestException(e.getMessage());
        }
    }
}
