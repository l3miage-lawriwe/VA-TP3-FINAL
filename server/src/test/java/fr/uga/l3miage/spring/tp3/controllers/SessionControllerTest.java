package fr.uga.l3miage.spring.tp3.controllers;





import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.exceptions.CandidatNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.ModificationSessionRestException;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import fr.uga.l3miage.spring.tp3.services.CandidateService;
import fr.uga.l3miage.spring.tp3.services.SessionService;
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

public class SessionControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EcosSessionRepository ecosSessionRepository;

    @SpyBean
    private SessionComponent sessionComponent;

    @SpyBean
    private SessionService sessionService;

    // pour refresh après chaque test
    @AfterEach
    public void clear() {
        ecosSessionRepository.deleteAll();
    }



    // test de creation qui marche
    @Test
    void testCreateSession(){
        //given
        final HttpHeaders headers = new HttpHeaders();

        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequests = SessionProgrammationStepCreationRequest
                .builder()
                .id((long)1)
                .description("desc test")
                .build();



        SessionProgrammationCreationRequest sessionProgramation = SessionProgrammationCreationRequest
                .builder()
                .id((long)2)
                .steps(Set.of(sessionProgrammationStepCreationRequests))
                .build();

        SessionCreationRequest sessioncreationRequest = SessionCreationRequest
                .builder()
                .examsId(Set.of())
                .ecosSessionProgrammation(sessionProgramation)
                .name("test")
                .build();


        // when
        ResponseEntity<SessionResponse> response = testRestTemplate.exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(sessioncreationRequest, headers), SessionResponse.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(ecosSessionRepository.count()).isEqualTo(1);
        verify(sessionComponent, times(1)).createSession(any(EcosSessionEntity.class));
    }

    @Test
    void testModifySession(){
        //given

        final HttpHeaders headers = new HttpHeaders();



        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequests = SessionProgrammationStepCreationRequest
                .builder()
                .id((long)1)
                .description("desc test")
                .build();



        SessionProgrammationCreationRequest sessionProgramation = SessionProgrammationCreationRequest
                .builder()
                .id((long)2)
                .steps(Set.of(sessionProgrammationStepCreationRequests))
                .build();

        SessionCreationRequest sessioncreationRequest = SessionCreationRequest
                .builder()
                .examsId(Set.of())
                .ecosSessionProgrammation(sessionProgramation)
                .name("test")

                .build();


        ResponseEntity<SessionResponse> test = testRestTemplate.exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(sessioncreationRequest, headers), SessionResponse.class);

        Long testId = test.getBody().getId();

        // when
        ResponseEntity<SessionResponse> response = testRestTemplate.exchange("/api/sessions/modify", HttpMethod.POST, new HttpEntity<>(testId, headers), SessionResponse.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ecosSessionRepository.getOne(testId).getStatus()).isEqualTo(SessionStatus.EVAL_ENDED);

    }



    // problème sur se test
    //vient probablement de la manière avec laquelle j'ai créé le endpoint
    @Test
    void testModifySessionErrorConflict(){
        //given

        final HttpHeaders headers = new HttpHeaders();



        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequests = SessionProgrammationStepCreationRequest
                .builder()
                .id((long)1)
                .description("desc test")
                .build();



        SessionProgrammationCreationRequest sessionProgramation = SessionProgrammationCreationRequest
                .builder()
                .id((long)2)
                .steps(Set.of(sessionProgrammationStepCreationRequests))
                .build();

        SessionCreationRequest sessioncreationRequest = SessionCreationRequest
                .builder()
                .examsId(Set.of())
                .ecosSessionProgrammation(sessionProgramation)
                .name("test")

                .build();






        ResponseEntity<SessionResponse> test = testRestTemplate.exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(sessioncreationRequest, headers), SessionResponse.class);

        Long testId = test.getBody().getId();

        ResponseEntity<SessionResponse> response = testRestTemplate.exchange("/api/sessions/modify", HttpMethod.POST, new HttpEntity<>(testId, headers), SessionResponse.class);

        // when
        ResponseEntity<SessionResponse> response2 = testRestTemplate.exchange("/api/sessions/modify", HttpMethod.POST, new HttpEntity<>(testId, headers), SessionResponse.class);

        //then
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

    }


    //meme problème ici
    @Test
    void testModifyErrorNotFound(){
        final HttpHeaders headers = new HttpHeaders();
        //given
        Long testId = (long)1;

        // when
        ResponseEntity<SessionResponse> response2 = testRestTemplate.exchange("/api/sessions/modify", HttpMethod.POST, new HttpEntity<>(testId, headers), SessionResponse.class);

        //then
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }


}
