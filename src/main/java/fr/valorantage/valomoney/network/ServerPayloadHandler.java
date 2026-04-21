package fr.valorantage.valomoney.network;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.attachment.ModAttachmentTypes;
import fr.valorantage.valomoney.gui.custom.ATMMenu;
import fr.valorantage.valomoney.network.packet.PlayerMoneyPayload;
import fr.valorantage.valomoney.network.packet.TransactionPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public class ServerPayloadHandler {
    private final static Logger LOGGER = LogUtils.getLogger();

    public static void handleTransactionPayloadOnNetwork(final TransactionPayload data, final IPayloadContext context) {
        LOGGER.debug("Network received TransactionPayload: {}", data);

        var player = (ServerPlayer) context.player();

        try {
            var menu = (ATMMenu) player.containerMenu;

            switch (data.kind()) {
                case CREDIT -> menu.credit(data.value());
                case DEBIT -> menu.debit(data.value());
            }
        } catch (ClassCastException e) {
            LOGGER.warn("Player {} sent a TransactionPayload but is not a ServerPlayer", player.getName().getString());
            return;
        }
    }

    public static void handlePlayerMoneyPayloadOnNetwork(final PlayerMoneyPayload data, final IPayloadContext context) {
        // Do something with the data, on the network thread
        LOGGER.debug("Network received PlayerMoneyPayload: {}", data);

        var level = (ServerLevel) context.player().level();
        var player = level.getPlayerByUUID(data.playerUUID());
        if (player != null) {
            float playerMoney = player.getData(ModAttachmentTypes.MONEY);
            context.reply(new PlayerMoneyPayload(data.playerUUID(), playerMoney));
        }
    }
}
