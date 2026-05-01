package fr.valorantage.valomoney.block.custom;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;

import fr.valorantage.valomoney.block.entity.custom.PiggyBankBlockEntity;
import fr.valorantage.valomoney.item.custom.CashItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// TODO: Display money value on screen when looking at the piggy bank
public class PiggyBankBlock extends FallingBlock implements EntityBlock {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);
    public static final MapCodec<PiggyBankBlock> CODEC = simpleCodec(PiggyBankBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public PiggyBankBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PiggyBankBlockEntity(blockPos, blockState);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof PiggyBankBlockEntity piggyBank) {
            if (stack.getItem() instanceof CashItem) {
                // TODO: Implement possibility to insert full stack when sneaking, and insert
                // only one item when not sneaking, to prevent accidentally inserting an entire
                // stack
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof PiggyBankBlockEntity piggyBank) {
                piggyBank.drops();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState,
            FallingBlockEntity fallingBlock) {
        super.onLand(level, pos, state, replaceableState, fallingBlock);

        // Break block
        level.destroyBlock(pos, false);
    }

}
