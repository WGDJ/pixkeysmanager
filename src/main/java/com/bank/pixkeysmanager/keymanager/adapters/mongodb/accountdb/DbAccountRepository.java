package com.bank.pixkeysmanager.keymanager.adapters.mongodb.accountdb;

import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerKeyNotFountException;
import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerQueryWithoutResultsException;
import com.bank.pixkeysmanager.keymanager.domain.exceptions.KeyManagerValidationException;
import com.bank.pixkeysmanager.keymanager.domain.model.account.Account;
import com.bank.pixkeysmanager.keymanager.domain.model.key.Key;
import com.bank.pixkeysmanager.keymanager.domain.ports.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;
import static org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq.valueOf;

@Component
@RequiredArgsConstructor
public class DbAccountRepository implements AccountRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Account save(final Account account) {
        return mongoTemplate.save(account);
    }

    @Override
    public Account update(final Account account) {
        return mongoTemplate.save(account);
    }

    @Override
    public Account getByAccountId(final UUID id) {
        return mongoTemplate.findById(id, Account.class);
    }

    @Override
    public Account getByKeyId(final UUID id) {
        Criteria criteria = Criteria.where("keys")
                .elemMatch(Criteria.where("deleted").is(false).and("_id").is(id));

        BooleanOperators.And projectionKeysFilter = BooleanOperators.And.and(valueOf("key.deleted").equalToValue(false));

        return aggregateFindResult(criteria, projectionKeysFilter).stream().findFirst().orElse(null);
    }

    @Override
    public Boolean existsKeyByValue(final Key key) {
        return mongoTemplate.exists(new Query(Criteria.where("keys")
                .elemMatch(Criteria.where("deleted").is(false)
                        .and("value").is(key.getValue()))), Account.class);
    }

    @Override
    public Boolean existsKeyByType(final Account account, final Key key) {
        return mongoTemplate.exists(new Query().addCriteria(Criteria.where("keys")
                .elemMatch(Criteria.where("deleted").is(false)
                        .and("type").is(key.getType()))
                .and("_id").is(account.getId())), Account.class);
    }

    @Override
    public List<Account> find(final Account account) {

        validateQuery(account);

        Key key = account.getKeys().iterator().next();

        Criteria keyElementMatch = Criteria.where("deleted").is(false);
        Criteria criteria = Criteria.where("keys").elemMatch(keyElementMatch);

        BooleanOperators.And projectionKeysFilter = BooleanOperators.And.and(valueOf("key.deleted").equalToValue(false));

        if (key.getId() != null) {
            projectionKeysFilter = projectionKeysFilter.andExpression(valueOf("key._id").equalToValue(key.getId()));

            final List<Account> result = aggregateFindResult(Criteria.where("keys")
                    .elemMatch(Criteria.where("deleted").is(false)
                            .and("_id").is(key.getId())), projectionKeysFilter);

            if (result.isEmpty()) {
                throw new KeyManagerKeyNotFountException(key.getId());
            } else {
                return result;
            }
        }

        if (key.getType() != null) {
            keyElementMatch.and("type").is(key.getType());
            projectionKeysFilter = projectionKeysFilter.andExpression(valueOf("key.type").equalToValue(key.getType().getValue()));
        }

        if (account.getAgencyNumber() != null && account.getAccountNumber() != null) {
            criteria = criteria.and("agencyNumber").is(account.getAgencyNumber())
                    .and("accountNumber").is(account.getAccountNumber());
        }

        if (account.getHolderName() != null) {
            criteria = criteria.and("holderName").regex(account.getHolderName());
        }

        final List<Account> result = aggregateFindResult(criteria, projectionKeysFilter);

        if (result.isEmpty()) {
            throw new KeyManagerQueryWithoutResultsException();
        }
        return result;
    }

    private List<Account> aggregateFindResult(final Criteria criteria, final BooleanOperators.And projectionKeysFilter) {
        final Aggregation aggregation = newAggregation(Aggregation.match(criteria),
                project()
                        .and("accountType").as("accountType")
                        .and("agencyNumber").as("agencyNumber")
                        .and("accountNumber").as("accountNumber")
                        .and("holderName").as("holderName")
                        .and("holderLastName").as("holderLastName")
                        .and("holderType").as("holderType")
                        .and(filter("keys")
                                .as("key")
                                .by(projectionKeysFilter))
                        .as("keys"));

        return mongoTemplate.aggregate(aggregation, Account.class, Account.class).getMappedResults();
    }

    private void validateQuery(Account account) {

        Key key = account.getKeys().iterator().next();

        Set<String> errors = new HashSet<>();

        if (key.getType() != null && ((account.getAgencyNumber() == null && account.getAccountNumber() == null) && account.getHolderName() == null)) {
            errors.add("A busca por tipo de chave pix deve ser acompanhada de 'agencia' e 'conta' ou 'nomeCorrentista.");
        }

        if (key.getId() == null && key.getType() == null && account.getAgencyNumber() == null && account.getAccountNumber() == null
                && account.getHolderName() == null && key.getIncludedTime() == null && key.getDeletedTime() == null) {
            errors.add("Informe pelo menos um parametro de busca.");
        }

        if ((account.getAgencyNumber() != null && account.getAccountNumber() == null) || account.getAgencyNumber() == null && account.getAccountNumber() != null) {
            errors.add("Os parâmetros 'agência' e 'conta' devem ser informados juntos.");
        }

        if (key.getId() != null && (key.getType() != null || account.getAgencyNumber() != null || account.getAccountNumber() != null
                || account.getHolderName() != null || key.getIncludedTime() != null || key.getDeletedTime() != null)) {
            errors.add("Não é possível consultar informado o id da chave pix combinado com outro campo.");
        }

        if (key.getIncludedTime() != null && key.getDeletedTime() != null) {
            errors.add("Não é possível consultar informado a data de inclusão combinada com a data de inaticação.");
        }

        if (!errors.isEmpty()) {
            throw new KeyManagerValidationException(errors);
        }
    }

}
