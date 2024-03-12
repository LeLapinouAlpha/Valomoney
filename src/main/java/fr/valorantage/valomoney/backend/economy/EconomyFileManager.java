package fr.valorantage.valomoney.backend.economy;

import java.io.*;
import java.nio.file.Paths;

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

    public void saveWallet(final Wallet wallet) throws IOException {
        try (var fos = new FileOutputStream(String.format("%s.wallet", wallet.getPlayerId()))) {
            var oos = new ObjectOutputStream(fos);
            oos.writeObject(wallet);
        }
    }

    public Wallet restoreWallet(final String filePath) throws IOException, ClassNotFoundException {
        try (var fis = new FileInputStream(String.valueOf(Paths.get(directory.getPath(), filePath)))) {
            var ois = new ObjectInputStream(fis);
            return (Wallet) ois.readObject();
        }
    }
}
