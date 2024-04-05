package fr.uga.l3miage.spring.tp3.components;


import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {
    @Autowired
    private CandidateComponent candidateComponent;
    @MockBean
    private CandidateRepository candidateRepository;



    @Test
    void testGetCandidatByIdFound() {
        //given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("test_man_1_firstname")
                .email("test@gmail")
                .build();
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidateEntity));

        // when - then
        assertDoesNotThrow(()->candidateComponent.getCandidatById((long)1));
    }

    @Test
    void testGetCandidatByIdNotFound() {
        //given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when - then
        assertThrows(CandidateNotFoundException.class,()->candidateComponent.getCandidatById((long)1));
    }


}
