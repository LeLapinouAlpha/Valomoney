package fr.valorantage.valomoney.containers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ShopContainer implements Container {
    private static final int SLOT_COUNT = 54;

    private final ItemStack[] itemStacks;
    public ShopContainer() {
        this.itemStacks = new ItemStack[54];
        clearContent();
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!itemStacks[i].isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return itemStacks[index];
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        int newCount = Math.max(itemStacks[index].getCount() - count, 0);
        itemStacks[index].setCount(newCount);
        return itemStacks[index];
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        var tmp = itemStacks[index];
        itemStacks[index] = ItemStack.EMPTY;
        return tmp;
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        itemStacks[index] = itemStack;
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < SLOT_COUNT; i++)
            itemStacks[i] = ItemStack.EMPTY;
    }
}
