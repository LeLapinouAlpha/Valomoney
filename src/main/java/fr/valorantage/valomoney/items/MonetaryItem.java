package fr.valorantage.valomoney.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MonetaryItem extends Item {
    private final float unitValue;

    public MonetaryItem(final float unitValue) {
        super(new Item.Properties());
        this.unitValue = unitValue;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> componentList, TooltipFlag flag) {
        componentList.add(Component.literal(String.format("Unit value: %.2f$", unitValue)));
        componentList.add(Component.literal(String.format("Total value: %.2f$", unitValue * stack.getCount())));
        super.appendHoverText(stack, level, componentList, flag);
    }
}
