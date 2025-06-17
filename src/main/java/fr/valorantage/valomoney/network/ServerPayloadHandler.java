package fr.valorantage.valomoney.network;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.attachment.ModAttachmentTypes;
import fr.valorantage.valomoney.gui.custom.ATMMenu;
import fr.valorantage.valomoney.network.packet.PlayerMoneyPayload;
import fr.valorantage.valomoney.network.packet.TransactionPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public class ServerPayloadHandler {
    private final static Logger LOGGER = LogUtils.getLogger();

    public static void handleTransactionPayloadOnNetwork(final TransactionPayload data, final IPayloadContext context) {
        LOGGER.debug("Server received TransactionPayload: {}", data);
        // TODO: implement this method
    }

    public static void handlePlayerMoneyPayloadOnNetwork(final PlayerMoneyPayload data, final IPayloadContext context) {
        // Do something with the data, on the network thread
        LOGGER.debug("Server received PlayerMoneyPayload: {}", data);

        ServerPlayer player = (ServerPlayer) context.player();
        float playerMoney = player.getData(ModAttachmentTypes.MONEY);
        context.reply(new PlayerMoneyPayload(playerMoney));
    }
}
