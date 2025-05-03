package fr.valorantage.valomoney.network;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.network.packet.ATMCreditPayload;
import fr.valorantage.valomoney.network.packet.ATMDebitPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ValomoneyMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModPayloads {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.NETWORK);

        // Register ATMDebitPayload (Only Client -> Server)
        registrar.playToServer(
                ATMDebitPayload.TYPE,
                ATMDebitPayload.STREAM_CODEC,
                ServerPayloadHandler::handleATMDebitPayloadOnNetwork
        );

        // Register ATMCreditPayload (Only Client -> Server)
        registrar.playToServer(
                ATMCreditPayload.TYPE,
                ATMCreditPayload.STREAM_CODEC,
                ServerPayloadHandler::handleATMCreditPayloadOnNetwork
        );
    }
}
