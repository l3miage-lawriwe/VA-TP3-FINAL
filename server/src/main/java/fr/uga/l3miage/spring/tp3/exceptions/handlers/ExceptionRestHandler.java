package fr.uga.l3miage.spring.tp3.exceptions.handlers;

import fr.uga.l3miage.spring.tp3.errors.AddTestCollectionError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionRestHandler {

    @ExceptionHandler(fr.uga.l3miage.spring.tp3.exceptions.rest.AddingCandidateRestException.class)
    public ResponseEntity<AddTestCollectionError> handle(HttpServletRequest httpServletRequest, Exception e){
        fr.uga.l3miage.spring.tp3.exceptions.rest.AddingCandidateRestException exception = (fr.uga.l3miage.spring.tp3.exceptions.rest.AddingCandidateRestException) e;
        final AddTestCollectionError response = AddTestCollectionError
                .builder()
                .uri(httpServletRequest.getRequestURI())
                .errorMessage(exception.getMessage())
                .build();
        log.warn(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }


}
