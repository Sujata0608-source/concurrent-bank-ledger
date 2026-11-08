
package com.jpmc.ledger.repository;

import com.jpmc.ledger.entity.IdempotentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IdempotencyRepository
        extends JpaRepository<IdempotentRequest, UUID> {
}
