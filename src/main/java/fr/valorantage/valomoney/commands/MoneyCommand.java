package fr.valorantage.valomoney.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;


public final class MoneyCommand extends ModCommand {
    public MoneyCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void register() {
        dispatcher.register(
                Commands.literal("money")
                        .executes((command -> display(command.getSource())))
                        .then(Commands.literal("credit")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                        .executes((command -> credit(command.getSource(), FloatArgumentType.getFloat(command, "amount")))))
                        )
                        .then(Commands.literal("debit")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                        .executes((command -> debit(command.getSource(), FloatArgumentType.getFloat(command, "amount")))))
                        )
                        .then(Commands.argument("player", EntityArgument.player())
                                .requires((source) -> source.hasPermission(2))
                                .executes((command) -> display(command.getSource(), EntityArgument.getPlayer(command, "player"))))
        );
    }

    private static int display(CommandSourceStack source) {
        if (source.getPlayer() != null) {
            try {
                var sourceWallet = ValomoneyMod.ECONOMY_MANAGER.getWallet(source.getPlayer().getUUID());
                source.sendSuccess(() -> Component.literal(String.format("Balance: %.2f$", sourceWallet.getBalance())), true);
                return 1;
            } catch (IllegalArgumentException walletNotFoundEx) {
                source.sendFailure(Component.literal(walletNotFoundEx.getMessage()));
                return -1;
            }
        } else {
            source.sendFailure(Component.literal("The /money command must be executed by a player!"));
            return -1;
        }
    }

    private static int display(CommandSourceStack source, ServerPlayer target) {
        try {
            var targetWallet = ValomoneyMod.ECONOMY_MANAGER.getWallet(target.getUUID());
            source.sendSuccess(() -> Component.literal(String.format("%s's balance: %.2f$", target.getDisplayName().getString(), targetWallet.getBalance())), true);
            return 1;
        } catch (IllegalArgumentException walletNotFoundEx) {
            source.sendFailure(Component.literal(walletNotFoundEx.getMessage()));
            return -1;
        }
    }

    private static int credit(CommandSourceStack source, float amount) {
        /* TODO: Get the wallet of the source player using the running instance of EconomyManager, check in all player's
            inventory if it haves enough coins and bills, and exchange them into virtual money calling addMoney method of
            player's wallet */
        if (source.getPlayer() != null) {
            source.sendSuccess(() -> Component.literal("The /money credit <amount> has been executed"), true);
            return 1;
        } else {
            return -1;
        }
    }

    private static int debit(CommandSourceStack source, float amount) {
        /* TODO: Get the Get the wallet of the source player using the running instance of EconomyManager, check if it
            haves enough funds and give its bills and coins to the rounded asked amount.
         */
        if (source.getPlayer() != null) {
            source.sendSuccess(() -> Component.literal("The /money debit <amount> has been executed"), true);
            return 1;
        } else {
            return -1;
        }
    }
}
