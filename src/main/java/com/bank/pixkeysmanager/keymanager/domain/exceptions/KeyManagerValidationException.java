package com.bank.pixkeysmanager.keymanager.domain.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class KeyManagerValidationException extends RuntimeException {
    private static final long serialVersionUID = 6639782153590013636L;
    private Set<String> errors;

    public KeyManagerValidationException(final Set<String> errors){
        super();
        this.errors = errors;
    }
}
