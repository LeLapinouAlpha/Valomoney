package fr.valorantage.valomoney.backend.economy;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public final class EconomyManager {
    private ArrayList<Wallet> wallets;

    public EconomyManager() {
        wallets = new ArrayList<>();
    }

    public Wallet getWallet(UUID playerId) {
        var wallet = new AtomicReference<Wallet>();
        wallets.forEach(w -> {
            if (w.getPlayerId().equals(playerId))
                wallet.set(w);
        });
        return wallet.get();
    }

    public void performTransaction(UUID senderId, UUID receiverId, float amount) throws IllegalArgumentException {
        var sender = getWallet(senderId);
        var receiver = getWallet(receiverId);

        try {
            sender.withdraw(amount);
            receiver.addMoney(amount);
        } catch (NullPointerException npex) {
            throw new IllegalArgumentException("The sender or the receiver does not exist");
        }
    }

    public ArrayList<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
    }
}
