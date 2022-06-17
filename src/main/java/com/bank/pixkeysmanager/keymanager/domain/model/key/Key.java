package com.bank.pixkeysmanager.keymanager.domain.model.key;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Builder
@Getter
@Setter
@Document
public class Key {

    @Id
    private UUID id;
    private KeyType type;
    private String value;
    private boolean deleted;
    private Date includedTime;
    private Date deletedTime;

}
