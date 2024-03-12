package fr.valorantage.valomoney.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.Objects;


public final class MoneyCommand {
    public MoneyCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("money").executes((command -> displayPlayerMoney(command.getSource()))));
    }

    private static int displayPlayerMoney(CommandSourceStack source) {
        if (source.getPlayer() != null) {
            source.sendSuccess(() -> Component.literal("/money command executed!"), true);
            return 1;
        }
        else {
            source.sendFailure(Component.literal("The /money command must be executed by a player!"));
            return -1;
        }
    }
}
