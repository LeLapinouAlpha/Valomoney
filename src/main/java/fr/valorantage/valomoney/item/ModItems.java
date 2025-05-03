package fr.valorantage.valomoney.item;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.item.custom.MonetaryItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    // Create a Deferred Register to hold Items which will all be registered under the "valomoney" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ValomoneyMod.MODID);

    // Creates monetary items
    public static final DeferredItem<Item> COIN = ITEMS.register("coin", () -> new MonetaryItem(1.0f));
    public static final DeferredItem<Item> BILL = ITEMS.register("bill", () -> new MonetaryItem(5.0f));

    // Creates ATM block item
    public static final DeferredItem<BlockItem> ATM_ITEM = ITEMS.register("atm", () -> new BlockItem(ModBlocks.ATM.get(), new Item.Properties()));

    // Creates bank card item
    public static final DeferredItem<Item> BANK_CARD = ITEMS.registerSimpleItem("bank_card");

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
