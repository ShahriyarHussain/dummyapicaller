package com.testmaster.dummyapicaller.Exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnhandledErrorException extends RuntimeException {

    private String message;

    public UnhandledErrorException(String message) {
        this.message = message;
    }
}