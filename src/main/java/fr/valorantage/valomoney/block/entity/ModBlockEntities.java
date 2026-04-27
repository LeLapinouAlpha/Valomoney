package fr.valorantage.valomoney.block.entity;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.block.ModBlocks;
import fr.valorantage.valomoney.block.entity.custom.ATMBlockEntity;
import fr.valorantage.valomoney.block.entity.custom.PiggyBankBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ValomoneyMod.MODID);

    public static final Supplier<BlockEntityType<ATMBlockEntity>> ATM_BE = BLOCK_ENTITIES.register("atm_be",
            () -> BlockEntityType.Builder.of(
                    ATMBlockEntity::new, ModBlocks.ATM.get()).build(null));

    public static final Supplier<BlockEntityType<PiggyBankBlockEntity>> PIGGY_BANK = BLOCK_ENTITIES
            .register("piggy_bank_be", () -> BlockEntityType.Builder.of(
                    PiggyBankBlockEntity::new, ModBlocks.PIGGY_BANK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
