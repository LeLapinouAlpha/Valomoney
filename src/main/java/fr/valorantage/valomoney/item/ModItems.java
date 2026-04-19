package fr.valorantage.valomoney.item;

import fr.valorantage.valomoney.Config;
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
    // Create a Deferred Register to hold Items which will all be registered under
    // the "valomoney" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ValomoneyMod.MODID);

    // Creates cash items
    public static final DeferredItem<Item> COIN1 = ITEMS.register("coin1",
            () -> new CashItem(Config.COIN1_VALUE::get, 0xd06f03));
    public static final DeferredItem<Item> COIN2 = ITEMS.register("coin2",
            () -> new CashItem(Config.COIN2_VALUE::get, 0x7b8999));
    public static final DeferredItem<Item> COIN3 = ITEMS.register("coin3",
            () -> new CashItem(Config.COIN3_VALUE::get, 0xf2e004));

    public static final DeferredItem<Item> BILL1 = ITEMS.register("bill1",
            () -> new CashItem(Config.BILL1_VALUE::get, 0x4d8c9c));
    public static final DeferredItem<Item> BILL2 = ITEMS.register("bill2",
            () -> new CashItem(Config.BILL2_VALUE::get, 0xa74c42));
    public static final DeferredItem<Item> BILL3 = ITEMS.register("bill3",
            () -> new CashItem(Config.BILL3_VALUE::get, 0x4c959d));
    public static final DeferredItem<Item> BILL4 = ITEMS.register("bill4",
            () -> new CashItem(Config.BILL4_VALUE::get, 0xad7a3c));
    public static final DeferredItem<Item> BILL5 = ITEMS.register("bill5",
            () -> new CashItem(Config.BILL5_VALUE::get, 0x5da049));

    // Creates ATM block item
    public static final DeferredItem<BlockItem> ATM_ITEM = ITEMS.register("atm",
            () -> new BlockItem(ModBlocks.ATM.get(), new Item.Properties()));

    // Creates bank card item
    public static final DeferredItem<Item> BANK_CARD = ITEMS.register("bank_card", BankCardItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
