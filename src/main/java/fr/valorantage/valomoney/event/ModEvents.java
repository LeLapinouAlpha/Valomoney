package fr.valorantage.valomoney.event;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

@EventBusSubscriber(modid = ValomoneyMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        var genericTrades = event.getGenericTrades();
        var rareTrades = event.getRareTrades();

        genericTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.COPPER_INGOT, 1),
                new ItemStack(ModItems.COIN1.get(), 1),
                64,
                9,
                0.02f));

        genericTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.IRON_INGOT, 1),
                new ItemStack(ModItems.COIN2.get(), 1),
                64,
                9,
                0.02f));

        genericTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.GOLD_INGOT, 1),
                new ItemStack(ModItems.COIN3.get(), 1),
                64,
                9,
                0.02f));

        rareTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 1),
                new ItemStack(ModItems.BILL1.get(), 1),
                32,
                12,
                0.02f));

        rareTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 2),
                new ItemStack(ModItems.BILL2.get(), 1),
                32,
                12,
                0.02f));

        rareTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 3),
                new ItemStack(ModItems.BILL3.get(), 1),
                32,
                12,
                0.02f));

        rareTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 4),
                new ItemStack(ModItems.BILL4.get(), 1),
                32,
                12,
                0.02f));

        rareTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 5),
                new ItemStack(ModItems.BILL5.get(), 1),
                32,
                12,
                0.02f));
    }
}
