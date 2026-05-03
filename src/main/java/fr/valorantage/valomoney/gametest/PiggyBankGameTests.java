package fr.valorantage.valomoney.gametest;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.block.entity.custom.PiggyBankBlockEntity;
import fr.valorantage.valomoney.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import org.slf4j.Logger;

import java.util.List;

@GameTestHolder(ValomoneyMod.MODID)
public class PiggyBankGameTests {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String BASICS_TEMPLATE = "basics";

    private static BlockPos placePiggyBankAndCheck(GameTestHelper helper, BlockPos relativePos) {
        BlockPos pos = GameTestUtils.placeBlock(helper, relativePos, ModBlocks.PIGGY_BANK.get());
        helper.assertBlockPresent(ModBlocks.PIGGY_BANK.get(), relativePos);
        GameTestUtils.assertBlockEntity(helper, pos, PiggyBankBlockEntity.class);
        return pos;
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void piggyBankFalling(GameTestHelper helper) {
        // Place piggy bank in the air
        BlockPos piggyPos = placePiggyBankAndCheck(helper, new BlockPos(1, 4, 1));
        // Remove block below to trigger fall
        helper.getLevel().setBlock(helper.absolutePos(new BlockPos(1, 3, 1)), Blocks.AIR.defaultBlockState(), 3);
        // Wait a few ticks for it to fall and break
        helper.runAfterDelay(5, () -> {
            helper.assertBlockNotPresent(ModBlocks.PIGGY_BANK.get(), new BlockPos(1, 4, 1));
            helper.succeed();
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void singleCashItemInsertion(GameTestHelper helper) {
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 1), 0.f);
        BlockPos piggyPos = placePiggyBankAndCheck(helper, new BlockPos(1, 2, 1));
        BlockState state = helper.getLevel().getBlockState(piggyPos);
        ItemStack coinStack = new ItemStack(ModItems.COIN3.get(), 5);

        ItemInteractionResult result = state.useItemOn(coinStack, helper.getLevel(), fakePlayer,
                InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(piggyPos), Direction.NORTH, piggyPos, false));
        helper.assertValueEqual(result, ItemInteractionResult.SUCCESS, "piggyBankInsertResult");

        PiggyBankBlockEntity entity = (PiggyBankBlockEntity) helper.getLevel().getBlockEntity(piggyPos);
        var slot = entity.inventory.getStackInSlot(0);
        helper.assertTrue(slot.getCount() == 1, String
                .format("Piggy bank should have 1 coin, but has %d", slot.getCount()));
        helper.succeed();
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void dropsOnBreak(GameTestHelper helper) {
        BlockPos piggyPos = placePiggyBankAndCheck(helper, new BlockPos(1, 2, 1));
        // Insert coins
        PiggyBankBlockEntity entity = (PiggyBankBlockEntity) helper.getLevel().getBlockEntity(piggyPos);
        entity.inventory.insertItem(0, new ItemStack(ModItems.COIN3.get(), 3), false);
        // Break piggy bank
        helper.getLevel().destroyBlock(piggyPos, true);
        // Check for drops
        helper.runAfterDelay(5, () -> {
            GameTestUtils.assertDrops(helper, new AABB(piggyPos), List.of(
                    new ItemStack(ModBlocks.PIGGY_BANK.get()),
                    new ItemStack(ModItems.COIN3.get(), 3)));
            helper.succeed();
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void dropsOnLand(GameTestHelper helper) {
        BlockPos piggyPos = placePiggyBankAndCheck(helper, new BlockPos(1, 4, 1));
        // Insert coins before fall
        PiggyBankBlockEntity entity = (PiggyBankBlockEntity) helper.getLevel().getBlockEntity(piggyPos);
        entity.inventory.insertItem(0, new ItemStack(ModItems.COIN3.get(), 2), false);
        // Remove block below to trigger fall
        helper.getLevel().setBlock(helper.absolutePos(new BlockPos(1, 3, 1)), Blocks.AIR.defaultBlockState(), 3);

        // Expand AABB to cover a larger region below the original position (3x6x3)
        BlockPos min = piggyPos.offset(-1, -2, -1);
        BlockPos max = piggyPos.offset(1, 3, 1);
        AABB region = new AABB(
                min.getX(), min.getY(), min.getZ(),
                max.getX() + 1, max.getY() + 1, max.getZ() + 1);

        // Wait for fall and break
        helper.runAfterDelay(5,
                () -> helper.succeedIf(() -> GameTestUtils.assertDrops(helper, region, List.of(
                        new ItemStack(ModItems.COIN3.get(), 2)))));
    }
}
