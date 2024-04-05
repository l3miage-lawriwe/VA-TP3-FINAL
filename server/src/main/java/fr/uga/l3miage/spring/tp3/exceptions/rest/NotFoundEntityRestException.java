package fr.uga.l3miage.spring.tp3.exceptions.rest;

import lombok.Getter;

@Getter
public class NotFoundEntityRestException extends RuntimeException{

    public NotFoundEntityRestException(String message) {
        super(message);
    }
}
