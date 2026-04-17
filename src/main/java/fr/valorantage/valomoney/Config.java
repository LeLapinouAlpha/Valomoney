package fr.valorantage.valomoney;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = ValomoneyMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        public static final ModConfigSpec.ConfigValue<Float> COIN1_VALUE;
        public static final ModConfigSpec.ConfigValue<Float> COIN2_VALUE;
        public static final ModConfigSpec.ConfigValue<Float> COIN3_VALUE;
        public static final ModConfigSpec.ConfigValue<Float> BILL1_VALUE;
        public static final ModConfigSpec.ConfigValue<Float> BILL2_VALUE;
        public static final ModConfigSpec.ConfigValue<Float> BILL3_VALUE;
        public static final ModConfigSpec.ConfigValue<Float> BILL4_VALUE;
        public static final ModConfigSpec.ConfigValue<Float> BILL5_VALUE;
        static {
                BUILDER.comment("Set the monetary value of cash items")
                                .push("cashItems");

                COIN1_VALUE = BUILDER.define("coin1", 0.1f);
                COIN2_VALUE = BUILDER.define("coin2", 0.5f);
                COIN3_VALUE = BUILDER.define("coin3", 1.0f);
                BILL1_VALUE = BUILDER.define("bill1", 5.0f);
                BILL2_VALUE = BUILDER.define("bill2", 10.0f);
                BILL3_VALUE = BUILDER.define("bill3", 20.0f);
                BILL4_VALUE = BUILDER.define("bill4", 50.0f);
                BILL5_VALUE = BUILDER.define("bill5", 100.0f);

                BUILDER.pop();
        }

        public static final ModConfigSpec SPEC = BUILDER.build();

        private static boolean validateItemName(final Object obj) {
                return obj instanceof String itemName
                                && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
        }

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {

        }
}
