package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ExamComponentTest {
    @Autowired
    private ExamComponent examComponent;
    @MockBean
    private ExamRepository examRepository;


    // TESTS DE getAllById
    @Test
    void testgetAllByIdFound() {
        //given
        ExamEntity examEntity = ExamEntity
                .builder()
                .build();

        List<ExamEntity> examEntities = new ArrayList<>();
        examEntities.add(examEntity);
        when(examRepository.findAllById(anyIterable())).thenReturn(examEntities);

        // when - then
        assertDoesNotThrow(()->examComponent.getAllById(Set.of((long)1)));
    }

    @Test
    void testgetAllByIdNotFound() {
        //given
        when(examRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when - then
        assertThrows(ExamNotFoundException.class,()->examComponent.getAllById(Set.of((long)1)));
    }
}
