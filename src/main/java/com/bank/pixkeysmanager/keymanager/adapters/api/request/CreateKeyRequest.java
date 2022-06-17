package com.bank.pixkeysmanager.keymanager.adapters.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class CreateKeyRequest {

    @JsonProperty("tipoChave")
    @NotEmpty(message = "O campo 'tipoChave' é obrigatório")
    @Pattern(regexp = "CELULAR|EMAIL|CPF|CNPJ|ALEATORIO", message = "O campo 'tipoChave' deve conter um dos seguintes valores: [CELULAR, EMAIL, CPF, CNPJ, ALEATORIO] ")
    private final String keyType;

    @JsonProperty("valorChave")
    @NotEmpty(message = "O campo 'valorChave' é obrigatório")
    @Size(min = 1, max = 77, message = "O campo 'valorChave' deve ter entre 1 e 77 caracteres")
    private final String keyValue;

    @JsonProperty("tipoConta")
    @NotEmpty(message = "O campo 'tipoConta' é obrigatório")
    @Pattern(regexp = "CORRENTE|POUPANCA", message = "O campo 'tipoConta' deve conter um dos seguintes valores: [CORRENTE, POUPANCA]. ")
    private final String accountType;

    @JsonProperty("agencia")
    @NotEmpty(message = "O campo 'agencia' é obrigatório")
    @Pattern(regexp="[\\d]{1,4}", message="O campo 'agência' deve conter entre 1 e 4 dígitos.")
    private final String agencyNumber;

    @JsonProperty("conta")
    @NotEmpty(message = "O campo 'conta' é obrigatório")
    @Pattern(regexp="[\\d]{1,8}", message="O campo 'conta' deve conter entre 1 e 8 dígitos.")
    private final String accountNumber;

    @JsonProperty("nomeCorrentista")
    @NotEmpty(message = "O campo 'nome' é obrigatório")
    @Size(min = 3, max = 30, message = "O campo 'nome' deve ter entre 3 e 30 caracteres")
    private final String holderName;

    @JsonProperty("sobrenomeCorrentista")
    @Size(min = 5, max = 45, message = "O campo 'sobrenome' deve ter entre 3 e 45 caracteres")
    private final String holderLastName;

    @JsonProperty("tipoPessoa")
    @NotEmpty(message = "O campo 'tipoPessoa' é obrigatório")
    @Pattern(regexp = "F|J", message = "O campo 'tipoPessoa' deve conter um dos seguintes valores: [F = Pessoa Física, J = Pessoa Jurídica] ")
    private final String holderType;

}
