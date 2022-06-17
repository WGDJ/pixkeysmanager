package com.bank.pixkeysmanager.integration;

import com.bank.pixkeysmanager.keymanager.adapters.api.request.CreateKeyRequest;
import com.bank.pixkeysmanager.keymanager.adapters.api.response.CreateKeyResponse;
import com.bank.pixkeysmanager.keymanager.domain.model.account.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class CreateKeyIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    private void cleanUpMongo() {
        mongoTemplate.remove(new Query(), Account.class);
    }

    @DisplayName("Teste criação chave pix.")
    @Test
    public void createKey() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        CreateKeyRequest createKeyRequest = new CreateKeyRequest("CELULAR", "+55988725862", "CORRENTE",
                "1234", "12345678", "João", "Silva", "F");

        mvc.perform(post("/keys")
                        .content(mapper.writeValueAsString(createKeyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", any(String.class)));

    }

    @DisplayName("Teste atualização do valor de uma chave pix ")
    @Test
    public void updateKey() throws Exception {

        CreateKeyRequest createKeyRequest = new CreateKeyRequest("CELULAR", "+5586988725862", "CORRENTE",
                "1234", "12345678", "João", "Silva", "F");

        ObjectMapper mapper = new ObjectMapper();

        String createKeyResponseAsString = mvc.perform(post("/keys")
                .content(mapper.writeValueAsString(createKeyRequest))
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        CreateKeyResponse createKeyResponse = mapper.readValue(createKeyResponseAsString, CreateKeyResponse.class);

        String newKeyValue = "+5585988725862";

        String bodyUpdate = mapper.writeValueAsString(Map.of("id", createKeyResponse.getId(), "valorChave", newKeyValue));

        mvc.perform(patch("/keys")
                        .content(bodyUpdate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valorChave", is(newKeyValue)));

    }

    @DisplayName("Teste deleção de chave pix")
    @Test
    public void deleteKey() throws Exception {

        CreateKeyRequest createKeyRequest = new CreateKeyRequest("CELULAR", "+5586988725862", "CORRENTE",
                "1234", "12345678", "João", "Silva", "F");

        ObjectMapper mapper = new ObjectMapper();

        String createKeyResponseAsString = mvc.perform(post("/keys")
                .content(mapper.writeValueAsString(createKeyRequest))
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        CreateKeyResponse createKeyResponse = mapper.readValue(createKeyResponseAsString, CreateKeyResponse.class);

        mvc.perform(delete("/keys/" + createKeyResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataHoraInativacao", any(String.class)));

    }

    @DisplayName("Teste deleção de chave pix")
    @Test
    public void findKey() throws Exception {

        CreateKeyRequest createKeyRequest = new CreateKeyRequest("CELULAR", "+5586988725862", "CORRENTE",
                "1234", "12345678", "João", "Silva", "F");

        ObjectMapper mapper = new ObjectMapper();

        String createKeyResponseAsString = mvc.perform(post("/keys")
                .content(mapper.writeValueAsString(createKeyRequest))
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();

        CreateKeyResponse createKeyResponse = mapper.readValue(createKeyResponseAsString, CreateKeyResponse.class);

        String bodyFind = mapper.writeValueAsString(Map.of("id", createKeyResponse.getId()));

        mvc.perform(get("/keys")
                        .content(bodyFind)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].chavesPix[0].id", is(createKeyResponse.getId().toString())));

    }
}