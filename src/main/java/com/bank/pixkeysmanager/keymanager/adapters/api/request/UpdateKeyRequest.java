package com.bank.pixkeysmanager.keymanager.adapters.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class UpdateKeyRequest {

    @JsonProperty("id")
    @NotEmpty(message = "O campo 'id' é obrigatório")
    @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$", message = "O campo 'id' é inválido.")
    private final String id;

    @JsonProperty("valorChave")
    @NotEmpty(message = "O campo 'valorChave' é obrigatório")
    @Size(min = 1, max = 77, message = "O campo 'valorChave' deve ter entre 1 e 77 caracteres")
    private final String keyValue;

}
