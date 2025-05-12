package fr.valorantage.valomoney.gametest;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.block.entity.custom.ATMBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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

    @GameTest
    public static void basicATMInteraction(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();

        // Place ATM block
        BlockPos pos = helper.absolutePos(new BlockPos(0, 2, 0));
        level.setBlock(pos, ModBlocks.ATM.get().defaultBlockState(), 3);

        // Check block
        BlockState state = level.getBlockState(pos);
        if (!state.is(ModBlocks.ATM.get())) {
            helper.fail("This block is not an ATM", pos);
        }

        // Check block entity
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ATMBlockEntity)) {
            helper.fail("This block entity is not an ATM block entity", pos);
        }

        // Create a fake player and use ATM to open the GUI
        Player fakePlayer = helper.makeMockPlayer(GameType.SURVIVAL);
        InteractionResult topInteractionResult = state.useWithoutItem(level, fakePlayer, new BlockHitResult(
                Vec3.atCenterOf(pos), Direction.UP, pos, false));
        if (topInteractionResult != InteractionResult.SUCCESS) {
            helper.fail("Failed to open ATM GUI from " + Direction.UP + " direction", pos);
        }

        helper.succeed();
    }
}