package com.quark.equipocinco.topictwisterbackend.exception;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class PlayerException extends Exception{

    protected int idError;
    protected String error;

    public PlayerException() {}

    public PlayerException(String message) {
        super(message);
    }
}
