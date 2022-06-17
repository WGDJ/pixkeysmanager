package com.bank.pixkeysmanager.keymanager.adapters.api.response;

import com.bank.pixkeysmanager.keymanager.domain.model.account.Account;
import com.bank.pixkeysmanager.keymanager.domain.model.account.AccountType;
import com.bank.pixkeysmanager.keymanager.domain.model.account.HolderType;
import com.bank.pixkeysmanager.keymanager.domain.model.key.Key;
import com.bank.pixkeysmanager.keymanager.domain.model.key.KeyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class DeleteKeyResponse {

    @JsonProperty("id")
    private final String id;

    @JsonProperty("tipoChave")
    private final KeyType keyType;

    @JsonProperty("valorChave")
    private final String keyValue;

    @JsonProperty("tipoConta")
    private final AccountType accountType;

    @JsonProperty("agencia")
    private final int agencyNumber;

    @JsonProperty("conta")
    private final int accountNumber;

    @JsonProperty("nomeCorrentista")
    private final String holderName;

    @JsonProperty("tipoPessoa")
    private final HolderType holderType;

    @JsonProperty("sobrenomeCorrentista")
    private final String holderLastName;

    @JsonProperty("dataHoraInclusao")
    private final Date includedTime;

    @JsonProperty("dataHoraInativacao")
    private final Date deletedTime;

    public static DeleteKeyResponse of(final Account account) {
        final Key key = account.getKeys().iterator().next();
        return DeleteKeyResponse.builder()
                .id(key.getId().toString())
                .keyType(key.getType())
                .keyValue(key.getValue())
                .accountType(account.getAccountType())
                .agencyNumber(account.getAgencyNumber())
                .accountNumber(account.getAccountNumber())
                .holderName(account.getHolderName())
                .holderLastName(account.getHolderLastName())
                .holderType(account.getHolderType())
                .includedTime(key.getIncludedTime())
                .deletedTime(key.getDeletedTime())
                .build();
    }
}
