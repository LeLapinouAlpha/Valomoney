package fr.valorantage.valomoney.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;


public final class MoneyCommand {
    public MoneyCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("money")
                        .executes((command -> displayPlayerMoney(command.getSource())))
                        .then(Commands.literal("credit")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                        .executes((command -> credit(command.getSource(), FloatArgumentType.getFloat(command, "amount")))))
                        )
                        .then(Commands.literal("debit")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                        .executes((command -> debit(command.getSource(), FloatArgumentType.getFloat(command, "amount")))))
                        )
        );
    }

    private static int displayPlayerMoney(CommandSourceStack source) {
        if (source.getPlayer() != null) {
            source.sendSuccess(() -> Component.literal("/money command executed!"), true);
            return 1;
        } else {
            source.sendFailure(Component.literal("The /money command must be executed by a player!"));
            return -1;
        }
    }

    private static int credit(CommandSourceStack source, float amount) {
        if (source.getPlayer() != null) {
            source.sendSuccess(() -> Component.literal("The /money credit <amount> has been executed"), true);
            return 1;
        } else {
            return -1;
        }
    }

    private static int debit(CommandSourceStack source, float amount) {
        if (source.getPlayer() != null) {
            source.sendSuccess(() -> Component.literal("The /money debit <amount> has been executed"), true);
            return 1;
        } else {
            return -1;
        }
    }
}
