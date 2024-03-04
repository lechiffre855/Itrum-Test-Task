package ru.lechiffre.itrumtask.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Wallet")
public class Wallet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "wallet_id")
    private UUID walletId;
    @Column(name = "amount")
    private Double amount;

    public Wallet(){}

    public Wallet(UUID walletId, Double amount) {
        this.walletId = walletId;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
