package com.kuddy.common.exception.custom;

import org.springframework.http.HttpStatus;


public class ServerErrorException extends ApplicationException {

    public ServerErrorException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}