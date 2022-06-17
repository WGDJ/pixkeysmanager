package com.bank.pixkeysmanager.keymanager.domain.model.account;

import com.bank.pixkeysmanager.keymanager.domain.model.key.Key;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@Document
public class Account {

    @Id
    private UUID id;
    private AccountType accountType;
    private Integer agencyNumber;
    private Integer accountNumber;
    private String holderName;
    private String holderLastName;
    private HolderType holderType;
    private Set<Key> keys;

}
