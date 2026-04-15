package fr.valorantage.valomoney.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class CashItem extends Item {
    private final float value;
    private Style nameStyle;

    public CashItem(float value, int nameColor) {
        super(new Item.Properties());

        if (value <= 0.0f)
            throw new IllegalArgumentException("Monetary value must be greater than zero");

        this.value = value;
        setNameColor(nameColor);
    }

    public CashItem(float value) {
        this(value, ChatFormatting.YELLOW.getColor());
    }

    public void setNameColor(int color) {
        this.nameStyle = Style.EMPTY.withColor(TextColor.fromRgb(color)).withBold(true);
    }

    public float getValue() {
        return value;
    }

    @Override
    public Component getName(ItemStack stack) {
        return ((MutableComponent) super.getName(stack)).withStyle(this.nameStyle);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        final int count = stack.getCount();
        final float value = count * this.value;
        tooltipComponents.add(Component.literal(String.format("Money: %.2f$", value)));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
