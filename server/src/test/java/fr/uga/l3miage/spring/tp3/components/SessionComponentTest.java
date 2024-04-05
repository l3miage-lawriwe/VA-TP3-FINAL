package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.models.*;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
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
public class SessionComponentTest {
    @Autowired
    private SessionComponent sessionComponent;
    @MockBean
    private EcosSessionRepository sessionRepository;

    @Test
    void TestcreateSession(){

        //given

        EcosSessionProgrammationStepEntity sessionProgrammationStepEntity = EcosSessionProgrammationStepEntity
                .builder()
                .id((long)1)
                .description("description test")
                .build();



        EcosSessionProgrammationEntity sessionProgramation = EcosSessionProgrammationEntity
                .builder()
                .id((long)2)
                .ecosSessionProgrammationStepEntities(Set.of(sessionProgrammationStepEntity))
                .build();

        EcosSessionEntity sessionEntity = EcosSessionEntity
                .builder()
                .examEntities(Set.of())
                .ecosSessionProgrammationEntity(sessionProgramation)
                .name("test")
                .build();

        when(sessionRepository.save(any(EcosSessionEntity.class))).thenReturn(sessionEntity);


        //when
        EcosSessionEntity sessionEntityVoulue = sessionComponent.createSession(sessionEntity);


        //then
        assertThat(sessionEntityVoulue).isEqualTo(sessionEntity);
    }

    /*
    @Test
    void TestcreateSessionError(){

        // la fonction n'a pas de throw

    }  */


    @Test
    void testGetSessionByIdFound(){
        //given
        EcosSessionEntity sessionEntity = EcosSessionEntity
                .builder()
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(sessionEntity));

        // when - then
        assertDoesNotThrow(()->sessionComponent.getById((long)1));
    }


    @Test
    void testGetSessionByIdNotFound(){
        //given
        EcosSessionEntity sessionEntity = EcosSessionEntity
                .builder()
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when - then
        assertThrows(SessionNotFoundException.class,()->sessionComponent.getById((long)1));
    }

}
