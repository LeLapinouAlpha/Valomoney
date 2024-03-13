package fr.valorantage.valomoney.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public final class ShopCommand extends ModCommand {
    public ShopCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void register() {
        dispatcher.register(Commands.literal("shop")
                .executes((command) -> show(command.getSource()))
                .then(Commands.literal("sell")
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes((command) -> sell(command.getSource(), IntegerArgumentType.getInteger(command, "amount")))
                        )
                )
        );
    }

    public static int show(CommandSourceStack source) {
        // TODO: Display the shop GUI
        source.sendSuccess(() -> Component.literal("This command must display the shop!"), true);
        return 1;
    }

    public static int sell(CommandSourceStack source, int amount) {
        if (amount <= 0) {
            source.sendFailure(Component.literal("The amout of items to sell must be positive"));
            return -1;
        }

        // TODO: Sell 'amount' items that are of the same types as the item in the hand of the source player
        source.sendSuccess(() -> Component.literal("/shop sell <amount> command executed!"), true);
        return 1;
    }
}
