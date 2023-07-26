package com.caller.dummyapicaller.Exception;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
public class UnhandledErrorException extends RuntimeException {

    public UnhandledErrorException(String message) {
        super(message);
    }
}
