package com.bank.pixkeysmanager.keymanager.domain.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class KeyManagerQueryWithoutResultsException extends KeyManagerValidationException {
    public KeyManagerQueryWithoutResultsException() {
        super(Set.of("Não foram encontrados reqistros com as parâmetros de busca informados."));
    }
}
