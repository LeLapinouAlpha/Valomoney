package fr.valorantage.valomoney.tabs;

import fr.valorantage.valomoney.ValomoneyMod;
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
            .icon(() -> ItemsRegister.BILL_FIVE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ItemsRegister.BILL_FIVE.get());
                output.accept(ItemsRegister.BILL_TEN.get());
                output.accept(ItemsRegister.BILL_TWENTY.get());
                output.accept(ItemsRegister.BILL_FIFTY.get());
                output.accept(ItemsRegister.BILL_HUNDRED.get());
            }).build());
}
