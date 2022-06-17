package com.bank.pixkeysmanager.keymanager.adapters.mongodb.accountdb;

import com.bank.pixkeysmanager.keymanager.adapters.api.request.CreateKeyRequest;
import com.bank.pixkeysmanager.keymanager.adapters.api.request.FindKeyRequest;
import com.bank.pixkeysmanager.keymanager.domain.model.account.Account;
import com.bank.pixkeysmanager.keymanager.domain.model.account.AccountType;
import com.bank.pixkeysmanager.keymanager.domain.model.account.HolderType;
import com.bank.pixkeysmanager.keymanager.domain.model.key.Key;
import com.bank.pixkeysmanager.keymanager.domain.model.key.KeyType;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class AccountDatabaseModel {

    public static Account toDomain(final CreateKeyRequest createKeyRequest) {
        return Account.builder()
                .id(UUID.nameUUIDFromBytes(
                        (AccountType.getByValue(createKeyRequest.getAccountType()) +
                                createKeyRequest.getAgencyNumber() +
                                Integer.valueOf(createKeyRequest.getAccountNumber()) +
                                HolderType.getByValue(createKeyRequest.getHolderType())).getBytes()))
                .accountType(AccountType.getByValue(createKeyRequest.getAccountType()))
                .agencyNumber(Integer.valueOf(createKeyRequest.getAgencyNumber()))
                .accountNumber(Integer.valueOf(createKeyRequest.getAccountNumber()))
                .holderName(createKeyRequest.getHolderName())
                .holderLastName(createKeyRequest.getHolderLastName())
                .holderType(HolderType.getByValue(createKeyRequest.getHolderType()))
                .keys(Set.of(Key.builder()
                        .type(KeyType.getByValue(createKeyRequest.getKeyType()))
                        .value(createKeyRequest.getKeyValue())
                        .includedTime(new Date())
                        .build()))
                .build();
    }

    public static Account toDomain(final FindKeyRequest findKeyRequest) {
        Key.KeyBuilder keyBuilder = Key.builder();

        if (findKeyRequest.getId() != null) {
            keyBuilder.id(UUID.fromString(findKeyRequest.getId()));
        }

        if (findKeyRequest.getKeyType() != null) {
            keyBuilder.type(KeyType.getByValue(findKeyRequest.getKeyType()));
        }

        if (findKeyRequest.getIncludedTime() != null) {
            keyBuilder.includedTime(findKeyRequest.getIncludedTime());
        }

        if (findKeyRequest.getDeletedTime() != null) {
            keyBuilder.deletedTime(findKeyRequest.getDeletedTime());
        }

        Account.AccountBuilder accountBuilder = Account.builder();
        accountBuilder.keys(Set.of(keyBuilder.build()));

        if (findKeyRequest.getAgencyNumber() != null) {
            accountBuilder.agencyNumber(Integer.valueOf(findKeyRequest.getAgencyNumber()));
        }
        if (findKeyRequest.getAccountNumber() != null) {
            accountBuilder.accountNumber(Integer.valueOf(findKeyRequest.getAccountNumber()));
        }
        if (findKeyRequest.getHolderName() != null) {
            accountBuilder.holderName(findKeyRequest.getHolderName());
        }

        return accountBuilder.build();
    }

}
