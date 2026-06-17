package com.jpmc.ledger.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Transaction(UUID id, UUID sourceAccountId, UUID targetAccountId, BigDecimal amount, Instant timestamp, String status) {}