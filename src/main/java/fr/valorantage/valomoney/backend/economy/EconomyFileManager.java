package fr.valorantage.valomoney.backend.economy;

import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;

public final class EconomyFileManager {
    private final String directoryPath;

    public EconomyFileManager(final String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void saveWallets(final ArrayList<Wallet> wallets) {
        throw new NotImplementedException();
    }

    public ArrayList<Wallet> restoreWallets() {
        throw new NotImplementedException();
    }
}
