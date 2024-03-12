package fr.valorantage.valomoney.backend.economy;

import java.io.File;

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
}
