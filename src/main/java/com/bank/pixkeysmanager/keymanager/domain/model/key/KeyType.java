package com.bank.pixkeysmanager.keymanager.domain.model.key;

import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerValidationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

@Getter
public enum KeyType {
    @JsonProperty("CELULAR")
    CELULAR("CELULAR"),
    @JsonProperty("EMAIL")
    EMAIL("EMAIL"),
    @JsonProperty("CPF")
    CPF("CPF"),
    @JsonProperty("CNPJ")
    CNPJ("CNPJ"),
    @JsonProperty("ALEATORIO")
    ALEATORIO("ALEATORIO");
    private final String value;

    KeyType(final String value) {
        this.value = value;
    }

    public static KeyType getByValue(final String value) {
        return Arrays.stream(KeyType.values())
                .filter(type -> type.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new KeyManagerValidationException(Set.of("Não existe entrada na enum KeyType com valor: " + value)));
    }

}
