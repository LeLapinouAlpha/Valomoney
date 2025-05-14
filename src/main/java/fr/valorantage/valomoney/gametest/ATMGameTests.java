package fr.valorantage.valomoney.gametest;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.attachment.ModAttachmentTypes;
import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.block.entity.custom.ATMBlockEntity;
import fr.valorantage.valomoney.gui.custom.ATMMenu;
import fr.valorantage.valomoney.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
public class ATMGameTests {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String BASICS_TEMPLATE = "basics";

    private static BlockPos placeATMAndCheck(GameTestHelper helper, BlockPos relativePos) {
        BlockPos pos = GameTestUtils.placeBlock(helper, relativePos, ModBlocks.ATM.get());

        // Check block & block entity
        helper.assertBlockPresent(ModBlocks.ATM.get(), relativePos);
        GameTestUtils.assertBlockEntity(helper, pos, ATMBlockEntity.class);

        return pos;
    }

    private static ATMMenu openATMMenu(GameTestHelper helper, BlockPos atmPos, Player player) {
        ATMBlockEntity atmBlockEntity = (ATMBlockEntity) helper.getLevel().getBlockEntity(atmPos);
        MenuProvider provider = new SimpleMenuProvider(atmBlockEntity, atmBlockEntity.getDisplayName());
        AbstractContainerMenu menu = provider.createMenu(0, player.getInventory(), player);
        if (menu instanceof ATMMenu atmMenu) {
            return atmMenu;
        } else {
            helper.fail("Could not open ATM menu", atmPos);
            return null;
        }
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void basicInteractionWithItem(GameTestHelper helper) {
        // Place ATM block and check for block type and block entity type
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0));

