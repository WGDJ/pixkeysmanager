package com.bank.pixkeysmanager.keymanager.domain.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class KeyManagerKeyNotFountException extends KeyManagerValidationException {
    public KeyManagerKeyNotFountException(final UUID keyId) {
        super(Set.of("Não foi encontrada a chave de id " + keyId));
    }
}
