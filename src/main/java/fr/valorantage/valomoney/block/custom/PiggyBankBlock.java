package fr.valorantage.valomoney.block.custom;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PiggyBankBlock extends BaseEntityBlock {
    public static final MapCodec<PiggyBankBlock> CODEC = simpleCodec(PiggyBankBlock::new);

    public PiggyBankBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        // FIXME: Create a PiggyBankBlockEntity and return a new instance here
        return null;
    }
}
