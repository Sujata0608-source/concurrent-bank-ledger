package com.jpmc.ledger.service;

import com.jpmc.ledger.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class LedgerService {

    private final AccountRepository accountRepository;

    public LedgerService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
