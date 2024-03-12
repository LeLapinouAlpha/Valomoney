package fr.valorantage.valomoney.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class PayCommand {

    public PayCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("pay")
                        .then(
                                Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                                .executes((command) -> pay(command.getSource(), EntityArgument.getPlayer(command, "player"), FloatArgumentType.getFloat(command, "amount"))))
                        )
        );
    }

    private static int pay(CommandSourceStack source, ServerPlayer target, float amount) {
        if (amount < 0) {
            source.sendFailure(Component.literal("The amount to pay must be strictly positive."));
            return -1;
        }
        source.sendSuccess(() -> Component.literal("/pay <player> <amount> has been executed!"), true);
        return 1;
    }
}
