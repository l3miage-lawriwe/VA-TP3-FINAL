package fr.uga.l3miage.spring.tp3.controllers;




import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.CandidatNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.ModificationSessionRestException;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
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

    @MockBean
    private CandidateService candidateService;


    // pour refresh après chaque test
    @AfterEach
    public void clear() {
        candidateRepository.deleteAll();
    }





    //tentative de test
    // j'ai l'erreur Not enough variable values available to expand 'candidateId'
    // il y a probablement une erreur dans la formation de la query mais j'arrive pas a fix

    @Test
    void testaverage(){
        //given
        final HttpHeaders headers = new HttpHeaders();


        Long testId = (long) 1;

        Double testResult = (double) 1;
        when(candidateService.getCandidateAverage(any(Long.class))).thenReturn(testResult);




        //when
        // l'erreur est très probablement sur cette ligne
        Double doubleDonne = testRestTemplate.exchange("/api/candidates/{candidateId}/average", HttpMethod.GET, new HttpEntity<>(testId, headers) ,Double.class).getBody();

        //then
        assertThat(doubleDonne).isEqualTo(testResult);

    }

    // meme probleme sur ce test
    // de plus ce test est incorrect : je devrait utiliser le spybean, mais j'ai déja fait un mockbean de CandidateService et j'ai oublié de demander a prof si on peut faire un spybean et un mockbean du même service
    @Test
    void testaverageError() {
        //given
        final HttpHeaders headers = new HttpHeaders();

        Long testId = (long) 1;

        when(candidateService.getCandidateAverage(any(Long.class))).thenThrow(new CandidateNotFoundRestException("candidat introvable", (long) 1));

        //when-then
        assertThrows(CandidateNotFoundRestException.class, () -> testRestTemplate.exchange("/api/candidates/{candidateId}/average", HttpMethod.GET, new HttpEntity<>(testId, headers), Double.class).getBody());

    }
}
