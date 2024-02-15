package ru.lechiffre.itrumtask.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;


public class WalletRequest {
    @NotBlank(message = "You must put UUID to the request")
    private UUID walletId;
    @Pattern(regexp = "DEPOSIT|WITHDRAW", message = "Transaction type must be either DEPOSIT or WITHDRAW")
    private String operationType;
    @Min(value = 1, message = "Amount must equal 1 or be greater")
    private Double amount;


    public WalletRequest(UUID walletId, String operationType, Double amount) {
        this.walletId = walletId;
        this.operationType = operationType;
        this.amount = amount;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
