package fr.valorantage.valomoney.gametest;

import fr.valorantage.valomoney.exception.GameTestException;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GameTestUtils {
    public static BlockPos placeBlock(GameTestHelper helper, BlockPos relativePos, Block block, int flags) {
        BlockPos pos = helper.absolutePos(relativePos);
        helper.getLevel().setBlock(pos, block.defaultBlockState(), flags);
        return pos;
    }

    public static BlockPos placeBlock(GameTestHelper helper, BlockPos relativePos, Block block) {
        return placeBlock(helper, relativePos, block, 3);
    }

    // FIXME: check for block entity to be non-null
    public static <T extends BlockEntity> void assertBlockEntity(GameTestHelper helper, BlockPos pos, Class<T> expectedClass) {
        BlockEntity blockEntity = helper.getLevel().getBlockEntity(pos);
        if (!(expectedClass.isInstance(blockEntity))) {
            throw new GameTestException(expectedClass, blockEntity.getClass(), pos);
        }
    }

    public static Player makeMockPlayer(GameTestHelper helper, GameType gameMode, BlockPos relativePos) {
        Player fakePlayer = helper.makeMockPlayer(gameMode);
        BlockPos newFakePlayerOnPos = helper.absolutePos(relativePos);
        fakePlayer.teleportTo(newFakePlayerOnPos.getX(), newFakePlayerOnPos.getY(), newFakePlayerOnPos.getZ());
        return fakePlayer;
    }
}
