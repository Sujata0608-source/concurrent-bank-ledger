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

import com.jpmc.ledger.entity.IdempotentRequest;
import com.jpmc.ledger.repository.IdempotencyRepository;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {

    private final LedgerService ledgerService;
    private final AccountRepository accountRepository;
    private final IdempotencyRepository idempotencyRepository;

    public LedgerController(
            LedgerService ledgerService,
            AccountRepository accountRepository,
            IdempotencyRepository idempotencyRepository) {

        this.ledgerService = ledgerService;
        this.accountRepository = accountRepository;
        this.idempotencyRepository = idempotencyRepository;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(
            @RequestHeader("Idempotency-Key") UUID idempotencyKey,
            @RequestBody TransferRequest request) {

        // Check whether this request key already exists
        if (idempotencyRepository.existsById(idempotencyKey)) {
            return ResponseEntity.status(409).build();
        }

        // Execute the transfer
        Transaction transaction = ledgerService.transferFunds(
                request.sourceAccountId(),
                request.targetAccountId(),
                request.amount()
        );

        // Record the successfully processed request
        IdempotentRequest idempotentRequest = new IdempotentRequest();
        idempotentRequest.setId(idempotencyKey);
        idempotentRequest.setResponseStatus("SUCCESS");

        idempotencyRepository.save(idempotentRequest);

        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountRepository.findAll());
    }
}