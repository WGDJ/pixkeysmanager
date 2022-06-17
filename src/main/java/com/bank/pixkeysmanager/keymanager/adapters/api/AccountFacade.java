package com.bank.pixkeysmanager.keymanager.adapters.api;

import com.bank.pixkeysmanager.keymanager.adapters.api.request.CreateKeyRequest;
import com.bank.pixkeysmanager.keymanager.adapters.api.request.FindKeyRequest;
import com.bank.pixkeysmanager.keymanager.adapters.api.request.UpdateKeyRequest;
import com.bank.pixkeysmanager.keymanager.adapters.api.response.CreateKeyResponse;
import com.bank.pixkeysmanager.keymanager.adapters.api.response.DeleteKeyResponse;
import com.bank.pixkeysmanager.keymanager.adapters.api.response.FindKeyResponse;
import com.bank.pixkeysmanager.keymanager.adapters.api.response.UpdateKeyResponse;
import com.bank.pixkeysmanager.keymanager.adapters.mongodb.accountdb.AccountDatabaseModel;
import com.bank.pixkeysmanager.keymanager.domain.ports.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountFacade {

    private final AccountService accountService;

    CreateKeyResponse save(final CreateKeyRequest createKeyRequest) {
        return CreateKeyResponse.of(accountService.save(AccountDatabaseModel.toDomain(createKeyRequest)));
    }

    UpdateKeyResponse update(final UpdateKeyRequest updateKeyRequest) {
        return UpdateKeyResponse.of(accountService.update(UUID.fromString(updateKeyRequest.getId()), updateKeyRequest.getKeyValue()));
    }

    DeleteKeyResponse delete(final String id) {
        return DeleteKeyResponse.of(accountService.delete(UUID.fromString(id)));
    }

    List<FindKeyResponse> find(final FindKeyRequest findKeyRequest) {
        return accountService.find(AccountDatabaseModel.toDomain(findKeyRequest)).stream()
                .map(FindKeyResponse::of).collect(Collectors.toList());
    }
}
