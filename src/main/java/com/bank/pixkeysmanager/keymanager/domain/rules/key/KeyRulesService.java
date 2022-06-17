package com.bank.pixkeysmanager.keymanager.domain.rules.key;

import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerValidationException;
import com.bank.pixkeysmanager.keymanager.domain.model.account.Account;
import com.bank.pixkeysmanager.keymanager.domain.model.key.Key;
import com.bank.pixkeysmanager.keymanager.domain.model.key.KeyType;
import com.bank.pixkeysmanager.keymanager.domain.ports.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class KeyRulesService {

    private AccountRepository accountRepository;

    public KeyRulesService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    private final Function<Key, List<String>> validateKeyTypePhoneNumber = (final Key key) -> {
        List<String> errors = new ArrayList<String>();

        if (!key.getValue().matches("\\+\\d{1,14}")) {
            errors.add("Chave pix do tipo telefone é inválida, o padrão permitido é +55000000000000.");
        }
        return errors;
    };
    private final Function<Key, List<String>> validateKeyTypeMail = (final Key key) -> {
        List<String> errors = new ArrayList<String>();

        if (!key.getValue().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.add("Chave pix do tipo email é inválida, o padrão permitido é ana.maria@gmail.com .");
        }
        if (key.getValue().length() > 77) {
            errors.add("Chave pix do tipo email é inválida, email muito longo, tamanho máximo é 77.");
        }

        return errors;
    };
    private final Function<Key, List<String>> validateKeyTypeCPF = (final Key key) -> {
        List<String> errors = new ArrayList<String>();

        if (!key.getValue().matches("\\d{11}")) {
            errors.add("Chave pix do tipo CPF é inválida, o padrão permitido é de 11 números.");
        }

        if (!CPFValidator.isCPF(key.getValue())) {
            errors.add("Chave pix do tipo CPF é inválida, CPF não passou pela verificação.");
        }

        return errors;
    };
    private final Map<KeyType, Function<Key, List<String>>> validatorStrategy = Map.of(
            KeyType.CELULAR, validateKeyTypePhoneNumber,
            KeyType.EMAIL, validateKeyTypeMail,
            KeyType.CPF, validateKeyTypeCPF
    );
    private final Function<Key, List<String>> validateAlreadyExistsKeyByValue = (final Key key) -> {
        List<String> errors = new ArrayList<String>();

        if (accountRepository.existsKeyByValue(key)) {
            errors.add("Chave pix já cadastrada.");
        }
        return errors;
    };

    private final BiFunction<Account, Key, List<String>> validateAlreadyExistsKeyByType = (final Account account, final Key key) -> {
        List<String> errors = new ArrayList<String>();

        if (accountRepository.existsKeyByType(account, key)) {
            errors.add("O usuário " + account.getHolderName() + " já possui chave pix do tipo " + key.getType().getValue() + " cadastrada.");
        }
        return errors;
    };

    private Set<BiFunction<Account, Key, List<String>>> getAccountAndKeyValidators() {
        return Set.of(validateAlreadyExistsKeyByType);
    }

    private Set<Function<Key, List<String>>> getKeyValidators(final Key key) {
        return Set.of(validateAlreadyExistsKeyByValue, validatorStrategy.get(key.getType()));
    }

    public void updateValidate(final Key key) {

        Set<String> errors = new HashSet<String>(getKeyValidators(key).stream()
                .map(validation -> validation.apply(key))
                .flatMap(Collection::stream)
                .sorted().collect(Collectors.toList()));

        if (errors.size() > 0) {
            throw new KeyManagerValidationException(errors);
        }
    }
    public void saveValidate(final Account account, final Key key) {
        Set<String> errors = new HashSet<String>();

        errors.addAll(getKeyValidators(key).stream()
                .map(validation -> validation.apply(key))
                .flatMap(Collection::stream)
                .sorted().collect(Collectors.toList()));

        errors.addAll(getAccountAndKeyValidators().stream()
                .map(validation -> validation.apply(account, key))
                .flatMap(Collection::stream)
                .sorted().collect(Collectors.toList()));


        if (errors.size() > 0) {
            throw new KeyManagerValidationException(errors);
        }
    }
}
