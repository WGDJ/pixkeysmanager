package com.bank.pixkeysmanager.keymanager.adapters.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public class CreateKeyResponse {

    @JsonProperty("id")
    private final UUID id;

    public static CreateKeyResponse of(final UUID id) {
        return CreateKeyResponse.builder().id(id).build();
    }

}
