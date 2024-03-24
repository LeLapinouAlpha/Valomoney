package fr.valorantage.valomoney.blocks.custom;

import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlocksRegister {
    public final static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ValomoneyMod.MODID);

    public final static RegistryObject<Block> SHOP_BLOCK = BLOCKS.register("shop", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
}
