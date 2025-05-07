package fr.valorantage.valomoney.network;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.item.custom.BankCardItem;
import fr.valorantage.valomoney.network.packet.ATMDebitPayload;
import fr.valorantage.valomoney.network.packet.PlayerMoneyPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public class ClientPayloadHandler {
    private final static Logger LOGGER = LogUtils.getLogger();

    public static void handleATMDebitPayloadOnNetwork(final ATMDebitPayload data, final IPayloadContext context) {
        // Do something with the data, on the network thread
        LOGGER.debug("Client received ATMDebitPayload: {}", data);
    }

    public static void handlePlayerMoneyPayloadOnNetwork(final PlayerMoneyPayload data, final IPayloadContext context) {
        // Do something with the data, on the network thread
        LOGGER.debug("Client received PlayerMoneyPayload: {}", data);

        BankCardItem.PLAYER_MONEY = data.amount();
    }
}
