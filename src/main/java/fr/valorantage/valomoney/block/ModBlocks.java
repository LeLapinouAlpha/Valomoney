package fr.valorantage.valomoney.block;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.block.custom.ATMBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "valomoney" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ValomoneyMod.MODID);

    // Create ATM block
    public static final DeferredBlock<Block> ATM = BLOCKS.register("atm", () -> new ATMBlock(BlockBehaviour.Properties.of()
            .strength(4.f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
    ));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
