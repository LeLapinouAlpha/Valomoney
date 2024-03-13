package fr.valorantage.valomoney.items;

import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemsRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ValomoneyMod.MODID);
    public static final RegistryObject<Item> BILL_FIVE = ITEMS.register("bill_five", BillFiveItem::new);
    public static final RegistryObject<Item> BILL_TEN = ITEMS.register("bill_ten", BillTenItem::new);
    public static final RegistryObject<Item> BILL_TWENTY = ITEMS.register("bill_twenty", BillTwentyItem::new);
    public static final RegistryObject<Item> BILL_FIFTY = ITEMS.register("bill_fifty", BillFiftyItem::new);
    public static final RegistryObject<Item> BILL_HUNDRED = ITEMS.register("bill_hundred", BillHundredItem::new);
    public static final RegistryObject<Item> COIN_FIFTY = ITEMS.register("coin_fifty", CoinFiftyItem::new);
    public static final RegistryObject<Item> COIN_ONE = ITEMS.register("coin_one", CoinOneItem::new);
    public static final RegistryObject<Item> COIN_TWO = ITEMS.register("coin_two", CoinTwoItem::new);
}
