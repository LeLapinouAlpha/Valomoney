package fr.valorantage.valomoney.item;

import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    // Create a Deferred Register to hold Items which will all be registered under the "valomoney" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ValomoneyMod.MODID);

    // Creates a new BlockItem with the id "valomoney:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", ValomoneyMod.EXAMPLE_BLOCK);
    // Creates a new food item with the id "valomoney:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // Creates a coin item
    public static final DeferredItem<Item> COIN = ITEMS.register("coin", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
