package fr.valorantage.valomoney.backend.economy;

import java.io.*;
import java.nio.file.Paths;

public final class EconomyFileManager {
    private final File directory;

    private String[] walletFiles;

    private int currentWalletIndex;

    public EconomyFileManager(final String directoryPath) {
        var directory = new File(directoryPath);
        if (!directory.exists()) {
            if (!directory.mkdir())
                throw new RuntimeException("Cannot create directory");
        } else if (!directory.isDirectory())
            throw new IllegalArgumentException(String.format("%s is not a directory", directoryPath));

        this.directory = directory;
        walletFiles = null;
        currentWalletIndex = 0;
    }

    public void saveWallet(final Wallet wallet) throws IOException {
        var walletFilename = String.format("%s.wallet", wallet.getPlayerId());
        var walletPath = String.valueOf(Paths.get(directory.getPath(), walletFilename));
        try (var fos = new FileOutputStream(walletPath)) {
            var oos = new ObjectOutputStream(fos);
            oos.writeObject(wallet);
        }
    }

    private void storeWalletFiles() {
        walletFiles = directory.list((dirname, filename) -> filename.endsWith(".wallet"));
    }

    public Wallet restoreWallet(final String filePath) throws IOException, ClassNotFoundException {
        try (var fis = new FileInputStream(filePath)) {
            var ois = new ObjectInputStream(fis);
            return (Wallet) ois.readObject();
        }
    }

    public Wallet restoreWallet() throws IOException, ClassNotFoundException {
        if (walletFiles == null)
            storeWalletFiles();
        if (currentWalletIndex >= walletFiles.length)
            return null;
        return restoreWallet(Paths.get(directory.getPath(), walletFiles[currentWalletIndex++]).toString());
    }

}
