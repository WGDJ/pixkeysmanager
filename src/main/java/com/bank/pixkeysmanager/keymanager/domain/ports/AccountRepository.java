package com.bank.pixkeysmanager.keymanager.domain.ports;

import com.bank.pixkeysmanager.keymanager.domain.model.account.Account;
import com.bank.pixkeysmanager.keymanager.domain.model.key.Key;

import java.util.List;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);

    Account update(Account account);

    Account getByAccountId(UUID id);

    Account getByKeyId(UUID id);

    Boolean existsKeyByValue(Key key);

    Boolean existsKeyByType(Account account, Key key);

    List<Account> find(Account account);

}
