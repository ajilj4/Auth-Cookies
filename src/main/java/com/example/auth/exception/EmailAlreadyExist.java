package com.example.auth.exception;

import lombok.Getter;

@Getter
public class EmailAlreadyExist extends RuntimeException {

    public EmailAlreadyExist(String email){
        super("Email all ready exist : " +email);
    }
}
