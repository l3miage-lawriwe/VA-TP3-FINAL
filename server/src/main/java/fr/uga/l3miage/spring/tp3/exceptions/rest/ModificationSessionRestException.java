package fr.uga.l3miage.spring.tp3.exceptions.rest;

import lombok.Getter;

@Getter
public class ModificationSessionRestException extends RuntimeException {
    private final Long sessionId;

    public ModificationSessionRestException(String message, Long sessionId) {
        super(message);
        this.sessionId = sessionId;
    }
}
