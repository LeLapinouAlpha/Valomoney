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

public class ShopBlockMenu extends AbstractContainerMenu implements IModTileEntityInventory {
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

    @Override
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

    @Override
    public void addHotbar() {
        for (int c = 0; c < HOTBAR_SLOT_COUNT; ++c) {
            final int slotIndex = this.getHotbarSlotIndex(c);
            final Vector2i slotPos = this.getHotbarSlotPos(c);
            this.addSlot(new Slot(this.playerInventory, slotIndex, slotPos.x, slotPos.y));
        }
    }

    @Override
    public void addPlayerInventory() {
        for (int r = 0; r < PLAYER_INVENTORY_ROWS; ++r) {
            for (int c = 0; c < PLAYER_INVENTORY_COLS; ++c) {
                final int slotIndex = this.getPlayerInventorySlotIndex(r, c);
                final Vector2i slotPos = this.getPlayerInventorySlotPos(r, c);
                this.addSlot(new Slot(this.playerInventory, slotIndex, slotPos.x, slotPos.y));
            }
        }
    }

    @Override
    public void addTEInventory() {
        for (int i = 0; i < this.getTEInventorySlotCount(); ++i) {
            final Vector2i slotPos = this.getTEInventorySlotPos(i);
            this.addSlot(new SlotItemHandler(this.blockEntity.itemHandler, i, slotPos.x, slotPos.y));
        }
    }

    @Override
    public int getTEInventorySlotCount() {
        return this.blockEntity.itemHandler.getSlots();
    }
}
