package com.bank.pixkeysmanager.keymanager.domain.model.account;

import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerValidationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

@Getter
public enum HolderType {
    @JsonProperty("F")
    FISICA("F"),
    @JsonProperty("J")
    JURIDICA("J");
    private final String value;

    HolderType(final String value) {
        this.value = value;
    }

    public static  HolderType getByValue(final String value) {
        return Arrays.stream(HolderType.values())
                .filter(type -> type.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new KeyManagerValidationException(Set.of("Não existe entrada na enum HolderType com valor: " + value)));
    }
}
