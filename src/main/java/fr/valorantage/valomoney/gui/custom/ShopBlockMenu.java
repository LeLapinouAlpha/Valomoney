package fr.valorantage.valomoney.gui.custom;

import org.joml.Vector2i;

import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.block.entity.custom.ShopBlockEntity;
import fr.valorantage.valomoney.gui.ModMenuTypes;
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

public class ShopBlockMenu extends AbstractContainerMenu {
    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the
    // player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the
    // slotIndex, which means
    // 0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 -
    // 8)
    // 9 - 35 = player inventory slots (which map to the InventoryPlayer slot
    // numbers 9 - 35)
    // 36 - 39 = TileInventory slots, which map to our TileEntity slot numbers 0 -
    // 4)
    private final static int VANILLA_FIRST_SLOT_INDEX = 0;

    private static final Vector2i HOTBAR_START = new Vector2i(15, 180);
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX;

    private static final Vector2i PLAYER_INVENTORY_START = new Vector2i(15, 122);
    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_INVENTORY_COLS = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLS * PLAYER_INVENTORY_ROWS;
    private static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 4;

    private static final Vector2i SLOTS_SIZE = new Vector2i(18, 18);

    private final Inventory playerInventory;
    private final ShopBlockEntity blockEntity;
    private final Level level;

    public ShopBlockMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public ShopBlockMenu(int containerId, Inventory playerInventory, BlockEntity blockEntity) {
        super(ModMenuTypes.SHOP_MENU.get(), containerId);
        this.playerInventory = playerInventory;
        this.blockEntity = (ShopBlockEntity) blockEntity;
        this.level = playerInventory.player.level();

        this.addPlayerInventory();
        this.addHotbar();
        this.addTEInventory();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(this.level, this.blockEntity.getBlockPos()), player,
                ModBlocks.SHOP.get());
    }

    public int getHotbarSlotIndex(int col) {
        return HOTBAR_FIRST_SLOT_INDEX + col;
    }

    public Vector2i getHotbarSlotPos(int col) {
        return new Vector2i(HOTBAR_START).add(new Vector2i(SLOTS_SIZE).mul(col, 0));
    }

    public int getPlayerInventorySlotIndex(int row, int col) {
        return row * PLAYER_INVENTORY_COLS + PLAYER_INVENTORY_FIRST_SLOT_INDEX + col;
    }

    public Vector2i getPlayerInventorySlotPos(int row, int col) {
        return new Vector2i(PLAYER_INVENTORY_START).add(new Vector2i(SLOTS_SIZE).mul(col, row));
    }

    public Vector2i getTEInventorySlotPos(int index) {
        switch (index) {
            case 0:
                return new Vector2i(51, 13);
            case 1:
                return new Vector2i(124, 13);
            case 2:
                return new Vector2i(51, 73);
            case 3:
                return new Vector2i(124, 73);
            default:
                throw new IllegalArgumentException("Invalid TE Inventory slot index: " + index);
        }
    }

    private void addHotbar() {
        for (int c = 0; c < HOTBAR_SLOT_COUNT; ++c) {
            final int slotIndex = this.getHotbarSlotIndex(c);
            final Vector2i slotPos = this.getHotbarSlotPos(c);
            this.addSlot(new Slot(this.playerInventory, slotIndex, slotPos.x, slotPos.y));
        }
    }

    private void addPlayerInventory() {
        for (int r = 0; r < PLAYER_INVENTORY_ROWS; ++r) {
            for (int c = 0; c < PLAYER_INVENTORY_COLS; ++c) {
                final int slotIndex = this.getPlayerInventorySlotIndex(r, c);
                final Vector2i slotPos = this.getPlayerInventorySlotPos(r, c);
                this.addSlot(new Slot(this.playerInventory, slotIndex, slotPos.x, slotPos.y));
            }
        }
    }

    private void addTEInventory() {
        for (int i = 0; i < TE_INVENTORY_SLOT_COUNT; ++i) {
            final Vector2i slotPos = this.getTEInventorySlotPos(i);
            this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, i, slotPos.x, slotPos.y));
        }
    }
}
