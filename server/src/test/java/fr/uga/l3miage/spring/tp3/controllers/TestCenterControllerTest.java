package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.components.TestCenterComponent;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.repositories.TestCenterRepository;
import fr.uga.l3miage.spring.tp3.responses.TestCenterResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;


@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")

public class TestCenterControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TestCenterRepository testCenterRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @SpyBean
    private TestCenterComponent testCenterComponent;

    // pour refresh après chaque test
    @AfterEach
    public void clear() {
        testCenterRepository.deleteAll();
    }



    // problème sur test, need fix l'implémentation du endpoint
    @Test
    void testAddEtudiantInTestCenter() {

        //given
        final HttpHeaders headers = new HttpHeaders();



        TestCenterEntity testCenter = TestCenterEntity
                .builder()
                .id((long)1)
                .build();

        CandidateEntity candidate = CandidateEntity
                .builder()
                .id((long)2)
                .build();

        testCenterRepository.save(testCenter);
        candidateRepository.save(candidate);

        //when
        ResponseEntity<TestCenterResponseDTO> response = testRestTemplate.exchange("/api/testCenters/{testCenterId}/add", HttpMethod.POST, new HttpEntity<>((long)1, headers), TestCenterResponseDTO.class,(long)2);


        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(testCenterRepository.getOne((long)1).getCandidateEntities().size()).isEqualTo(1);

    }

    //même problême
    @Test

    void testAddEtudiantInTestCenterErrorTestCenterNotFount() {


        //given
        final HttpHeaders headers = new HttpHeaders();





        CandidateEntity candidate = CandidateEntity
                .builder()
                .id((long)2)
                .build();

        candidateRepository.save(candidate);

        //when
        ResponseEntity<TestCenterResponseDTO> response = testRestTemplate.exchange("/api/testCenters/{testCenterId}/add", HttpMethod.POST, new HttpEntity<>((long)1, headers), TestCenterResponseDTO.class,(long)2);


        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    //même problême
    @Test
    void testAddEtudiantInTestCenterErrorEtudiantNotFound() {
        //given
        final HttpHeaders headers = new HttpHeaders();



        TestCenterEntity testCenter = TestCenterEntity
                .builder()
                .id((long)1)
                .build();


        testCenterRepository.save(testCenter);

        //when
        ResponseEntity<TestCenterResponseDTO> response = testRestTemplate.exchange("/api/testCenters/{testCenterId}/add", HttpMethod.POST, new HttpEntity<>((long)1, headers), TestCenterResponseDTO.class,(long)2);


        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(testCenterRepository.getOne((long)1).getCandidateEntities().size()).isEqualTo(1);

    }

}

