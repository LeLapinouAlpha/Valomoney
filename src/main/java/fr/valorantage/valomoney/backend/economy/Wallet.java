package fr.valorantage.valomoney.backend.economy;

import org.apache.commons.lang3.NotImplementedException;

import java.util.UUID;

public final class Wallet {
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
        this.balance = balance;
    }

    public void withdraw(float amount) {
        throw new NotImplementedException();
    }

    public void addMoney(float amount) {
        throw new NotImplementedException();
    }

    public void save(String filePath) {
        throw new NotImplementedException();
    }

    public void restore(String filePath) {
        throw new NotImplementedException();
    }
}
