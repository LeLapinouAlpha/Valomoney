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
import java.util.function.Supplier;

public class CashItem extends Item {
    private final Supplier<Float> valueSupplier;
    private Style nameStyle;

    // Prefer the Supplier constructor to avoid reading config during registration.
    public CashItem(Supplier<Float> valueSupplier, int nameColor) {
        super(new Item.Properties());
        this.valueSupplier = valueSupplier;
        setNameColor(nameColor);
    }

    // Convenience constructors for constant values
    public CashItem(float value, int nameColor) {
        this(() -> value, nameColor);
        if (value <= 0.0f)
            throw new IllegalArgumentException("Monetary value must be greater than zero");
    }

    public CashItem(float value) {
        this(value, ChatFormatting.YELLOW.getColor());
    }

    public int getNameColor() {
        return this.nameStyle.getColor().getValue();
    }

    public void setNameColor(int color) {
        this.nameStyle = Style.EMPTY.withColor(TextColor.fromRgb(color)).withBold(true);
    }

    public float getValue() {
        return this.valueSupplier.get();
    }

    @Override
    public Component getName(ItemStack stack) {
        return ((MutableComponent) super.getName(stack)).withStyle(this.nameStyle);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        final int count = stack.getCount();
        final float value = count * this.getValue();
        tooltipComponents.add(Component.literal(String.format("Money: %.2f$", value)));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
