package fr.valorantage.valomoney.events;

import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.commands.MoneyCommand;
import fr.valorantage.valomoney.commands.PayCommand;
import fr.valorantage.valomoney.commands.ShopCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = ValomoneyMod.MODID)
public final class CommandEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new MoneyCommand(event.getDispatcher());
        new PayCommand(event.getDispatcher());
        new ShopCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}
