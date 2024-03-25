package fr.valorantage.valomoney.menus;

import fr.valorantage.valomoney.blocks.entity.ShopBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class ShopMenu extends AbstractContainerMenu {
    private final ShopBlockEntity blockEntity;
    private final ContainerData data;
    private final Level level;

    public ShopMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public ShopMenu(int containerId, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(MenusRegister.SHOP_MENU.get(), containerId);
        this.blockEntity = (ShopBlockEntity) entity;
        this.data = data;
        this.level = inventory.player.level();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
