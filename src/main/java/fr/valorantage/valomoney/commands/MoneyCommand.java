package fr.valorantage.valomoney.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.items.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;


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
                                .executes((command -> credit(command.getSource())))
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

    private static int credit(CommandSourceStack source) {
        if (source.getPlayer() != null) {
            try {
                var sourceWallet = ValomoneyMod.ECONOMY_MANAGER.getWallet(source.getPlayer().getUUID());
                var sourceInventory = source.getPlayer().getInventory();
                float totalMoney = 0.0f;
                for (var itemStack : sourceInventory.items) {

                    sourceInventory.removeItem(itemStack);
                    totalMoney += getMonetaryItemStackValue(itemStack);
                }
                sourceWallet.addMoney(totalMoney);
                float finalTotalMoney = totalMoney;
                source.sendSuccess(() -> Component.literal(String.format("You credited %.2f$ to your wallet.", finalTotalMoney)), true);
                return 1;
            } catch (IllegalArgumentException argEx) {
                source.sendFailure(Component.literal(argEx.getMessage()));
                return -1;
            }
        } else {
            source.sendFailure(Component.literal("The /money credit <amount> command must be executed by a player!"));
            return -1;
        }
    }

    private static int debit(CommandSourceStack source, float amount) {
        if (source.getPlayer() != null) {
            try {
                var sourceWallet = ValomoneyMod.ECONOMY_MANAGER.getWallet(source.getPlayer().getUUID());
                var units = sourceWallet.debit(amount);
                for (int i = 0; i < units.length; i++) {
                    var stack = getMonetaryItemStack(i, units[i]);
                    source.getPlayer().getInventory().add(stack);
                }
                return 1;
            } catch (IllegalArgumentException argEx) {
                source.sendFailure(Component.literal(argEx.getMessage()));
                return -1;
            }
        } else {
            source.sendFailure(Component.literal("The /money debit <amount> command must be executed by a player!"));
            return -1;
        }
    }

    private static ItemStack getMonetaryItemStack(int unitIndex, int count) {
        var stack = switch (unitIndex) {
            case 0:
                yield new ItemStack(ItemsRegister.BILL_HUNDRED.get());
            case 1:
                yield new ItemStack(ItemsRegister.BILL_FIFTY.get());
            case 2:
                yield new ItemStack(ItemsRegister.BILL_TWENTY.get());
            case 3:
                yield new ItemStack(ItemsRegister.BILL_TEN.get());
            case 4:
                yield new ItemStack(ItemsRegister.BILL_FIVE.get());
            case 5:
                yield new ItemStack(ItemsRegister.COIN_TWO.get());
            case 6:
                yield new ItemStack(ItemsRegister.COIN_ONE.get());
            case 7:
                yield new ItemStack(ItemsRegister.COIN_FIFTY.get());
            default:
                throw new IllegalArgumentException("Invalid monetary unit index");
        };
        stack.setCount(count);
        return stack;
    }

    private static float getMonetaryItemStackValue(final ItemStack itemStack) {
        float unitValue;
        if (itemStack.getItem().equals(ItemsRegister.COIN_FIFTY.get()))
            unitValue = 0.50f;
        else if (itemStack.getItem().equals(ItemsRegister.COIN_ONE.get()))
            unitValue = 1.0f;
        else if (itemStack.getItem().equals(ItemsRegister.COIN_TWO.get()))
            unitValue = 2.0f;
        else if (itemStack.getItem().equals(ItemsRegister.BILL_FIVE.get()))
            unitValue = 5.0f;
        else if (itemStack.getItem().equals(ItemsRegister.BILL_TEN.get()))
            unitValue = 10.0f;
        else if (itemStack.getItem().equals(ItemsRegister.BILL_TWENTY.get()))
            unitValue = 20.0f;
        else if (itemStack.getItem().equals(ItemsRegister.BILL_FIFTY.get()))
            unitValue = 50.0f;
        else if (itemStack.getItem().equals(ItemsRegister.BILL_HUNDRED.get()))
            unitValue = 100.0f;
        else
            unitValue = 0.0f;
        return unitValue * itemStack.getCount();
    }
}
