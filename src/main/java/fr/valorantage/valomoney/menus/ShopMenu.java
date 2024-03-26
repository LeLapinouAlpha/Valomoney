package fr.valorantage.valomoney.menus;

import fr.valorantage.valomoney.blocks.entity.ShopBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ShopMenu extends AbstractContainerMenu {
    private static final int SLOT_SIZE = 18;
    private static final int INPUT_ROW_COUNT = 6;
    private static final int INPUT_COL_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COL_COUNT = 9;
    private static final int SLOT_X_OFFSET = 8;
    private static final int PLAYER_INVENTORY_Y_OFFSET = SLOT_SIZE * 7 + 14;
    private static final int PLAYER_HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_HOTBAR_Y_OFFSET = PLAYER_INVENTORY_Y_OFFSET + 3 * SLOT_SIZE + 4;
    private final ShopBlockEntity blockEntity;
    private final ContainerData data;
    private final Level level;

    public ShopMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), null);
    }

    public ShopMenu(int containerId, Inventory playerInventory, BlockEntity entity, ContainerData data) {
        super(MenusRegister.SHOP_MENU.get(), containerId);
        this.blockEntity = (ShopBlockEntity) entity;
        this.data = data;
        this.level = playerInventory.player.level();
        addPlayerInventorySlots(playerInventory);
        addPlayerHotbar(playerInventory);
        addInputSlots();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    private void addPlayerInventorySlots(Inventory playerInventory) {
        for (int i = 0; i < PLAYER_INVENTORY_ROW_COUNT; ++i)
            for (int j = 0; j < PLAYER_INVENTORY_COL_COUNT; ++j)
                this.addSlot(new Slot(
                        playerInventory,
                        j + i * PLAYER_INVENTORY_COL_COUNT + PLAYER_HOTBAR_SLOT_COUNT,
                        SLOT_X_OFFSET + j * SLOT_SIZE,
                        PLAYER_INVENTORY_Y_OFFSET + i * SLOT_SIZE
                ));
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < PLAYER_HOTBAR_SLOT_COUNT; ++i)
            this.addSlot(new Slot(playerInventory, i, SLOT_X_OFFSET + i * SLOT_SIZE, PLAYER_HOTBAR_Y_OFFSET));
    }

    private void addInputSlots() {
        for (int i = 0; i < INPUT_ROW_COUNT; i++)
            for (int j = 0; j < INPUT_COL_COUNT; j++)
                this.addSlot(new Slot(
                        this.blockEntity.container,
                        j + i * INPUT_COL_COUNT,
                        SLOT_X_OFFSET + j * SLOT_SIZE,
                        SLOT_SIZE + i * SLOT_SIZE)
                );
    }
}
