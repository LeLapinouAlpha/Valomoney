package fr.valorantage.valomoney.network;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.network.packet.PlayerMoneyPayload;
import fr.valorantage.valomoney.network.packet.TransactionPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ValomoneyMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModPayloads {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.NETWORK);

        // TODO: Create a method to refactor those instructions
        // Register TransactionPayload (Client -> Server)
        registrar.playToServer(
                TransactionPayload.TYPE,
                TransactionPayload.STREAM_CODEC,
                ServerPayloadHandler::handleTransactionPayloadOnNetwork
        );

        // Register PlayerMoneyPayload (Server <-> Client)
        registrar.playBidirectional(
                PlayerMoneyPayload.TYPE,
                PlayerMoneyPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handlePlayerMoneyPayloadOnNetwork,
                        ServerPayloadHandler::handlePlayerMoneyPayloadOnNetwork
                )
        );
    }
}
