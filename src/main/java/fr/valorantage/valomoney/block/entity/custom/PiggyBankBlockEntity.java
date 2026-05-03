package fr.valorantage.valomoney.block.entity.custom;

import fr.valorantage.valomoney.block.entity.ModBlockEntities;
import fr.valorantage.valomoney.item.custom.CashItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PiggyBankBlockEntity extends BlockEntity {
    public final ItemStackHandler inventory = new ItemStackHandler(9) {

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof CashItem;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public PiggyBankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIGGY_BANK.get(), pos, state);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    public void drops() {
        SimpleContainer container = new SimpleContainer(this.inventory.getSlots());
        for (int i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, this.inventory.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, container);
    }

    public int insertCash(ItemStack insertStack) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            insertStack = inventory.insertItem(i, insertStack, false);
            if (insertStack.isEmpty()) {
                break;
            }
        }
        return insertStack.getCount();
    }
}
