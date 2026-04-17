package fr.valorantage.valomoney.gametest;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.item.ModItems;
import fr.valorantage.valomoney.item.custom.CashItem;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import org.slf4j.Logger;

@GameTestHolder(ValomoneyMod.MODID)
public class CashItemsTests {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String EMPTY_EMPLATE = "empty";

    @GameTest(template = EMPTY_EMPLATE)
    public static void cashItemsValues(GameTestHelper helper) {
        helper.assertValueEqual(((CashItem) ModItems.COIN1.get()).getValue(), 0.1f, "coin1");
        helper.assertValueEqual(((CashItem) ModItems.COIN2.get()).getValue(), 0.5f, "coin2");
        helper.assertValueEqual(((CashItem) ModItems.COIN3.get()).getValue(), 1.0f, "coin3");

        helper.assertValueEqual(((CashItem) ModItems.BILL1.get()).getValue(), 5.0f, "bill1");
        helper.assertValueEqual(((CashItem) ModItems.BILL2.get()).getValue(), 10.0f, "bill2");
        helper.assertValueEqual(((CashItem) ModItems.BILL3.get()).getValue(), 20.0f, "bill3");
        helper.assertValueEqual(((CashItem) ModItems.BILL4.get()).getValue(), 50.0f, "bill4");
        helper.assertValueEqual(((CashItem) ModItems.BILL5.get()).getValue(), 100.0f, "bill5");

        helper.succeed();
    }

    @GameTest(template = EMPTY_EMPLATE)
    public static void cashItemsNameColor(GameTestHelper helper) {
        helper.assertValueEqual(((CashItem) ModItems.COIN1.get()).getNameColor(), 0xd06f03, "coin1 name color");
        helper.assertValueEqual(((CashItem) ModItems.COIN2.get()).getNameColor(), 0x7b8999, "coin2 name color");
        helper.assertValueEqual(((CashItem) ModItems.COIN3.get()).getNameColor(), 0xf2e004, "coin3 name color");

        helper.assertValueEqual(((CashItem) ModItems.BILL1.get()).getNameColor(), 0x4d8c9c, "bill1 name color");
        helper.assertValueEqual(((CashItem) ModItems.BILL2.get()).getNameColor(), 0xa74c42, "bill2 name color");
        helper.assertValueEqual(((CashItem) ModItems.BILL3.get()).getNameColor(), 0x4c959d, "bill3 name color");
        helper.assertValueEqual(((CashItem) ModItems.BILL4.get()).getNameColor(), 0xad7a3c, "bill4 name color");
        helper.assertValueEqual(((CashItem) ModItems.BILL5.get()).getNameColor(), 0x5da049, "bill5 name color");

        helper.succeed();
    }
}
