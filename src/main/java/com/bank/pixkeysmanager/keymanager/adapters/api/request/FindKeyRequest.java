package com.bank.pixkeysmanager.keymanager.adapters.api.request;

import com.bank.pixkeysmanager.keymanager.domain.model.key.KeyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class FindKeyRequest {

    @JsonProperty("id")
    @Pattern(regexp="^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$", message="O campo 'id' é inválido.")
    private final String id;

    @JsonProperty("tipoChave")
    @Pattern(regexp = "CELULAR|EMAIL|CPF|CNPJ|ALEATORIO", message = "O campo 'tipoChave' deve conter um dos seguintes valores: [CELULAR, EMAIL, CPF, CNPJ, ALEATORIO] ")
    private final String keyType;

    @JsonProperty("agencia")
    @Pattern(regexp="[\\d]{1,4}", message="O campo 'agência' deve conter entre 1 e 4 dígitos.")
    private final String agencyNumber;

    @JsonProperty("conta")
    @Pattern(regexp="[\\d]{1,8}", message="O campo 'conta' deve conter entre 1 e 8 dígitos.")
    private final String accountNumber;

    @JsonProperty("nomeCorrentista")
    @Size(min = 3, max = 30, message = "O campo 'nome' deve ter entre 3 e 30 caracteres")
    private final String holderName;

    @JsonProperty("dataHoraInclusao")
    @Past(message = "A data deve ser no passado")
    private final Date includedTime;

    @JsonProperty("dataHoraInativacao")
    @Past(message = "A data deve ser no passado")
    private final Date deletedTime;
}
