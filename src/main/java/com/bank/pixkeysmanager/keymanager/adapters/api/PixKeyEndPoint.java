package com.bank.pixkeysmanager.keymanager.adapters.api;

import com.bank.pixkeysmanager.keymanager.adapters.api.request.CreateKeyRequest;
import com.bank.pixkeysmanager.keymanager.adapters.api.request.FindKeyRequest;
import com.bank.pixkeysmanager.keymanager.adapters.api.request.UpdateKeyRequest;
import com.bank.pixkeysmanager.keymanager.adapters.api.response.CreateKeyResponse;
import com.bank.pixkeysmanager.keymanager.adapters.api.response.DeleteKeyResponse;
import com.bank.pixkeysmanager.keymanager.adapters.api.response.FindKeyResponse;
import com.bank.pixkeysmanager.keymanager.adapters.api.response.UpdateKeyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("keys")
@Validated
@RequiredArgsConstructor
public class PixKeyEndPoint {

    private final AccountFacade account;

    @PostMapping
    CreateKeyResponse create(@RequestBody @Valid final CreateKeyRequest createKeyRequest) {
        return account.save(createKeyRequest);
    }

    @PatchMapping
    UpdateKeyResponse update(@RequestBody @Valid final UpdateKeyRequest updateKeyRequest) {
        return account.update(updateKeyRequest);
    }

    @DeleteMapping("/{id}")
    DeleteKeyResponse delete(@PathVariable("id")
                             @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$", message = "O parâmetro 'id' é inválido.")
                             final String id) {
        return account.delete(id);
    }

    @GetMapping
    List<FindKeyResponse> find(@RequestBody @Valid final FindKeyRequest findKeyRequest) {
        return account.find(findKeyRequest);
    }
}
