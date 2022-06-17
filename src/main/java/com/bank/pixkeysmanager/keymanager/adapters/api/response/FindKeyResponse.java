package com.bank.pixkeysmanager.keymanager.adapters.api.response;

import com.bank.pixkeysmanager.keymanager.domain.model.account.Account;
import com.bank.pixkeysmanager.keymanager.domain.model.account.AccountType;
import com.bank.pixkeysmanager.keymanager.domain.model.account.HolderType;
import com.bank.pixkeysmanager.keymanager.domain.model.key.KeyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class FindKeyResponse {

    @JsonProperty("tipoConta")
    private final AccountType accountType;

    @JsonProperty("agencia")
    private final int agencyNumber;

    @JsonProperty("conta")
    private final int accountNumber;

    @JsonProperty("tipoPessoa")
    private final HolderType holderType;

    @JsonProperty("nomeCorrentista")
    private final String holderName;

    @JsonProperty("sobrenomeCorrentista")
    private final String holderLastName;

    @JsonProperty("chavesPix")
    private Set<Key> keys;

    public static FindKeyResponse of(Account account) {
        return FindKeyResponse.builder()
                .accountType(account.getAccountType())
                .agencyNumber(account.getAgencyNumber())
                .accountNumber(account.getAccountNumber())
                .holderName(account.getHolderName())
                .holderLastName(account.getHolderLastName())
                .holderType(account.getHolderType())
                .keys(account.getKeys().stream()
                        .map(key -> Key.builder()
                                .id(key.getId())
                                .keyType(key.getType())
                                .keyValue(key.getValue())
                                .includedTime(key.getIncludedTime())
                                .build()).collect(Collectors.toSet()))
                .build();
    }

    @Getter
    @Builder
    private static class Key {
        @JsonProperty("id")
        private final UUID id;

        @JsonProperty("tipoChave")
        private final KeyType keyType;

        @JsonProperty("valorChave")
        private final String keyValue;

        @JsonProperty("dataHoraInclusao")
        private final Date includedTime;

    }
}
