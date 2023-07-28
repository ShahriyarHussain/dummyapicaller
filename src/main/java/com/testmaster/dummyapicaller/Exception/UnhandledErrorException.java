package com.testmaster.dummyapicaller.Exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnhandledErrorException extends RuntimeException {

    public UnhandledErrorException(String message) {
        super(message);
    }
}