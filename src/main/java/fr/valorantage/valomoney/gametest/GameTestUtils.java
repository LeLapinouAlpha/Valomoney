package fr.valorantage.valomoney.gametest;

import fr.valorantage.valomoney.attachment.ModAttachmentTypes;
import fr.valorantage.valomoney.gui.custom.ATMMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
    public static <T extends BlockEntity> void assertBlockEntity(GameTestHelper helper, BlockPos pos,
            Class<T> expectedClass) {
        BlockEntity blockEntity = helper.getLevel().getBlockEntity(pos);
        if (!(expectedClass.isInstance(blockEntity))) {
            helper.fail(String.format("Expected block entity: %s, Actual: %s", expectedClass, blockEntity.getClass()));
        }
    }

    public static <T> void assertPlayerDataAttachment(GameTestHelper helper, Player player,
            AttachmentType<T> attachmentType, String attachmentName, T expectedData) {
        T actualData = player.getData(attachmentType);
        helper.assertValueEqual(actualData, expectedData, attachmentName);
    }

    public static void assertPlayerDataAttachment(GameTestHelper helper, Player player,
            AttachmentType<Float> attachmentType, String attachmentName, Float expectedValue, float epsilon) {
        Float actualValue = player.getData(attachmentType);
        helper.assertTrue(Math.abs(actualValue - expectedValue) < epsilon,
                String.format("%s expected: %f, actual: %f", attachmentName, expectedValue, actualValue));
    }

    public static void assertPlayersMoney(GameTestHelper helper, Player player, float money) {
        assertPlayerDataAttachment(helper, player, ModAttachmentTypes.MONEY.get(), "playersMoney", money, 1e-3f);
    }

    public static Player makeMockPlayer(GameTestHelper helper, GameType gameMode, BlockPos relativePos,
            float initialBalance) {
        Player fakePlayer = helper.makeMockPlayer(gameMode);
        BlockPos newFakePlayerOnPos = helper.absolutePos(relativePos);
        fakePlayer.teleportTo(newFakePlayerOnPos.getX(), newFakePlayerOnPos.getY(), newFakePlayerOnPos.getZ());
        fakePlayer.setData(ModAttachmentTypes.MONEY.get(), initialBalance);
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

    public static int findItemSlotInMenu(AbstractContainerMenu menu, Item item) {
        return menu.slots.stream()
                .filter(slot -> slot.getItem().is(item))
                .findFirst()
                .map(slot -> slot.index)
                .orElse(-1);
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

    public static void assertInventoryEquals(GameTestHelper helper, Inventory inventory,
            List<ItemStack> expectedItems) {
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

    public static void assertItemStackEquals(GameTestHelper helper, ItemStack actual, ItemStack expected, String name) {
        helper.assertValueEqual(actual.toString(), expected.toString(), name);
    }

    public static void assertInventoryAllMatch(GameTestHelper helper, Inventory inventory,
            ItemStack expectedItemStack) {
        inventory.items.stream()
                .forEach(actualItemStack -> assertItemStackEquals(helper, actualItemStack, expectedItemStack,
                        "playerInventoryItem"));
    }

    public static void assertQuickMoveStack(GameTestHelper helper, Player player, AbstractContainerMenu menu,
            int srcIndex, ItemStack expectedSrcItemStack, int dstIndex, ItemStack expectedDstItemStack) {
        menu.quickMoveStack(player, srcIndex);

        var actualSrcSlotItemStack = menu.slots.get(srcIndex).getItem();
        assertItemStackEquals(helper, actualSrcSlotItemStack, expectedSrcItemStack, "menuSrcSlotItemStack");

        var actualDstSlotItemStack = menu.slots.get(dstIndex).getItem();
        assertItemStackEquals(helper, actualDstSlotItemStack, expectedDstItemStack, "menuDstSlotItemStack");
    }

    /**
     * Asserts that the ATM tile inventory (slots 1-4) contains the expected
     * ItemStacks (ignoring empty slots and order).
     * 
     * @param helper        GameTestHelper
     * @param atmMenu       ATMMenu instance
     * @param expectedItems List of expected ItemStacks
     */
    public static void assertTileInventoryEquals(GameTestHelper helper, ATMMenu atmMenu,
            List<ItemStack> expectedItems) {
        // ATM tile inventory slots are usually 1-4 (skip slot 0, which is for the bank
        // card)
        var actualItems = java.util.stream.IntStream.range(1, 5)
                .mapToObj(new java.util.function.IntFunction<ItemStack>() {
                    @Override
                    public ItemStack apply(int i) {
                        return atmMenu.getTileInventorySlot(i).getItem();
                    }
                })
                .filter(stack -> !stack.isEmpty())
                .sorted(Comparator.comparing(ItemStack::toString))
                .toList();
        var sortedExpectedItems = expectedItems.stream()
                .filter(stack -> !stack.isEmpty())
                .sorted(Comparator.comparing(ItemStack::toString))
                .toList();
        helper.assertValueEqual(actualItems.toString(), sortedExpectedItems.toString(), "tileInventoryItems");
    }
}
