package fr.valorantage.valomoney.network.cache;

import net.neoforged.neoforge.network.PacketDistributor;
import fr.valorantage.valomoney.network.packet.PlayerMoneyPayload;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BankCardClientCache {
    private static final Map<UUID, Float> MONEY = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> LAST_REQUEST = new ConcurrentHashMap<>();
    private static final long THROTTLE_MS = 1000L; // 1 second

    public static Float getCachedMoney(UUID player) {
        return MONEY.get(player);
    }

    public static void putCachedMoney(UUID player, float amount) {
        MONEY.put(player, amount);
    }

    public static void maybeRequestMoney(UUID player) {
        long now = System.currentTimeMillis();
        Long last = LAST_REQUEST.get(player);
        if (last == null || (now - last) >= THROTTLE_MS) {
            LAST_REQUEST.put(player, now);
            PacketDistributor.sendToServer(new PlayerMoneyPayload(player, -1f));
        }
    }
}
