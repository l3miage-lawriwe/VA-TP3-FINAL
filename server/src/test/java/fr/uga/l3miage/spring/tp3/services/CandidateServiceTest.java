package fr.uga.l3miage.spring.tp3.services;



import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.ExamMapper;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateServiceTest {
    @Autowired
    private CandidateService candidateService;

    @MockBean
    private CandidateComponent candidateComponent;


    @Test
    void testGetCandidateAverage() throws CandidateNotFoundException {


        ExamEntity exam1 = ExamEntity
                .builder()
                .weight(1)
                .build();


        ExamEntity exam2 = ExamEntity
                .builder()
                .weight(1)
                .build();

        //given
        CandidateEvaluationGridEntity grid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(5)
                .examEntity(exam1)
                .build();

        CandidateEvaluationGridEntity grid2 = CandidateEvaluationGridEntity
                .builder()
                .grade(15)
                .examEntity(exam2)
                .build();



        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("test_man_1_firstname")
                .email("test@gmail")
                .candidateEvaluationGridEntities(Set.of(grid1,grid2))
                .build();
        when(candidateComponent.getCandidatById(anyLong())).thenReturn(candidateEntity);


        //when
        Double reponseDeService = candidateService.getCandidateAverage((long)1);

        //then
        assertThat(reponseDeService).isNotNaN();
        assertThat(reponseDeService).isEqualTo(10);

    }


    // TEST POUR EXEPTION
    @Test
    void testGetCandidateAverageNOTFOUND() throws CandidateNotFoundException {

        when(candidateComponent.getCandidatById(anyLong())).thenThrow(new CandidateNotFoundException(String.format("Le candidat [%s] n'a pas été trouvé",(long)1),(long)1));
        //when -then
        assertThrows(CandidateNotFoundRestException.class,()->candidateService.getCandidateAverage((long)1));

    }
}
