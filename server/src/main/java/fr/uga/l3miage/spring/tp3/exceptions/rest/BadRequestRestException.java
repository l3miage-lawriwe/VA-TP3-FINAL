package fr.uga.l3miage.spring.tp3.exceptions.rest;

public class BadRequestRestException extends RuntimeException{
    public BadRequestRestException(String message) {
        super(message);
    }
}
