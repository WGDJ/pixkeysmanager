package com.bank.pixkeysmanager.keymanager.adapters.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CreateKeyResponse {

    @JsonProperty("id")
    private final UUID id;

    CreateKeyResponse(@JsonProperty("id") final String id) {
        this.id = UUID.fromString(id);
    }

    public static CreateKeyResponse of(final UUID id) {
        return CreateKeyResponse.builder().id(id).build();
    }

}
