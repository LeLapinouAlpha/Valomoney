package fr.valorantage.valomoney.gui.custom;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.attachment.ModAttachmentTypes;
import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.block.entity.custom.ATMBlockEntity;
import fr.valorantage.valomoney.gui.ModMenuTypes;
import fr.valorantage.valomoney.item.ModItems;
import fr.valorantage.valomoney.item.custom.MonetaryItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.slf4j.Logger;

public class ATMMenu extends AbstractContainerMenu {
    private final static Logger LOGGER = LogUtils.getLogger();

    private final Inventory playerInventory;
    private final ATMBlockEntity blockEntity;
    private final Level level;

    public ATMMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public ATMMenu(int containerId, Inventory inventory, BlockEntity blockEntity) {
        super(ModMenuTypes.ATM_MENU.get(), containerId);
        this.playerInventory = inventory;
        this.blockEntity = (ATMBlockEntity) blockEntity;
        this.level = inventory.player.level();

        this.addPlayerInventory(inventory);
        this.addPlayerHotbar(inventory);
        this.addTileInventory();
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    public static final int HOTBAR_SLOT_COUNT = 9;
    public static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    public static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    public static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    public static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    public static final int VANILLA_FIRST_SLOT_INDEX = 0;
    public static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    public static final int TE_INVENTORY_SLOT_COUNT = 1;

    @Override
    public ItemStack quickMoveStack(Player player, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(this.level, blockEntity.getBlockPos()), player, ModBlocks.ATM.get());
    }

    public Slot getSlot(int index) {
        return slots.get(index);
    }

    private void addTileInventory() {
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 29, 24));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, VANILLA_FIRST_SLOT_INDEX + l + i * 9 + 9, 15 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 15 + i * 18, 144));
        }
    }

    // FIXME: must check if there is enough space in inventory
    public void debit(float amount) {
        var player = this.playerInventory.player;
        var currentPlayerMoney = player.getData(ModAttachmentTypes.MONEY);
        LOGGER.debug("{}'s actual balance is {}$", player.getDisplayName().getString(), currentPlayerMoney);

        if (this.blockEntity.inventory.getStackInSlot(0).getItem() == ModItems.BANK_CARD.get()) {
            var billItemStack = new ItemStack(ModItems.BILL.get(), 1);
            var billItem = (MonetaryItem) billItemStack.getItem();
            int billCount = (int) (amount / billItem.getValue());
            billItemStack.setCount(billCount);
            float billMoney = billCount * billItem.getValue();
            amount -= billMoney;

            var coinItemStack = new ItemStack(ModItems.COIN.get(), 1);
            var coinItem = (MonetaryItem) coinItemStack.getItem();
            int coinCount = (int) (amount / coinItem.getValue());
            coinItemStack.setCount(coinCount);
            float coinMoney = coinCount * coinItem.getValue();
            amount -= coinMoney;

            float actualDebit = billMoney + coinMoney;
            if (currentPlayerMoney < actualDebit) {
                LOGGER.debug("{}'s has insufficient funds", player.getDisplayName().getString());
                return;
            }

            if (!this.playerInventory.add(billItemStack)) {
                LOGGER.error("Couldn't add bills item to player's inventory: {}", billItemStack);
                return;
            }

            if (!this.playerInventory.add(coinItemStack)) {
                LOGGER.error("Couldn't add coins item to player's inventory: {}", coinItemStack);
                return;
            }

            LOGGER.debug("Debit will give: {} bills (5$), {} coins (1$)", billCount, coinCount);
            LOGGER.debug(String.format("Money that will not be given: %.2f$", amount));

            player.setData(ModAttachmentTypes.MONEY, currentPlayerMoney - actualDebit);
            LOGGER.debug("{}'s new balance is {}$", player.getDisplayName().getString(), player.getData(ModAttachmentTypes.MONEY));

        } else {
            LOGGER.debug("Cannot debit without a bank card");
        }
    }

    public void credit(float amount) {
        var player = this.playerInventory.player;
        var actualPlayerMoney = player.getData(ModAttachmentTypes.MONEY);
        LOGGER.debug("{}'s actual balance is {}$", player.getDisplayName().getString(), actualPlayerMoney);

        if (this.blockEntity.inventory.getStackInSlot(0).getItem() == ModItems.BANK_CARD.get()) {
            float playerInventoryMoney = 0;
            for (int i = 0; i < this.playerInventory.getContainerSize(); i++) {
                var item = this.playerInventory.getItem(i);
                if (item.getItem() instanceof MonetaryItem monetaryItem) {
                    float itemStackAmount = monetaryItem.getValue() * item.getCount();

                    if (playerInventoryMoney + itemStackAmount > amount) {
                        float remainingAmount = amount - playerInventoryMoney;
                        int maxItemCount = (int) (remainingAmount / monetaryItem.getValue());
                        if (maxItemCount > 0) {
                            playerInventoryMoney += monetaryItem.getValue() * maxItemCount;
                            item.setCount(item.getCount() - maxItemCount);
                        }
                        break;
                    } else {
                        playerInventoryMoney += itemStackAmount;
                        this.playerInventory.removeItem(i, item.getCount());
                    }
                }
            }

            LOGGER.debug("Credit {}$ to {}'s account", playerInventoryMoney, player.getName().getString());

            player.setData(ModAttachmentTypes.MONEY, actualPlayerMoney + playerInventoryMoney);
            LOGGER.debug("{}'s new balance is {}$", player.getDisplayName().getString(), player.getData(ModAttachmentTypes.MONEY));
        } else {
            LOGGER.debug("Cannot credit without a bank card");
        }
    }
}
