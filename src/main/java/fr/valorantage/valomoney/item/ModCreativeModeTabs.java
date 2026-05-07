package fr.valorantage.valomoney.item;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {
    // Create a Deferred Register to hold CreativeModeTabs which will all be
    // registered under the "valomoney" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, ValomoneyMod.MODID);

    // Creates a creative tab with the id "valomoney:example_tab" for the example
    // item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VALOMONEY_TAB = CREATIVE_MODE_TABS
            .register("valomoney_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.valomoney.items_tab")) // The language key for the title
                                                                                      // of your CreativeModeTab
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.COIN3.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.COIN1.get());
                        output.accept(ModItems.COIN2.get());
                        output.accept(ModItems.COIN3.get());
                        output.accept(ModItems.BILL1.get());
                        output.accept(ModItems.BILL2.get());
                        output.accept(ModItems.BILL3.get());
                        output.accept(ModItems.BILL4.get());
                        output.accept(ModItems.BILL5.get());
                        output.accept(ModItems.BANK_CARD.get());
                        output.accept(ModBlocks.ATM.get());
                        output.accept(ModBlocks.PIGGY_BANK.get());
                        output.accept(ModBlocks.SHOP.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
