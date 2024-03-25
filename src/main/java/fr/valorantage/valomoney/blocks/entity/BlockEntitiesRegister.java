package fr.valorantage.valomoney.blocks.entity;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.blocks.custom.BlocksRegister;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntitiesRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ValomoneyMod.MODID);
    public static final RegistryObject<BlockEntityType<ShopBlockEntity>> SHOP_BLOCK_ENTITY = BLOCK_ENTITIES.register("shop",
            () -> BlockEntityType.Builder.of(ShopBlockEntity::new, BlocksRegister.SHOP_BLOCK.get()).build(null));
}
