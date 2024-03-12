package fr.valorantage.valomoney.backend.economy;

import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public final class EconomyFileManager {
    private File directory;

    public EconomyFileManager(final String directoryPath) {
        var directory = new File(directoryPath);
        if (!directory.exists()) {
            if (!directory.mkdir())
                throw new RuntimeException("Cannot create directory");
        } else if (!directory.isDirectory())
            throw new IllegalArgumentException(String.format("%s is not a directory", directoryPath));

        this.directory = directory;
    }

    public void saveWallets(final ArrayList<Wallet> wallets) {
        wallets.forEach(wallet -> {
            try {
                Wallet.save(wallet, Paths.get(directory.getPath(), wallet.getPlayerId().toString() + ".wallet").toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public ArrayList<Wallet> restoreWallets() throws IOException, ClassNotFoundException {
        var wallets = new ArrayList<Wallet>();
        var files = directory.listFiles(pathname -> pathname.isFile() && pathname.toString().endsWith(".wallet") && pathname.canRead());
        if (files != null)
            for (var file : files)
                wallets.add(Wallet.restore(file.getPath()));

        return wallets;
    }
}
