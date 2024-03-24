package fr.valorantage.valomoney.tabs;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.blocks.custom.BlocksRegister;
import fr.valorantage.valomoney.items.ItemsRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class CreativeModeTabsRegister {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ValomoneyMod.MODID);

    public static final RegistryObject<CreativeModeTab> VALOMONEY_TAB = CREATIVE_MODE_TABS.register("valomoney_tab", () -> CreativeModeTab.builder()
            .title(Component.literal("Valomoney"))
            .icon(() -> ItemsRegister.BANKNOTE_FIVE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ItemsRegister.BANKNOTE_FIVE.get());
                output.accept(ItemsRegister.BANKNOTE_TEN.get());
                output.accept(ItemsRegister.BANKNOTE_TWENTY.get());
                output.accept(ItemsRegister.BANKNOTE_FIFTY.get());
                output.accept(ItemsRegister.BANKNOTE_HUNDRED.get());
                output.accept(ItemsRegister.COIN_FIFTY.get());
                output.accept(ItemsRegister.COIN_ONE.get());
                output.accept(ItemsRegister.COIN_TWO.get());
                output.accept(ItemsRegister.SHOP_BLOCK_ITEM.get());
            }).build());
}
