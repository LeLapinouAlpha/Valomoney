package fr.valorantage.valomoney.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.Comparator;
import java.util.List;

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
            helper.fail(String.format("Expected block entity: %s, Actual: %s", expectedClass, blockEntity.getClass()));
        }
    }

    public static <T> void assertPlayerDataAttachment(GameTestHelper helper, Player player, AttachmentType<T> attachmentType, T expectedData) {
        T actualData = player.getData(attachmentType);
        if (!expectedData.equals(actualData)) {
            helper.fail(String.format("Expected data attachment: %s, Actual: %s", expectedData, actualData), player.getOnPos());
        }
    }

    public static Player makeMockPlayer(GameTestHelper helper, GameType gameMode, BlockPos relativePos) {
        Player fakePlayer = helper.makeMockPlayer(gameMode);
        BlockPos newFakePlayerOnPos = helper.absolutePos(relativePos);
        fakePlayer.teleportTo(newFakePlayerOnPos.getX(), newFakePlayerOnPos.getY(), newFakePlayerOnPos.getZ());
        return fakePlayer;
    }

    public static int findItemSlotInInventory(Inventory inventory, Item item) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).is(item)) {
                return i;
            }
        }
        return -1;
    }

    public static void assertDrops(GameTestHelper helper, AABB aabb, List<ItemStack> expectedDrops) {
        var actualDrops = helper.getLevel().getEntitiesOfClass(ItemEntity.class, aabb).stream()
                .map(ItemEntity::getItem)
                .sorted(Comparator.comparing(ItemStack::toString))
                .toList();
        var sortedExpectedDrops = expectedDrops.stream()
                .sorted(Comparator.comparing(ItemStack::toString))
                .toList();

        helper.assertValueEqual(actualDrops.toString(), sortedExpectedDrops.toString(), "drops");
    }

    public static void assertInventoryEquals(GameTestHelper helper, Inventory inventory, List<ItemStack> expectedItems) {
        var actualItems = inventory.items.stream()
                .filter(stack -> !stack.isEmpty())
                .sorted(Comparator.comparing(ItemStack::toString))
                .toList();
        var sortedExpectedItems = expectedItems.stream()
                .filter(stack -> !stack.isEmpty())
                .sorted(Comparator.comparing(ItemStack::toString))
                .toList();

        helper.assertValueEqual(actualItems.toString(), sortedExpectedItems.toString(), "playerInventoryItems");
    }

    public static void assertInventoryAllMatch(GameTestHelper helper, Inventory inventory, ItemStack itemStack) {
        inventory.items.stream().forEach(stack -> {
           helper.assertValueEqual(stack.toString(), itemStack.toString(), "playerInventoryItem");
        });
    }
}
