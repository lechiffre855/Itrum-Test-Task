package ru.lechiffre.itrumtask.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "Wallet")
public class Wallet {
    @Id
    @Column(name = "id")
    private UUID walletId;
    @Column(name = "amount")
    private Double amount;

    public Wallet(){}

    public Wallet(UUID walletId, Double amount) {
        this.walletId = walletId;
        this.amount = amount;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
