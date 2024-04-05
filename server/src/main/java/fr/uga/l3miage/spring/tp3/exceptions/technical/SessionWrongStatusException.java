package fr.uga.l3miage.spring.tp3.exceptions.technical;

import lombok.Getter;

@Getter
public class SessionWrongStatusException extends Exception{
    private final Long sessionId;

    public SessionWrongStatusException(String message, Long sessionId) {
        super(message);
        this.sessionId = sessionId;
    }
}
