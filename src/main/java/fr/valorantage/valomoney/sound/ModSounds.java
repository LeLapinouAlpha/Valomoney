package fr.valorantage.valomoney.sound;

import java.util.function.Supplier;

import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT,
            ValomoneyMod.MODID);

    public static Supplier<SoundEvent> ATM_CASH = registerSoundEvent("atm_cash");

    public static Supplier<SoundEvent> PIGGY_BANK_COIN = registerSoundEvent("piggy_bank_coin");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ValomoneyMod.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
