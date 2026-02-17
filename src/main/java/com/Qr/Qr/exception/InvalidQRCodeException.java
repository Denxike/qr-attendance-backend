package com.Qr.Qr.exception;

public class InvalidQRCodeException extends RuntimeException{
    public InvalidQRCodeException(String message){
        super(message);
    }
}
