package fr.valorantage.valomoney.gametest;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.block.entity.custom.ATMBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
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
    public static void basicInteractionWithItem(GameTestHelper helper) {
        // Place ATM block
        BlockPos pos = GameTestUtils.placeBlock(helper, new BlockPos(0, 2, 0), ModBlocks.ATM.get());

        // Check block
        GameTestUtils.assertBlock(helper, pos, ModBlocks.ATM.get());

        // Check block entity
        GameTestUtils.assertBlockEntity(helper, pos, ATMBlockEntity.class);

        // Create a fake player, teleport it to structure and make it use ATM to open the GUI in all directions
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(0, 2, 0));
        BlockState state = helper.getLevel().getBlockState(pos);
        var usedItemStack = new ItemStack(Blocks.STONE, 64);
        ItemInteractionResult interactionResult = state.useItemOn(usedItemStack, helper.getLevel(), fakePlayer, InteractionHand.MAIN_HAND, new BlockHitResult(
                Vec3.atCenterOf(pos), Direction.NORTH, pos, false));
        if (interactionResult != ItemInteractionResult.SUCCESS) {
            helper.fail("Failed to open ATM GUI using item stack" + usedItemStack, pos);
        }

        helper.succeed();
    }

    @GameTest
    public static void basicInteractionWithoutItem(GameTestHelper helper) {
        // Place ATM block
        BlockPos pos = GameTestUtils.placeBlock(helper, new BlockPos(0, 2, 0), ModBlocks.ATM.get());

        // Check block
        GameTestUtils.assertBlock(helper, pos, ModBlocks.ATM.get());

        // Check block entity
        GameTestUtils.assertBlockEntity(helper, pos, ATMBlockEntity.class);

        // Create a fake player, teleport it to structure and make it use ATM to open the GUI in all directions
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(0, 2, 0));
        BlockState state = helper.getLevel().getBlockState(pos);
        InteractionResult interactionResult = state.useWithoutItem(helper.getLevel(), fakePlayer, new BlockHitResult(
                Vec3.atCenterOf(pos), Direction.NORTH, pos, false));
        if (interactionResult != InteractionResult.SUCCESS) {
            helper.fail("Failed to open ATM GUI", pos);
        }

        helper.succeed();
    }
}