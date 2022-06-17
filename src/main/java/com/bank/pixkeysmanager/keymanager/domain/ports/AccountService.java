package com.bank.pixkeysmanager.keymanager.domain.ports;

import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerKeyNotFountException;
import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerValidationException;
import com.bank.pixkeysmanager.keymanager.domain.model.account.Account;
import com.bank.pixkeysmanager.keymanager.domain.model.key.Key;
import com.bank.pixkeysmanager.keymanager.domain.rules.key.KeyRulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final KeyRulesService keyRules;
    private final AccountRepository accountRepository;
    @NotNull
    private Account getAccountByKeyId(final UUID keyId) {
        return Optional.ofNullable(accountRepository.getByKeyId(keyId))
                .orElseThrow(() -> new KeyManagerKeyNotFountException(keyId));
    }

    public UUID save(final Account account) {

        Key key = account.getKeys().stream().findFirst().get();

        key.setId(UUID.randomUUID());

        keyRules.saveValidate(account, key);

        Account persistentAccount = accountRepository.getByAccountId(account.getId());

        if (persistentAccount != null) {
            persistentAccount.getKeys().add(key);
            accountRepository.save(persistentAccount);
        } else {
            accountRepository.save(account);
        }

        return key.getId();
    }

    @Transactional
    public Key update(final UUID id, final String keyValue) {
        Account persistentAccount = getAccountByKeyId(id);

        Key requestKey = persistentAccount.getKeys().stream()
                .filter(key -> key.getId().equals(id)).findFirst().orElse(null);

        requestKey.setValue(keyValue);

        keyRules.updateValidate(requestKey);

        accountRepository.update(persistentAccount);

        return requestKey;
    }

    public Account delete(final UUID id) {
        Account persistentAccount = getAccountByKeyId(id);

        Key keyToDelete = persistentAccount.getKeys()
                .stream().filter(key -> key.getId().equals(id) && !key.isDeleted())
                .findFirst().orElseThrow(() -> new KeyManagerValidationException(Set.of("Não foi encontrada chave pix para deleção.")));
        keyToDelete.setDeleted(true);
        keyToDelete.setDeletedTime(new Date());

        return accountRepository.update(persistentAccount);
    }

    public List<Account> find(final Account account) {
        return accountRepository.find(account);
    }
}
