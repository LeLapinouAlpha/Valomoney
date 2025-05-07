package fr.valorantage.valomoney.gametest;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import org.slf4j.Logger;

@GameTestHolder(ValomoneyMod.MODID)
public class GameTests {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    @GameTest
    public static void exampleTest(GameTestHelper helper) {
        helper.succeed();
    }
}