package fr.uga.l3miage.spring.tp3.controllers;




import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.CandidatNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.ModificationSessionRestException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import fr.uga.l3miage.spring.tp3.services.CandidateService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @SpyBean
    //@MockBean
    private CandidateService candidateService;


    // pour refresh après chaque test
    @AfterEach
    public void clear() {
        candidateRepository.deleteAll();
    }





    //tentative de test
    // je N'arrive pas a faire fonctionner les insertions dans les repositories

    @Test
    void testaverage(){
        //given
        final HttpHeaders headers = new HttpHeaders();

        ExamEntity exam1 = ExamEntity
                .builder()
                .id((long)5)
                .weight(1)
                .build();


        ExamEntity exam2 = ExamEntity
                .builder()
                .id((long)4)
                .weight(1)
                .build();

        //given
        CandidateEvaluationGridEntity grid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(5)
                .sheetNumber((long)3)
                .examEntity(exam1)
                .build();

        CandidateEvaluationGridEntity grid2 = CandidateEvaluationGridEntity
                .builder()
                .sheetNumber((long)2)
                .grade(15)
                .examEntity(exam2)
                .build();



        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .id((long)1)
                .firstname("test_man_1_firstname")
                .email("test@gmail")
                .candidateEvaluationGridEntities(Set.of(grid1,grid2))
                .build();

        examRepository.save(exam1);
        examRepository.save(exam2);
        candidateEvaluationGridRepository.save(grid1);
        candidateEvaluationGridRepository.save(grid2);
        candidateRepository.save(candidateEntity);


        //when
        Double doubleDonne = testRestTemplate.exchange("/api/candidates/{candidateId}/average", HttpMethod.GET, new HttpEntity<>(null, headers) ,Double.class,(long)1).getBody();

        //then
        assertThat(doubleDonne).isEqualTo((double)10);

    }


    //Version du test avec un mockbean
    // remplacer le spybean de candidateService par un mockBean pour utiliser
    @Test
    void testaverageMockBean(){
        //given
        final HttpHeaders headers = new HttpHeaders();

        Long testId = (long) 1;

        Double testResult = (double) 1;
        when(candidateService.getCandidateAverage(any(Long.class))).thenReturn(testResult);



        //when
        ResponseEntity<Double> doubleDonne = testRestTemplate.exchange("/api/candidates/{candidateId}/average", HttpMethod.GET, new HttpEntity<>(null, headers) ,Double.class,testId);

        //then
        assertThat(doubleDonne.getBody()).isEqualTo(testResult);

    }



    @Test
    void testaverageError() {
        //given
        final HttpHeaders headers = new HttpHeaders();

        //when
        ResponseEntity<CandidatNotFoundResponse> response = testRestTemplate.exchange("/api/candidates/{candidateId}/average", HttpMethod.GET, new HttpEntity<>(null, headers) ,CandidatNotFoundResponse.class,(long)1);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
