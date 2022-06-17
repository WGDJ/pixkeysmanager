package com.bank.pixkeysmanager.keymanager.adapters.api.response;

import com.bank.pixkeysmanager.keymanager.domain.model.key.Key;
import com.bank.pixkeysmanager.keymanager.domain.model.key.KeyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UpdateKeyResponse {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("tipoChave")
    private final KeyType keyType;

    @JsonProperty("valorChave")
    private final String keyValue;

    public static UpdateKeyResponse of(final Key key) {
        return UpdateKeyResponse.builder()
                .id(key.getId())
                .keyType(key.getType())
                .keyValue(key.getValue())
                .build();
    }
}
