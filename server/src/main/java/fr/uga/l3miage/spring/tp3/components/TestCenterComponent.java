package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.repositories.TestCenterRepository;


import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestCenterComponent {
    private final TestCenterRepository testCenterRepository;

    public TestCenterEntity addCandidate(Long idTestcenter, CandidateEntity candidateEntity) throws TestCenterNotFoundException {
        TestCenterEntity testCentertEntity = testCenterRepository.findById(idTestcenter)
                .orElseThrow(() -> new TestCenterNotFoundException(String.format("Le centre de test %s n'a pas été trouvé", idTestcenter)));
        // apparament sa marche pas ici
        //java.lang.UnsupportedOperationException
        // POURQUOI ? JE VEUT AJOUTER UNE ENTITE A UN SET PUTAIN
        testCentertEntity.getCandidateEntities().add(candidateEntity);
        return testCenterRepository.save(testCentertEntity);
    }

}
