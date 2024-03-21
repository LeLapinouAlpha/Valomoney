package fr.valorantage.valomoney.items;

import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemsRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ValomoneyMod.MODID);
    public static final RegistryObject<Item> BANKNOTE_FIVE = ITEMS.register("banknote_five", BankNoteFiveItem::new);
    public static final RegistryObject<Item> BANKNOTE_TEN = ITEMS.register("banknote_ten", BankNoteTenItem::new);
    public static final RegistryObject<Item> BANKNOTE_TWENTY = ITEMS.register("banknote_twenty", BankNoteTwentyItem::new);
    public static final RegistryObject<Item> BANKNOTE_FIFTY = ITEMS.register("banknote_fifty", BankNoteFiftyItem::new);
    public static final RegistryObject<Item> BANKNOTE_HUNDRED = ITEMS.register("banknote_hundred", BankNoteHundredItem::new);
    public static final RegistryObject<Item> COIN_FIFTY = ITEMS.register("coin_fifty", CoinFiftyItem::new);
    public static final RegistryObject<Item> COIN_ONE = ITEMS.register("coin_one", CoinOneItem::new);
    public static final RegistryObject<Item> COIN_TWO = ITEMS.register("coin_two", CoinTwoItem::new);
}
