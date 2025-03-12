package com.nm.notesms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotesBadRequestException extends RuntimeException {

    public NotesBadRequestException(String message) {
        super(message);
    }

}
