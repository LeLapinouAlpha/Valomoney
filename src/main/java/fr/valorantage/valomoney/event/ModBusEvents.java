package fr.valorantage.valomoney.event;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.block.entity.ModBlockEntities;
import fr.valorantage.valomoney.block.entity.custom.ATMBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = ValomoneyMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.ATM_BE.get(),
                ATMBlockEntity::getItemHandler);
    }
}
