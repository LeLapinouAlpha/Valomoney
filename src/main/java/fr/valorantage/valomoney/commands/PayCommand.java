package fr.valorantage.valomoney.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.checkerframework.checker.units.qual.C;

import java.util.Objects;

public final class PayCommand extends ModCommand {

    public PayCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void register() {
        dispatcher.register(Commands.literal("pay")
                        .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                                .executes((command) -> pay(command.getSource(), EntityArgument.getPlayer(command, "player"), FloatArgumentType.getFloat(command, "amount"))))
                        )
        );
    }

    private static int pay(CommandSourceStack source, ServerPlayer target, float amount) {
        try {
            ValomoneyMod.ECONOMY_MANAGER.performTransaction(Objects.requireNonNull(source.getPlayer()).getUUID(), target.getUUID(), amount);
            source.sendSuccess(() -> Component.literal(String.format("You have send %.2f$ to %s.", amount, target.getDisplayName())), true);
            return 1;
        } catch (Exception ex) {
            source.sendFailure(Component.literal(ex.getMessage()));
            return -1;
        }
    }
}
