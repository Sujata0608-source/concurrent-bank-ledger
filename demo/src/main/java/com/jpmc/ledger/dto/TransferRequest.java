package com.jpmc.ledger.dto;
import java.math.BigDecimal;
import java.util.UUID;
public record TransferRequest (
        UUID sourceAccountId,
        UUID targetAccountId,
        BigDecimal amount
) {}
