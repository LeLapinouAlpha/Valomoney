package fr.valorantage.valomoney.gametest;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.attachment.ModAttachmentTypes;
import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.block.entity.custom.ATMBlockEntity;
import fr.valorantage.valomoney.exception.GameTestException;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import org.slf4j.Logger;

@GameTestHolder(ValomoneyMod.MODID)
public class ATMGameTests {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private static BlockPos placeATMAndCheck(GameTestHelper helper, BlockPos relativePos) {
        BlockPos pos = GameTestUtils.placeBlock(helper, relativePos, ModBlocks.ATM.get());

        // Check block & block entity
        try {
            helper.assertBlockPresent(ModBlocks.ATM.get(), relativePos);
            GameTestUtils.assertBlockEntity(helper, pos, ATMBlockEntity.class);
        } catch (GameTestException gameTestException) {
            helper.fail(gameTestException.getMessage(), gameTestException.getPos());
        }
        return pos;
    }

    @GameTest
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

    @GameTest
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

    @GameTest
    public static void basicTransaction(GameTestHelper helper) {
        // Place ATM block and check for block type and block entity type
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0));

        // Create a fake player, teleport it to structure and make it use ATM to open the GUI
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0));

        // Add cash items in fake player's inventory (5x5+10x1=35$) and a bank card
        ItemStack bills = new ItemStack(ModItems.BILL.get(), 5);
        ItemStack coins = new ItemStack(ModItems.COIN.get(), 10);
        ItemStack bankCard = new ItemStack(ModItems.BANK_CARD.get(), 1);
        fakePlayer.getInventory().add(bills);
        fakePlayer.getInventory().add(coins);
        fakePlayer.getInventory().add(bankCard);

        // Reset fake player's balance and open ATM menu for it
        fakePlayer.setData(ModAttachmentTypes.MONEY.get(), 0.f);
        ATMBlockEntity atmBlockEntity = (ATMBlockEntity) helper.getLevel().getBlockEntity(atmPos);
        MenuProvider provider = new SimpleMenuProvider(atmBlockEntity, atmBlockEntity.getDisplayName());
        AbstractContainerMenu menu = provider.createMenu(0, fakePlayer.getInventory(), fakePlayer);
        if (menu instanceof ATMMenu atmMenu) {
            // Add a bank card in ATM's inventory
            atmBlockEntity.inventory.setStackInSlot(0, fakePlayer.getInventory().getItem(2));

            // Set amount to credit and debit
            final float amount = 35.f;

            // Credit then debit the same 'amount' of money
            atmMenu.credit(amount);
            float fakePlayerBalance = fakePlayer.getData(ModAttachmentTypes.MONEY.get());
            if (fakePlayerBalance != amount) {
                helper.fail(String.format("Expected: %.2f$, Actual: %.2f$", amount, fakePlayerBalance), atmPos);
            }
            atmMenu.debit(amount);
            fakePlayerBalance = fakePlayer.getData(ModAttachmentTypes.MONEY.get());
            if (fakePlayerBalance != 0.f) {
                helper.fail(String.format("Expected: %.2f$, Actual: %.2f$", amount, fakePlayerBalance), atmPos);
            }
        } else {
            helper.fail("Menu is not ATMMenu", atmPos);
        }

        helper.succeed();
    }
}