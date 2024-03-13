package fr.valorantage.valomoney.items;

import net.minecraft.world.item.Item;

public abstract class BillItem extends Item {

    private final int unitValue;

    public BillItem(final int unitValue) {
        super(new Item.Properties());
        this.unitValue = unitValue;
    }
}
