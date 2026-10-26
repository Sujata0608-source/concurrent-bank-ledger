package com.jpmc.ledger.controller;

import com.jpmc.ledger.dto.TransferRequest;
import com.jpmc.ledger.model.Transaction;
import com.jpmc.ledger.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jpmc.ledger.entity.Account;
import com.jpmc.ledger.repository.AccountRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {

    private final LedgerService ledgerService;
    private final AccountRepository accountRepository;

    public LedgerController(LedgerService ledgerService, AccountRepository accountRepository) {
        this.ledgerService = ledgerService;
        this.accountRepository= accountRepository;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(
            @RequestBody TransferRequest request) {

        Transaction transaction = ledgerService.transferFunds(
                request.sourceAccountId(),
                request.targetAccountId(),
                request.amount()
        );

        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountRepository.findAll());
    }
}
