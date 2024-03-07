package fr.valorantage.valomoney.backend.economy;

import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.UUID;

public final class EconomyManager {
    private ArrayList<Wallet> wallets;

    public EconomyManager() {
        wallets = new ArrayList<>();
    }

    public Wallet findWalletByPlayerId(UUID playerId) {
        throw new NotImplementedException();
    }

    public void performTransaction(UUID senderId, UUID receiverId, float amount) {
        throw new NotImplementedException();
    }

    public ArrayList<Wallet> getWallets() {
        return wallets;
    }
}
