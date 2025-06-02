package fr.valorantage.valomoney.gametest;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.block.entity.custom.ATMBlockEntity;
import fr.valorantage.valomoney.component.ModDataComponentTypes;
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
import net.minecraft.world.item.Items;
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

    private static ItemStack createBoundBankCard(Player player, int count) {
        var bankCard = new ItemStack(ModItems.BANK_CARD.get(), count);
        bankCard.set(ModDataComponentTypes.PLAYER_UUID.get(), player.getUUID().toString());
        return bankCard;
    }

    private static ItemStack createBoundBankCard(Player player) {
        return createBoundBankCard(player, 1);
    }

    private static BlockPos placeATMAndCheck(GameTestHelper helper, BlockPos relativePos, Player player, boolean withBankCard) {
        BlockPos pos = GameTestUtils.placeBlock(helper, relativePos, ModBlocks.ATM.get());

        // Check block & block entity
        helper.assertBlockPresent(ModBlocks.ATM.get(), relativePos);
        GameTestUtils.assertBlockEntity(helper, pos, ATMBlockEntity.class);

        if (withBankCard) {
            ATMBlockEntity atmBlockEntity = (ATMBlockEntity) helper.getLevel().getBlockEntity(pos);
            atmBlockEntity.inventory.insertItem(0, createBoundBankCard(player), false);
        }

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
    public static void useItemOn(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), 0.f);

        // Place ATM block without a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, false);

        // Make fake player use the ATM to open the GUI
        BlockState state = helper.getLevel().getBlockState(atmPos);
        var usedItemStack = new ItemStack(Blocks.STONE, 64);
        ItemInteractionResult interactionResult = state.useItemOn(usedItemStack, helper.getLevel(), fakePlayer, InteractionHand.MAIN_HAND, new BlockHitResult(
                Vec3.atCenterOf(atmPos), Direction.NORTH, atmPos, false));

        helper.succeedIf(() -> helper.assertValueEqual(interactionResult, ItemInteractionResult.SUCCESS, "atmItemInteractionResult"));
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void quickMoveBankCard(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), 0.f);

        // Place ATM block without a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, false);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        // Add bank card in fake player's inventory
        atmMenu.getVanillaInventorySlot(0).set(createBoundBankCard(fakePlayer));

        // Move the bank card from fake player's inventory to the ATM inventory using 'quickMoveStack' method
        final int bankCardSlot = GameTestUtils.findItemSlotInMenu(atmMenu, ModItems.BANK_CARD.get());
        helper.succeedIf(() -> GameTestUtils.assertQuickMoveStack(helper, fakePlayer, atmMenu,
                bankCardSlot, ItemStack.EMPTY,
                atmMenu.getTileInventorySlot(0).index, new ItemStack(ModItems.BANK_CARD.get())
        ));
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void quickMoveBankCardStack(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), 0.f);

        // Place ATM block without a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, false);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        // Add bank card in fake player's inventory
        atmMenu.getVanillaInventorySlot(0).set(createBoundBankCard(fakePlayer));

        // Move the bank card from fake player's inventory to the ATM inventory using 'quickMoveStack' method
        final int bankCardSlot = GameTestUtils.findItemSlotInMenu(atmMenu, ModItems.BANK_CARD.get());
        helper.succeedIf(() -> GameTestUtils.assertQuickMoveStack(helper, fakePlayer, atmMenu,
                bankCardSlot, new ItemStack(ModItems.BANK_CARD.get(), 63),
                atmMenu.getTileInventorySlot(0).index, new ItemStack(ModItems.BANK_CARD.get())
        ));
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void quickMoveOtherItem(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), 0.f);

        // Place ATM block without a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, false);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        // Add stone blocks in fake player's inventory
        atmMenu.getVanillaInventorySlot(0).set(new ItemStack(Items.STONE));

        // Move the bank card from fake player's inventory to the ATM's inventory using 'quickMoveStack' method
        final int stoneSlot = GameTestUtils.findItemSlotInMenu(atmMenu, Items.STONE);
        helper.succeedIf(() -> GameTestUtils.assertQuickMoveStack(helper, fakePlayer, atmMenu,
                stoneSlot, new ItemStack(Blocks.STONE),
                atmMenu.getTileInventorySlot(0).index, ItemStack.EMPTY
        ));
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void quickMoveBankCardInInventory(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), 0.f);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        // Move the bank card from ATM's inventory to the fake player's inventory using 'quickMoveStack' method
        helper.succeedIf(() -> GameTestUtils.assertQuickMoveStack(helper, fakePlayer, atmMenu,
                atmMenu.getTileInventorySlot(0).index, ItemStack.EMPTY,
                atmMenu.getVanillaInventorySlot(0).index, new ItemStack(ModItems.BANK_CARD.get())
        ));
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void quickMoveBankCardInInventoryExistingStack(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), 0.f);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        // Add bank card in fake player's inventory
        atmMenu.getVanillaInventorySlot(0).set(new ItemStack(ModItems.BANK_CARD.get(), 63));

        helper.succeedIf(() -> GameTestUtils.assertQuickMoveStack(helper, fakePlayer, atmMenu,
                atmMenu.getTileInventorySlot(0).index, ItemStack.EMPTY,
                atmMenu.getVanillaInventorySlot(0).index, new ItemStack(ModItems.BANK_CARD.get(), 64)
        ));
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void basicTransaction(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Add cash items in fake player's inventory (10x1+5x5=35$) and a bank card
        fakePlayer.getInventory().add(new ItemStack(ModItems.COIN.get(), 10));
        fakePlayer.getInventory().add(new ItemStack(ModItems.BILL.get(), 5));

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        // Credit then debit the same 'amount' of money
        helper.succeedIf(() -> {
            // Set amount to credit and debit
            final float amount = 35.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.credit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance + amount);
            atmMenu.debit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void basicTransactionWithoutBankCard(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block without a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, false);

        // Add cash items in fake player's inventory (10x1+5x5=35$)
        fakePlayer.getInventory().add(new ItemStack(ModItems.COIN.get(), 10));
        fakePlayer.getInventory().add(new ItemStack(ModItems.BILL.get(), 5));

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        // Credit then debit the same 'amount' of money
        helper.succeedIf(() -> {
            // Set amount to credit and debit
            final float amount = 35.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.credit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.debit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void dropsOnRemoveEmpty(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), 0.f);

        // Place ATM block without a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, false);

        // Break ATM block
        helper.getLevel().destroyBlock(atmPos, true);

        // Check for drops (Expected ATM block item and one bank card)
        helper.runAfterDelay(2, () -> {
            helper.succeedIf(() -> GameTestUtils.assertDrops(helper, new AABB(atmPos), List.of(
                    new ItemStack(ModBlocks.ATM.asItem()))
            ));
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void dropsOnRemove(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), 0.f);

        // Place ATM block without a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, false);

        // Break ATM block
        helper.getLevel().destroyBlock(atmPos, true);

        // Check for drops (Expected ATM block item and one bank card)
        helper.runAfterDelay(2, () -> {
            helper.succeedIf(() -> GameTestUtils.assertDrops(helper, new AABB(atmPos), List.of(
                    new ItemStack(ModItems.BANK_CARD.get()),
                    new ItemStack(ModBlocks.ATM.asItem()))
            ));
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void debitInsufficientFunds(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to debit
            final float amount = 100.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.debit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            GameTestUtils.assertInventoryEquals(helper, fakePlayer.getInventory(), List.of());
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void debitInventoryFull(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Fill player's inventory with stone
        var playerInventory = fakePlayer.getInventory();
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            playerInventory.setItem(i, new ItemStack(Blocks.STONE.asItem(), 64));
        }

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to debit
            final float amount = 100.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.debit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            GameTestUtils.assertInventoryAllMatch(helper, fakePlayer.getInventory(), new ItemStack(Blocks.STONE.asItem(), 64));
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void debitInvalidAmount(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to debit
            final float amount = -100.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.debit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            GameTestUtils.assertInventoryEquals(helper, fakePlayer.getInventory(), List.of());
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void debitOnlyBills(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to debit
            final float amount = 10.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.debit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance - amount);
            GameTestUtils.assertInventoryEquals(helper, fakePlayer.getInventory(), List.of(
                    new ItemStack(ModItems.BILL.get(), 2)
            ));
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void debitOnlyBillsMultipleStacks(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to debit
            final float amount = 640.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.debit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance - amount);
            GameTestUtils.assertInventoryEquals(helper, fakePlayer.getInventory(), List.of(
                    new ItemStack(ModItems.BILL.get(), 64),
                    new ItemStack(ModItems.BILL.get(), 64)
            ));
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void debitOnlyCoins(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to debit
            final float amount = 2.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.debit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance - amount);
            GameTestUtils.assertInventoryEquals(helper, fakePlayer.getInventory(), List.of(
                    new ItemStack(ModItems.COIN.get(), 2)
            ));
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void debitBillsAndCoins(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to debit
            final float amount = 12.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.debit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance - amount);
            GameTestUtils.assertInventoryEquals(helper, fakePlayer.getInventory(), List.of(
                    new ItemStack(ModItems.BILL.get(), 2),
                    new ItemStack(ModItems.COIN.get(), 2)
            ));
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void creditNoCash(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to credit
            final float amount = 12.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.credit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            GameTestUtils.assertInventoryEquals(helper, fakePlayer.getInventory(), List.of());
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void creditNoCashButOtherItems(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        // Fill player's inventory with stone
        var playerInventory = fakePlayer.getInventory();
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            playerInventory.setItem(i, new ItemStack(Blocks.STONE.asItem(), 64));
        }

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to credit
            final float amount = 12.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.credit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            GameTestUtils.assertInventoryAllMatch(helper, fakePlayer.getInventory(), new ItemStack(Blocks.STONE.asItem(), 64));
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void creditNotEnoughCash(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        //
        fakePlayer.getInventory().add(new ItemStack(ModItems.BILL.get(), 1));
        fakePlayer.getInventory().add(new ItemStack(ModItems.COIN.get(), 2));

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to credit
            final float amount = 12.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.credit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance + 7.f);
            GameTestUtils.assertInventoryEquals(helper, fakePlayer.getInventory(), List.of());
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void creditTooMuchCash(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        //
        fakePlayer.getInventory().add(new ItemStack(ModItems.BILL.get(), 10));

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to credit
            final float amount = 45.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.credit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance + amount);
            GameTestUtils.assertInventoryEquals(helper, fakePlayer.getInventory(), List.of(new ItemStack(ModItems.BILL.get(), 1)));
        });
    }

    @GameTest(template = BASICS_TEMPLATE)
    public static void creditCashEqualToAmount(GameTestHelper helper) {
        // Create a fake player and move it inside the gametest structure, and assign it an initial balance
        final float initialBalance = 0.f;
        Player fakePlayer = GameTestUtils.makeMockPlayer(helper, GameType.SURVIVAL, new BlockPos(1, 2, 0), initialBalance);

        // Place ATM block with a bank card
        BlockPos atmPos = placeATMAndCheck(helper, new BlockPos(0, 2, 0), fakePlayer, true);

        //
        fakePlayer.getInventory().add(new ItemStack(ModItems.BILL.get(), 10));

        // Open ATM menu for fake player
        ATMMenu atmMenu = openATMMenu(helper, atmPos, fakePlayer);

        helper.succeedIf(() -> {
            // Set amount to credit
            final float amount = 50.f;

            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance);
            atmMenu.credit(amount);
            GameTestUtils.assertPlayersMoney(helper, fakePlayer, initialBalance + amount);
            GameTestUtils.assertInventoryEquals(helper, fakePlayer.getInventory(), List.of());
        });
    }
}