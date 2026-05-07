package fr.valorantage.valomoney.block.entity.custom;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import fr.valorantage.valomoney.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShopBlockEntity extends BlockEntity {
    private final static Logger LOGGER = LogUtils.getLogger();

    public ShopBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SHOP_BLOCK_ENTITY.get(), blockPos, blockState);
    }
}
