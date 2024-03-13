package fr.valorantage.valomoney.items;

import net.minecraft.world.item.Item;

public abstract class MonetaryItem extends Item {
    private final float unitValue;

    public MonetaryItem(final float unitValue) {
        super(new Item.Properties());
        this.unitValue = unitValue;
    }
}
