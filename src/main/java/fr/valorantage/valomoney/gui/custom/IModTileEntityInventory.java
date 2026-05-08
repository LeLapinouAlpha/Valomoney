package fr.valorantage.valomoney.gui.custom;

import org.joml.Vector2i;

public interface IModTileEntityInventory {
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
    // Starting from 36 = TileInventory slots, which map to our TileEntity slot
    // numbers 0 - n)
    public final static int VANILLA_FIRST_SLOT_INDEX = 0;

    public static final Vector2i HOTBAR_START = new Vector2i(15, 180);
    public static final int HOTBAR_SLOT_COUNT = 9;
    public static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX;

    public static final Vector2i PLAYER_INVENTORY_START = new Vector2i(15, 122);
    public static final int PLAYER_INVENTORY_ROWS = 3;
    public static final int PLAYER_INVENTORY_COLS = 9;
    public static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLS * PLAYER_INVENTORY_ROWS;
    public static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;

    public static final Vector2i SLOTS_SIZE = new Vector2i(18, 18);

    default Vector2i getSlotsSize() {
        return SLOTS_SIZE;
    }

    default int getHotbarSlotIndex(int col) {
        return HOTBAR_FIRST_SLOT_INDEX + col;
    }

    default Vector2i getHotbarSlotPos(int col) {
        return new Vector2i(HOTBAR_START).add(new Vector2i(this.getSlotsSize()).mul(col, 0));
    }

    default int getPlayerInventorySlotIndex(int row, int col) {
        return PLAYER_INVENTORY_FIRST_SLOT_INDEX + (row * PLAYER_INVENTORY_COLS) + col;
    }

    default Vector2i getPlayerInventorySlotPos(int row, int col) {
        return new Vector2i(PLAYER_INVENTORY_START).add(new Vector2i(this.getSlotsSize()).mul(col, row));
    }

    int getTEInventorySlotCount();

    Vector2i getTEInventorySlotPos(int index);

    void addHotbar();

    void addPlayerInventory();

    void addTEInventory();
}
