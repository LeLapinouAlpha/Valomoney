package fr.valorantage.valomoney.attachment;

import com.mojang.serialization.Codec;
import fr.valorantage.valomoney.ValomoneyMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachmentTypes {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
            .create(NeoForgeRegistries.ATTACHMENT_TYPES, ValomoneyMod.MODID);

    public static final Supplier<AttachmentType<Float>> MONEY = ATTACHMENT_TYPES.register("money",
            () -> AttachmentType.builder(() -> 0.f).serialize(Codec.FLOAT).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
