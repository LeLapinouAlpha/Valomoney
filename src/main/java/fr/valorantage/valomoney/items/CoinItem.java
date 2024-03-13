package fr.valorantage.valomoney.items;

import net.minecraft.world.item.Item;

public abstract class CoinItem extends Item {

    private final float unitValue;

    public CoinItem(float unitValue) {
        super(new Item.Properties());
        this.unitValue = unitValue;
    }
}
