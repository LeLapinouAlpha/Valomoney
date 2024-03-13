package fr.valorantage.valomoney.backend.economy;

import java.io.Serializable;
import java.util.UUID;

public final class Wallet implements Serializable {
    private final UUID playerId;
    private float balance;

    public Wallet(UUID playerId) {
        this.playerId = playerId;
        balance = 0.0f;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        if (balance < 0)
            throw new IllegalArgumentException("The balance must be positive");
        this.balance = balance;
    }

    public void withdraw(float amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("The amount of money you want to withdraw must be strictly positive");
        else if (balance < amount)
            throw new IllegalArgumentException("You don't have enough funds");
        balance -= amount;
    }

    public void addMoney(float amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("The amount of money you want to add must be strictly positive");
        balance += amount;
    }
}
