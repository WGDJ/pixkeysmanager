package com.bank.pixkeysmanager.keymanager.domain.model.account;

import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerValidationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

@Getter
public enum AccountType {
    @JsonProperty("CORRENTE")
    CURRENT("CORRENTE"),
    @JsonProperty("POUPANCA")
    SAVINGS("POUPANCA");
    private final String value;

    AccountType(final String value) {
        this.value = value;
    }

    public static AccountType getByValue(final String value) {
        return Arrays.stream(AccountType.values())
                .filter(type -> type.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new KeyManagerValidationException(Set.of("Não existe entrada na enum AccountType com valor: " + value)));
    }
}
