package fr.valorantage.valomoney.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GameTestUtils {
    public static BlockPos placeBlock(GameTestHelper helper, ServerLevel level, BlockPos relativePos, Block block, int flags) {
        BlockPos pos = helper.absolutePos(relativePos);
        level.setBlock(pos, block.defaultBlockState(), flags);
        return pos;
    }

    public static BlockPos placeBlock(GameTestHelper helper, ServerLevel level, BlockPos relativePos, Block block) {
        return placeBlock(helper, level, relativePos, block, 3);
    }

    public static void assertBlock(GameTestHelper helper, BlockPos pos, BlockState state, Block expectedBlock) {
        if (!state.is(expectedBlock)) {
            helper.fail(String.format("Expected block: %s, Actual block: %s", expectedBlock, state.getBlock()), pos);
        }
    }

    // FIXME: check for block entity to be non-null
    public static <T extends BlockEntity> void assertBlockEntity(GameTestHelper helper, ServerLevel level, BlockPos pos, Class<T> expectedClass) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(expectedClass.isInstance(blockEntity))) {
            helper.fail(String.format("Expected block entity: %s, Actual block entity: %s", expectedClass, blockEntity.getClass()), pos);
        }
    }

    public static Player makeMockPlayer(GameTestHelper helper, GameType gameMode, BlockPos relativePos) {
        Player fakePlayer = helper.makeMockPlayer(gameMode);
        BlockPos newFakePlayerOnPos = helper.absolutePos(relativePos);
        fakePlayer.teleportTo(newFakePlayerOnPos.getX(), newFakePlayerOnPos.getY(), newFakePlayerOnPos.getZ());
        return fakePlayer;
    }
}
