package fr.valorantage.valomoney.gui.custom;

import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.block.entity.custom.ATMBlockEntity;
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

public class ATMMenu extends AbstractContainerMenu {
    private final ATMBlockEntity blockEntity;
    private final Level level;

    public ATMMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public ATMMenu(int containerId, Inventory inventory, BlockEntity blockEntity) {
        super(ModMenuTypes.ATM_MENU.get(), containerId);
        this.blockEntity = (ATMBlockEntity) blockEntity;
        this.level = inventory.player.level();

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 29, 24));
        this.addPlayerInventory(inventory);
        this.addPlayerHotbar(inventory);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        // TODO
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(this.level, blockEntity.getBlockPos()), player, ModBlocks.ATM.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 15 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 15 + i * 18, 144));
        }
    }
}
