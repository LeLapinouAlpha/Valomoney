package fr.valorantage.valomoney.backend.economy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public final class EconomyManager {
    private ArrayList<Wallet> wallets;

    public EconomyManager() {
        wallets = new ArrayList<>();
    }

    public Wallet getWallet(UUID playerId) {
        var optionalWallet = wallets.stream().filter(w -> w.getPlayerId().equals(playerId)).findAny();
        if (optionalWallet.isEmpty())
            throw new IllegalArgumentException("This player does not have a wallet");
        return optionalWallet.get();
    }

    public void performTransaction(UUID senderId, UUID receiverId, float amount) throws IllegalArgumentException {
        var sender = getWallet(senderId);
        var receiver = getWallet(receiverId);
        sender.withdraw(amount);
        receiver.addMoney(amount);
    }

    public void createWallet(UUID playerId) {
        try {
            getWallet(playerId);
        } catch (IllegalArgumentException iaex) {
            wallets.add(new Wallet(playerId));
        }
        throw new IllegalArgumentException("Cannot create a new wallet for this player");
    }

    public void addWallet(Wallet wallet) {
        try {
            getWallet(wallet.getPlayerId());
        } catch (IllegalArgumentException iaex) {
            wallets.add(wallet);
        }
        throw new IllegalArgumentException("This wallet is already associated with a player");
    }

    public void saveState(EconomyFileManager economyFileManager) throws IOException {
        for (final var wallet : wallets) {
            economyFileManager.saveWallet(wallet);
        }
    }

    public void restoreState(EconomyFileManager economyFileManager) throws IOException, ClassNotFoundException {
        wallets.clear();
        Wallet wallet = economyFileManager.restoreWallet();
        while (wallet != null)
        {
            wallets.add(wallet);
            wallet = economyFileManager.restoreWallet();
        }
    }
}
