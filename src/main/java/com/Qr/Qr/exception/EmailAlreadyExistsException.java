package com.Qr.Qr.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String message){
        super(message);
    }
//    public EmailAlreadyExistsException(String email){
//        super("Email already exists: "+ email);
//    }
}
