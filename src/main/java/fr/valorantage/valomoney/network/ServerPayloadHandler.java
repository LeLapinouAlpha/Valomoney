package fr.valorantage.valomoney.network;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.gui.custom.ATMMenu;
import fr.valorantage.valomoney.network.packet.ATMDebitPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public class ServerPayloadHandler {
    private final static Logger LOGGER = LogUtils.getLogger();

    public static void handleATMDebitPayloadOnNetwork(final ATMDebitPayload data, final IPayloadContext context) {
        // Do something with the data, on the network thread
        LOGGER.debug("Server received ATMDebitPayload: {}", data);

        ServerPlayer player = (ServerPlayer) context.player();
        ATMMenu atmMenu = (ATMMenu) player.containerMenu;
        atmMenu.debit(data.amount());
    }
}
