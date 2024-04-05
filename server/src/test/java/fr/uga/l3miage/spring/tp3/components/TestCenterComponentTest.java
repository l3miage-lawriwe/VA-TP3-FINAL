package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException;
import fr.uga.l3miage.spring.tp3.models.*;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import fr.uga.l3miage.spring.tp3.repositories.TestCenterRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TestCenterComponentTest {
    @Autowired
    private TestCenterComponent testCenterComponent;
    @MockBean
    private TestCenterRepository testCenterRepository;


    // PROBLEME DANS TESTCENTERCOMPONENT
    @Test
    void testaddCandidate() throws TestCenterNotFoundException {
        //given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("test_man_1_firstname")
                .email("test@gmail")
                .build();

        TestCenterEntity testCenterEntity = TestCenterEntity
                .builder()
                .candidateEntities(Set.of())
                .code(TestCenterCode.PAR)
                .build();


        when(testCenterRepository.findById(anyLong())).thenReturn(Optional.of(testCenterEntity));
        when(testCenterRepository.save(any(TestCenterEntity.class))).thenReturn(testCenterEntity);
        //when

        TestCenterEntity EestCenterEntityVoulue = testCenterComponent.addCandidate((long)1,candidateEntity);

        //then

        assertThat(EestCenterEntityVoulue).isEqualTo(testCenterEntity);

    }


    // PROBLEME ICI AUSSI
/*
    org.mockito.exceptions.base.MockitoException:
    Checked exception is invalid for this method!
    Invalid: fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException
 */
    // WTF ???
    @Test
    void addEtudiantInTestCenter() {
        //given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("test_man_1_firstname")
                .email("test@gmail")
                .build();

        TestCenterEntity testCenterEntity = TestCenterEntity
                .builder()
                .candidateEntities(Set.of())
                .code(TestCenterCode.PAR)
                .build();


        when(testCenterRepository.findById(anyLong())).thenThrow(new TestCenterNotFoundException(String.format("Le centre de test %s n'a pas été trouvé", (long)1)));

        //when - then
        assertThrows(TestCenterNotFoundException.class,()->testCenterComponent.addCandidate((long)1,candidateEntity));

    }

}
