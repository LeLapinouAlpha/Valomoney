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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ATMMenu extends AbstractContainerMenu {
    private final static Logger LOGGER = LogUtils.getLogger();

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 36 = TileInventory slots, which map to our TileEntity slot numbers 0 - 0)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 1;

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

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (isVanillaInventorySlot(pIndex)) {
            if (!moveItemStackToTileInventory(sourceStack)) {
                return ItemStack.EMPTY;
            }
        } else if (isTileInventorySlot(pIndex)) {
            if (!moveItemStackToVanillaInventory(sourceStack)) {
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
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(this.level, blockEntity.getBlockPos()), player, ModBlocks.ATM.get());
    }

    public int getVanillaInventoryFirstSlotIndex() {
        return VANILLA_FIRST_SLOT_INDEX;
    }

    public int getVanillaInventorySlotCount() {
        return VANILLA_SLOT_COUNT;
    }

    public int getVanillaInventoryLastSlotIndex() {
        return getVanillaInventoryFirstSlotIndex() + getVanillaInventorySlotCount();
    }

    public int getTileInventoryFirstSlotIndex() {
        return TE_INVENTORY_FIRST_SLOT_INDEX;
    }

    public int getTileInventorySlotCount() {
        return TE_INVENTORY_SLOT_COUNT;
    }

    public int getTileInventoryLastSlotIndex() {
        return getTileInventoryFirstSlotIndex() + getTileInventorySlotCount();
    }

    public @NotNull Slot getSlot(int index) {
        return slots.get(index);
    }

    private void addTileInventory() {
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 29, 24));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, getVanillaInventoryFirstSlotIndex() + l + i * 9 + 9, 15 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 15 + i * 18, 144));
        }
    }

    private boolean isVanillaInventorySlot(int index) {
        return index >= getVanillaInventoryFirstSlotIndex() && index < getVanillaInventoryLastSlotIndex();
    }

    public Slot getVanillaInventorySlot(int index) throws IndexOutOfBoundsException {
        final int correctedIndex = getVanillaInventoryFirstSlotIndex() + index;
        if (!isVanillaInventorySlot(correctedIndex)) {
            throw new IndexOutOfBoundsException(index);
        }
        return this.getSlot(correctedIndex);
    }

    private boolean isTileInventorySlot(int index) {
        return index >= getTileInventoryFirstSlotIndex() && index < getTileInventoryLastSlotIndex();
    }

    public Slot getTileInventorySlot(int index) throws IndexOutOfBoundsException {
        final int correctedIndex = this.getTileInventoryFirstSlotIndex() + index;
        if (!isTileInventorySlot(correctedIndex)) {
            throw new IndexOutOfBoundsException(index);
        }
        return this.getSlot(correctedIndex);
    }

    private boolean moveItemStackToVanillaInventory(ItemStack stack) {
        return moveItemStackTo(stack, getVanillaInventoryFirstSlotIndex(), getVanillaInventoryLastSlotIndex(), false);
    }

    private boolean moveItemStackToTileInventory(ItemStack stack) {
        return moveItemStackTo(stack, getTileInventoryFirstSlotIndex(), getTileInventoryLastSlotIndex(), false);
    }

    public boolean hasBankCard() {
        return this.getTileInventorySlot(0).getItem().is(ModItems.BANK_CARD.get());
    }

    private static <T extends MonetaryItem> List<ItemStack> withdrawCash(List<T> authorizedCashItems, final float maxValue) {
        authorizedCashItems.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));

        List<ItemStack> cashItems = new ArrayList<>();
        float currentValue = maxValue;
        for (var cashItem : authorizedCashItems) {
            final int count = (int) (currentValue / cashItem.getValue());
            final int fullStackCount = count / 64;
            final int remaining = count % 64;

            for (int i = 0; i < fullStackCount; ++i) {
                cashItems.add(new ItemStack(cashItem, 64));
            }

            if (remaining > 0) {
                cashItems.add(new ItemStack(cashItem, remaining));
            }

            currentValue -= fullStackCount * 64 * cashItem.getValue() + remaining * cashItem.getValue();
        }
        return cashItems;
    }

    public void debit(float amount) {
        var player = this.playerInventory.player;
        float currentPlayerBalance = player.getData(ModAttachmentTypes.MONEY);

        if (hasBankCard()) {
            // Get cash items list to give to player
            var cashItems = withdrawCash(new ArrayList<>(List.of(
                    (MonetaryItem) ModItems.BILL.get(),
                    (MonetaryItem) ModItems.COIN.get()
            )), Math.min(amount, currentPlayerBalance));

            // Distribute cash items in player's inventory and updating dynamically player's balance
            for (var cashItemStack : cashItems) {
                if (this.playerInventory.add(cashItemStack.copy())) {
                    // Withdraw cashItemStack's value from player's balance
                    var item = cashItemStack.getItem();
                    var monetaryItem = (MonetaryItem) item;
                    currentPlayerBalance -= monetaryItem.getValue() * cashItemStack.getCount();
                    player.setData(ModAttachmentTypes.MONEY.get(), currentPlayerBalance);
                }
            }
        }
    }

    public void credit(float amount) {
        var player = this.playerInventory.player;
        var actualPlayerMoney = player.getData(ModAttachmentTypes.MONEY);
        LOGGER.debug("{}'s actual balance is {}$", player.getDisplayName().getString(), actualPlayerMoney);

        if (hasBankCard()) {
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
