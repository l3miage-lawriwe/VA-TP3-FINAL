package fr.uga.l3miage.spring.tp3.services;


import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.components.TestCenterComponent;
import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.exceptions.rest.AddingCandidateRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.ExamMapper;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.responses.TestCenterResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.*;

import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TestCenterServiceTest {
    @Autowired
    private TestCenterService testCenterService;

    @MockBean
    private TestCenterComponent testCenterComponent;

    @MockBean
    private CandidateComponent candidateComponent;

    @Test
    void TestCenterComponentTest() throws CandidateNotFoundException, TestCenterNotFoundException {

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

        when(candidateComponent.getCandidatById(anyLong())).thenReturn(candidateEntity);
        when(testCenterComponent.addCandidate(anyLong(), any(CandidateEntity.class))).thenReturn(testCenterEntity);

        TestCenterResponseDTO testCenterResponseDTO = new TestCenterResponseDTO();
        testCenterResponseDTO.setWorked(true);

        //when
        TestCenterResponseDTO testCenterResponseDTOAttendue = testCenterService.addEtudiantInTestCenter((long)1,(long)1);


        //then
        assertThat(testCenterResponseDTOAttendue).isEqualTo(testCenterResponseDTO);
    }

    @Test
    void TestCenterComponentTestErrorCandidate() throws CandidateNotFoundException, TestCenterNotFoundException {

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

        when(candidateComponent.getCandidatById(anyLong())).thenThrow(new CandidateNotFoundException(String.format("Le centre de test %s n'a pas été trouvé", (long)1),(long)1));
        when(testCenterComponent.addCandidate(anyLong(), any(CandidateEntity.class))).thenReturn(testCenterEntity);

        //when - then
        assertThrows(AddingCandidateRestException.class,()->testCenterService.addEtudiantInTestCenter((long)1,(long)1));


    }

    @Test
    void TestCenterComponentTestErrorTestCenter() throws CandidateNotFoundException, TestCenterNotFoundException {

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

        when(candidateComponent.getCandidatById(anyLong())).thenReturn(candidateEntity);
        when(testCenterComponent.addCandidate(anyLong(), any(CandidateEntity.class))).thenThrow(new TestCenterNotFoundException(String.format("Le centre de test %s n'a pas été trouvé", (long)1)));

        //when - then
        assertThrows(AddingCandidateRestException.class,()->testCenterService.addEtudiantInTestCenter((long)1,(long)1));


    }
}
