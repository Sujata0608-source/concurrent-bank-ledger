package com.jpmc.ledger.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "idempotent_requests")
public class IdempotentRequest {

    @Id
    private UUID id;

    private String responseStatus;

    public IdempotentRequest() {
    }

    //getter
    public UUID getId() {
        return id;
    }

    //setter
    public void setId(UUID id) {
        this.id = id;
    }

    //getter
    public String getResponseStatus() {
        return responseStatus;
    }

    //setter
    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }
}