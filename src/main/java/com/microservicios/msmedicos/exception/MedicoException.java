package com.microservicios.msmedicos.exception;

public class MedicoException extends RuntimeException {
    
    public MedicoException(String message) {
        super(message);
    }
    
    public MedicoException(String message, Throwable cause) {
        super(message, cause);
    }
}