        // Create a fake player, teleport it to structure and make it use ATM to open the GUI
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0));
        BlockState state = helper.getLevel().getBlockState(atmPos);
        var usedItemStack = new ItemStack(Blocks.STONE, 64);
        ItemInteractionResult interactionResult = state.useItemOn(usedItemStack, helper.getLevel(), fakePlayer, InteractionHand.MAIN_HAND, new BlockHitResult(
                Vec3.atCenterOf(atmPos), Direction.NORTH, atmPos, false));
        if (interactionResult != ItemInteractionResult.SUCCESS) {
            helper.fail("Failed to open ATM GUI using item stack" + usedItemStack, atmPos);
        }

        helper.succeed();
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void basicInteractionWithoutItem(GameTestHelper helper) {
        // Place ATM block and check for block type and block entity type
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0));

        // Create a fake player, teleport it to structure and make it use ATM to open the GUI
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0));
        BlockState state = helper.getLevel().getBlockState(atmPos);
        InteractionResult interactionResult = state.useWithoutItem(helper.getLevel(), fakePlayer, new BlockHitResult(
                Vec3.atCenterOf(atmPos), Direction.NORTH, atmPos, false));
        if (interactionResult != InteractionResult.SUCCESS) {
            helper.fail("Failed to open ATM GUI", atmPos);
        }

        helper.succeed();
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void quickMoveBankCard(GameTestHelper helper) {
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0));

        // Create a fake player and teleport it to structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0));

        // Add bank card in fake player's inventory
        fakePlayer.getInventory().add(new ItemStack(ModItems.BANK_CARD.get()));

        // Reset fake player's balance and open ATM menu for it
        fakePlayer.setData(ModAttachmentTypes.MONEY.get(), 0.f);
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);
        // Move the bank card from fake player's inventory to the ATM inventory using 'quickMoveStack' method
        int bankCardSlot = GameTestUtils.findItemSlotInInventory(fakePlayer.getInventory(), ModItems.BANK_CARD.get());

        if (bankCardSlot >= 0) {
            atmMenu.quickMoveStack(fakePlayer, bankCardSlot);
            var atmSlotItemStack = atmMenu.slots.getFirst().getItem();
            helper.assertValueEqual(atmSlotItemStack.getItem(), ModItems.BANK_CARD.get(), "atmFirstSlotItem");
            helper.assertValueEqual(fakePlayer.getInventory().getItem(bankCardSlot), ItemStack.EMPTY, "bankCardSlotItem");
        } else {
            helper.fail("No bank card found in player's inventory", fakePlayer.getOnPos());
        }
        helper.succeed();
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void quickMoveOtherItem(GameTestHelper helper) {
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0));

        // Create a fake player and teleport it to structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0));

        // Add bank card in fake player's inventory
        fakePlayer.getInventory().add(new ItemStack(Blocks.STONE));

        // Reset fake player's balance and open ATM menu for it
        fakePlayer.setData(ModAttachmentTypes.MONEY.get(), 0.f);
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);
        // Move the bank card from fake player's inventory to the ATM inventory using 'quickMoveStack' method
        int stoneSlot = GameTestUtils.findItemSlotInInventory(fakePlayer.getInventory(), Blocks.STONE.asItem());

        if (stoneSlot >= 0) {
            atmMenu.quickMoveStack(fakePlayer, stoneSlot);
            var atmSlotItemStack = atmMenu.slots.getFirst().getItem();
            helper.assertValueEqual(atmSlotItemStack.getItem(), ItemStack.EMPTY.getItem(), "atmFirstSlotItem");
            helper.assertValueEqual(fakePlayer.getInventory().getItem(stoneSlot).getItem(), Blocks.STONE.asItem(), "stoneSlotItem");
        } else {
            helper.fail("No bank card found in player's inventory", fakePlayer.getOnPos());
        }
        helper.succeed();
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void basicTransaction(GameTestHelper helper) {
        // Place ATM block and check for block type and block entity type
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0));

        // Create a fake player and teleport it to structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0));

        // Add cash items in fake player's inventory (10x1+5x5=35$) and a bank card
        fakePlayer.getInventory().add(new ItemStack(ModItems.COIN.get(), 10));
        fakePlayer.getInventory().add(new ItemStack(ModItems.BILL.get(), 5));

        // Reset fake player's balance and open ATM menu for it
        fakePlayer.setData(ModAttachmentTypes.MONEY.get(), 0.f);
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        // Add bank card to the ATM inventory
        ATMBlockEntity atmBlockEntity = (ATMBlockEntity) helper.getLevel().getBlockEntity(atmPos);
        atmBlockEntity.inventory.setStackInSlot(0, new ItemStack(ModItems.BANK_CARD.get()));

        // Set amount to credit and debit
        final float amount = 35.f;

        // Credit then debit the same 'amount' of money
        atmMenu.credit(amount);
        GameTestUtils.assertPlayerDataAttachment(helper, fakePlayer, ModAttachmentTypes.MONEY.get(), amount);
        atmMenu.debit(amount);
        GameTestUtils.assertPlayerDataAttachment(helper, fakePlayer, ModAttachmentTypes.MONEY.get(), 0.f);

        helper.succeed();
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void basicTransactionWithoutBankCard(GameTestHelper helper) {
        // Place ATM block and check for block type and block entity type
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0));

        // Create a fake player and teleport it to structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0));

        // Add cash items in fake player's inventory (10x1+5x5=35$)
        fakePlayer.getInventory().add(new ItemStack(ModItems.COIN.get(), 10));
        fakePlayer.getInventory().add(new ItemStack(ModItems.BILL.get(), 5));

        // Reset fake player's balance and open ATM menu for it
        fakePlayer.setData(ModAttachmentTypes.MONEY.get(), 0.f);
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        // Set amount to credit and debit
        final float amount = 35.f;

        // Credit then debit the same 'amount' of money
        atmMenu.credit(amount);
        GameTestUtils.assertPlayerDataAttachment(helper, fakePlayer, ModAttachmentTypes.MONEY.get(), 0.f);
        atmMenu.debit(amount);
        GameTestUtils.assertPlayerDataAttachment(helper, fakePlayer, ModAttachmentTypes.MONEY.get(), 0.f);

        helper.succeed();
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void dropsOnRemoveEmpty(GameTestHelper helper) {
        // Place ATM block and check for block type and block entity type
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0));

        // Break ATM block
        helper.getLevel().destroyBlock(atmPos, true);

        // Check for drops (Expected ATM block item and one bank card)
        GameTestUtils.assertDrops(helper, new AABB(atmPos), List.of(
                new ItemStack(ModBlocks.ATM.asItem()))
        );
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void dropsOnRemove(GameTestHelper helper) {
        // Place ATM block and check for block type and block entity type
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0));

        // Add bank card to the ATM inventory
        ATMBlockEntity atmBlockEntity = (ATMBlockEntity) helper.getLevel().getBlockEntity(atmPos);
        ItemStack bankCardItemStack = new ItemStack(ModItems.BANK_CARD.get());
        atmBlockEntity.inventory.setStackInSlot(0, bankCardItemStack);

        // Break ATM block
        helper.getLevel().destroyBlock(atmPos, true);

        // Check for drops (Expected ATM block item and one bank card)
        GameTestUtils.assertDrops(helper, new AABB(atmPos), List.of(
                new ItemStack(ModItems.BANK_CARD.get()),
                new ItemStack(ModBlocks.ATM.asItem()))
        );
    }
}