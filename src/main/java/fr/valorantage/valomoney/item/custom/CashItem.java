package fr.valorantage.valomoney.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CashItem extends Item {
    private final float value;

    public CashItem(float value) {
        super(new Item.Properties());

        if (value <= 0.0f)
            throw new IllegalArgumentException("Monetary value must be greater than zero");
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        final int count = stack.getCount();
        final float value = count * this.value;
        tooltipComponents.add(Component.literal(String.format("Money: %.2f$", value)));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
