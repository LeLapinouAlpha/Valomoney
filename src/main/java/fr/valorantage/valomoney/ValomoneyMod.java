package fr.valorantage.valomoney;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.backend.economy.EconomyFileManager;
import fr.valorantage.valomoney.backend.economy.EconomyManager;
import fr.valorantage.valomoney.blocks.custom.BlocksRegister;
import fr.valorantage.valomoney.blocks.entity.BlockEntitiesRegister;
import fr.valorantage.valomoney.items.ItemsRegister;
import fr.valorantage.valomoney.menus.MenusRegister;
import fr.valorantage.valomoney.screens.ShopScreen;
import fr.valorantage.valomoney.tabs.CreativeModeTabsRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.IOException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ValomoneyMod.MODID)
public class ValomoneyMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "valomoney";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // Create an EconomyManager instance
    public final static EconomyManager ECONOMY_MANAGER = new EconomyManager();

    // Create an EconomyFileManager instance
    public final static EconomyFileManager ECONOMY_FILE_MANAGER = new EconomyFileManager("./valomoney");

    public ValomoneyMod() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so items get registered
        ItemsRegister.ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CreativeModeTabsRegister.CREATIVE_MODE_TABS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BlocksRegister.BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so menus get registered
        MenusRegister.MENUS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so block entities get registered
        BlockEntitiesRegister.BLOCK_ENTITIES.register(modEventBus);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        try {
            ECONOMY_MANAGER.restoreState(ECONOMY_FILE_MANAGER);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        try {
            LOGGER.info("MESSAGE FROM STOPPING SERVER");
            ECONOMY_MANAGER.saveState(ECONOMY_FILE_MANAGER);
        } catch (IOException ioEx) {
            LOGGER.error(ioEx.getMessage());
        }
    }

    @SubscribeEvent
    public void onPlayerLogging(PlayerEvent.PlayerLoggedInEvent event) {
        try {
            ECONOMY_MANAGER.createWallet(event.getEntity().getUUID());
        } catch (Exception ignored) {

        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            MenuScreens.register(MenusRegister.SHOP_MENU.get(), ShopScreen::new);
        }
    }
}