package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.ModificationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.ExamMapper;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.*;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
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
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;


    @MockBean
    private SessionComponent sessionComponent;

    @SpyBean
    private ExamMapper examMapper;

    @SpyBean
    private SessionMapper sessionMapper;


    @Test
    void testCreateSession(){

        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequests = SessionProgrammationStepCreationRequest
                .builder()
                .id((long)1)
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
        EcosSessionEntity ecosSessionEntity = sessionMapper.toEntity(sessioncreationRequest);
        when(sessionComponent.createSession(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);

        SessionResponse responseExpected = sessionMapper.toResponse(ecosSessionEntity);

        //when
        SessionResponse response = sessionService.createSession(sessioncreationRequest);


        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
        verify(sessionMapper, times(2)).toEntity(sessioncreationRequest);
        verify(sessionMapper, times(2)).toResponse(same(ecosSessionEntity));
    }


    @Test
    void testCreateSessionThrowError(){

        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequests = SessionProgrammationStepCreationRequest
                .builder()
                .id((long)1)
                .build();



        SessionProgrammationCreationRequest sessionProgramation = SessionProgrammationCreationRequest
                .builder()
                .id((long)2)
                .steps(Set.of(sessionProgrammationStepCreationRequests))
                .build();

        SessionCreationRequest sessioncreationRequest = SessionCreationRequest
                .builder()
                .ecosSessionProgrammation(sessionProgramation)
                .name("test")
                .build();
        EcosSessionEntity ecosSessionEntity = sessionMapper.toEntity(sessioncreationRequest);
        when(sessionComponent.createSession(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);

        //when-then
        assertThrows(CreationSessionRestException.class,()->sessionService.createSession(sessioncreationRequest));

    }


    @Test
    void testmodifyStateSession() throws SessionNotFoundException {

        //given
        EcosSessionEntity sessionEntity = EcosSessionEntity
                .builder()
                .status(SessionStatus.EVAL_STARTED)
                .name("test")
                .build();

        EcosSessionEntity sessionEntity2 = EcosSessionEntity
                .builder()
                .status(SessionStatus.EVAL_ENDED)
                .name("test")
                .build();

        when(sessionComponent.getById(anyLong())).thenReturn(sessionEntity);


        SessionResponse responseExpected = sessionMapper.toResponse(sessionEntity2);

        //when
        SessionResponse response = sessionService.modifyStateSession((long)1);


        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);

    }

    @Test
    void testmodifyStateSessionErrorNotFound() throws SessionNotFoundException {

        //given
        EcosSessionEntity sessionEntity = EcosSessionEntity
                .builder()
                .status(SessionStatus.EVAL_STARTED)
                .name("test")
                .build();

        EcosSessionEntity sessionEntity2 = EcosSessionEntity
                .builder()
                .status(SessionStatus.EVAL_ENDED)
                .name("test")
                .build();

        when(sessionComponent.getById(anyLong())).thenThrow(new SessionNotFoundException(String.format("La session %s n'a pas été trouvé", (long)1),(long)1));


        SessionResponse responseExpected = sessionMapper.toResponse(sessionEntity2);

        //when - then
        assertThrows(ModificationSessionRestException.class,()->sessionService.modifyStateSession((long)1));


    }

    @Test
    void testmodifyStateSessionErrorWrongStatus() throws SessionNotFoundException {

        //given
        EcosSessionEntity sessionEntity = EcosSessionEntity
                .builder()
                .status(SessionStatus.CORRECTED)
                .name("test")
                .build();

        EcosSessionEntity sessionEntity2 = EcosSessionEntity
                .builder()
                .status(SessionStatus.EVAL_ENDED)
                .name("test")
                .build();

        when(sessionComponent.getById(anyLong())).thenReturn(sessionEntity);

        SessionResponse responseExpected = sessionMapper.toResponse(sessionEntity2);

        //when - then
        assertThrows(ModificationSessionRestException.class,()->sessionService.modifyStateSession((long)1));


    }

}
