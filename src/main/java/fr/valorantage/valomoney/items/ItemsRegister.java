package fr.valorantage.valomoney.items;

import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(ValomoneyMod.MODID)
public final class ItemsRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ValomoneyMod.MODID);
    public static final RegistryObject<Item> BILL_FIVE = ITEMS.register("bill_five", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BILL_TEN = ITEMS.register("bill_ten", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BILL_TWENTY = ITEMS.register("bill_twenty", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BILL_FIFTY = ITEMS.register("bill_fifty", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BILL_HUNDRED = ITEMS.register("bill_hundred", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COIN_FIFTY = ITEMS.register("coin_fifty", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COIN_ONE = ITEMS.register("coin_one", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COIN_TWO = ITEMS.register("coin_two", () -> new Item(new Item.Properties()));
}
