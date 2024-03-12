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
        if (wallet.get() == null)
            throw new IllegalArgumentException("This player does not have a wallet");
        return wallet.get();
    }

    public void performTransaction(UUID senderId, UUID receiverId, float amount) throws IllegalArgumentException {
        var sender = getWallet(senderId);
        var receiver = getWallet(receiverId);
        sender.withdraw(amount);
        receiver.addMoney(amount);
    }

    public void createNewWallet(UUID playerId) {
        try {
            getWallet(playerId);
            throw new IllegalArgumentException("Cannot create a new wallet for this player");
        } catch (IllegalArgumentException iaex) {
            wallets.add(new Wallet(playerId));
        }
    }

    public ArrayList<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
    }
}
