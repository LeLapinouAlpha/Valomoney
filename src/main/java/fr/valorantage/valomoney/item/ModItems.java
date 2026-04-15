package fr.valorantage.valomoney.item;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.item.custom.BankCardItem;
import fr.valorantage.valomoney.item.custom.CashItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    // Create a Deferred Register to hold Items which will all be registered under the "valomoney" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ValomoneyMod.MODID);

    // Creates cash items
    public static final DeferredItem<Item> COIN1 = ITEMS.register("coin1", () -> new CashItem(0.1f));
    public static final DeferredItem<Item> COIN2 = ITEMS.register("coin2", () -> new CashItem(0.5f));
    public static final DeferredItem<Item> COIN3 = ITEMS.register("coin3", () -> new CashItem(1.0f));
    public static final DeferredItem<Item> BILL1 = ITEMS.register("bill1", () -> new CashItem(5.0f));
    public static final DeferredItem<Item> BILL2 = ITEMS.register("bill2", () -> new CashItem(10.0f));
    public static final DeferredItem<Item> BILL3 = ITEMS.register("bill3", () -> new CashItem(20.0f));
    public static final DeferredItem<Item> BILL4 = ITEMS.register("bill4", () -> new CashItem(50.0f));
    public static final DeferredItem<Item> BILL5 = ITEMS.register("bill5", () -> new CashItem(100.0f));

    // Creates ATM block item
    public static final DeferredItem<BlockItem> ATM_ITEM = ITEMS.register("atm", () -> new BlockItem(ModBlocks.ATM.get(), new Item.Properties()));

    // Creates bank card item
    public static final DeferredItem<Item> BANK_CARD = ITEMS.register("bank_card", BankCardItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
