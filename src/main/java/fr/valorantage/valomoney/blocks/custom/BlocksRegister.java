package fr.valorantage.valomoney.blocks.custom;

import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlocksRegister {
    public final static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ValomoneyMod.MODID);
    public final static RegistryObject<BaseEntityBlock> SHOP_BLOCK = BLOCKS.register("shop", () -> new ShopBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
}
