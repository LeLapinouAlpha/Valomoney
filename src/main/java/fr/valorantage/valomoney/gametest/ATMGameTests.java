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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import org.slf4j.Logger;

@GameTestHolder(ValomoneyMod.MODID)
public class ATMGameTests {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    @GameTest
    public static void basicInteraction(GameTestHelper helper) {
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

        // Create a fake player, teleport it to structure and make it use ATM to open the GUI in all directions
        Player fakePlayer = helper.makeMockPlayer(GameType.SURVIVAL);
        BlockPos newFakePlayerOnPos = helper.absolutePos(new BlockPos(1, 2, 0));
        fakePlayer.teleportTo(newFakePlayerOnPos.getX(), newFakePlayerOnPos.getY(), newFakePlayerOnPos.getZ());
        for (Direction direction : Direction.values()) {
            var usedItemStack = ItemStack.EMPTY;
            ItemInteractionResult interactionResult = state.useItemOn(usedItemStack, level, fakePlayer, InteractionHand.MAIN_HAND, new BlockHitResult(
                    Vec3.atCenterOf(pos), direction, pos, false));
            if (interactionResult != ItemInteractionResult.SUCCESS) {
                helper.fail("Failed to open ATM GUI from " + direction + " direction" + " using item stack" + usedItemStack, pos);
            }
        }

        helper.succeed();
    }
}