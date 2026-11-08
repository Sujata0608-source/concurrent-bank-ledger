package com.jpmc.ledger.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.jpmc.ledger.entity.Account;
import com.jpmc.ledger.model.Transaction;
import com.jpmc.ledger.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LedgerService {

    private final AccountRepository accountRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(LedgerService.class);

    public LedgerService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @Transactional
    public Transaction transferFunds(UUID sourceId,
                                     UUID targetId,
                                     BigDecimal amount) {
        //logger
        logger.info(
                "Transfer initiated from account {} to account {} for amount {}",
                sourceId,
                targetId,
                amount
        );

        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException("Source and target account cannot be the same.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }

        UUID first = sourceId;
        UUID second = targetId;

        if (first.compareTo(second) > 0) {
            first = targetId;
            second = sourceId;
        }
        final UUID firstId = first;
        final UUID secondId = second;

        //setting the account for source and target account
        Account firstAccount = accountRepository
                .findByIdForUpdate(first)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found: " + firstId));

        Account secondAccount = accountRepository
                .findByIdForUpdate(second)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found: " + secondId
                        ));

        //locking the account by the direction of money flow
        Account source = sourceId.equals(firstId)
                ? firstAccount
                : secondAccount;

        Account target = targetId.equals(firstId)
                ? firstAccount
                : secondAccount;

        //checking the account for insufficient balance
        if (source.getBalance().compareTo(amount) < 0) {
            logger.error(
                    "Transfer failed: insufficient funds in account {}",
                    sourceId
            );
            throw new IllegalStateException("Insufficient funds.");
        }
        source.setBalance(
                source.getBalance().subtract(amount)
        );

        target.setBalance(
                target.getBalance().add(amount)
        );

        accountRepository.save(source);
        accountRepository.save(target);
        logger.info(
                "Transfer completed successfully from account {} to account {}",
                sourceId,
                targetId
        );

        return new Transaction(
                UUID.randomUUID(),
                sourceId,
                targetId,
                amount,
                Instant.now(),
                "SUCCESS"
        );
    }
}
