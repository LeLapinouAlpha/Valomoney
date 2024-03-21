package fr.valorantage.valomoney.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.items.ItemsRegister;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.IntRange;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;


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
                    totalMoney += getValueFromItemStack(itemStack);
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
                var sourcePlayer = source.getPlayer();
                var sourceInventory = sourcePlayer.getInventory();
                var debitDistribution = sourceWallet.getDebitDistribution(amount);
                var oldDistribution = getMonetaryItemDistribution(sourceInventory);
                for (int i = 0; i < debitDistribution.length; i++) {
                    float unitValue = switch (i) {
                        case 0 -> 100.0f;
                        case 1 -> 50.0f;
                        case 2 -> 20.0f;
                        case 3 -> 10.0f;
                        case 4 -> 5.0f;
                        case 5 -> 2.0f;
                        case 6 -> 1.0f;
                        case 7 -> 0.5f;
                        default -> 0.0f;
                    };
                    int count = debitDistribution[i];
                    if (count > 0) {
                        var item = Objects.requireNonNull(getItemFromValue(unitValue));
                        sourcePlayer.addItem(new ItemStack(item, count));
                    }
                }

                var units = new float[]{100, 50, 20, 10, 5, 2, 1, 0.5f};
                var newDistribution = getMonetaryItemDistribution(sourceInventory);
                float toDebit = (float) IntStream.range(0, newDistribution.length)
                        .mapToDouble(i -> (newDistribution[i] - oldDistribution[i]) * units[i])
                        .sum();

                if (toDebit < 0) {
                    source.sendFailure(Component.literal("Your inventory is full. You don't have enough space to perform this debit."));
                    return -1;
                }
                sourcePlayer.sendSystemMessage(Component.literal(String.format("You have been debited of %.2f$ from your wallet.", toDebit)));
                sourceWallet.withdraw(toDebit);
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


    private static Item getItemFromValue(float unitValue) {
        if (unitValue == 100.0f)
            return ItemsRegister.BILL_HUNDRED.get();
        else if (unitValue == 50.0f)
            return ItemsRegister.BILL_FIFTY.get();
        else if (unitValue == 20.0f)
            return ItemsRegister.BILL_TWENTY.get();
        else if (unitValue == 10.0f)
            return ItemsRegister.BILL_TEN.get();
        else if (unitValue == 5.0f)
            return ItemsRegister.BILL_FIVE.get();
        else if (unitValue == 2.0f)
            return ItemsRegister.COIN_TWO.get();
        else if (unitValue == 1.0f)
            return ItemsRegister.COIN_ONE.get();
        else if (unitValue == 0.50f)
            return ItemsRegister.COIN_FIFTY.get();
        else
            return null;
    }

    private static float getValueFromItemStack(final ItemStack itemStack) {
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

    private static int[] getMonetaryItemDistribution(Inventory sourceInventory) {
        var distribution = new int[8];
        for (var item : sourceInventory.items) {
            if (item.getItem().equals(ItemsRegister.BILL_HUNDRED.get())) {
                distribution[0] += item.getCount();
            } else if (item.getItem().equals(ItemsRegister.BILL_FIFTY.get())) {
                distribution[1] += item.getCount();
            } else if (item.getItem().equals(ItemsRegister.BILL_TWENTY.get())) {
                distribution[2] += item.getCount();
            } else if (item.getItem().equals(ItemsRegister.BILL_TEN.get())) {
                distribution[3] += item.getCount();
            } else if (item.getItem().equals(ItemsRegister.BILL_FIVE.get())) {
                distribution[4] += item.getCount();
            } else if (item.getItem().equals(ItemsRegister.COIN_TWO.get())) {
                distribution[5] += item.getCount();
            } else if (item.getItem().equals(ItemsRegister.COIN_ONE.get())) {
                distribution[6] += item.getCount();
            } else if (item.getItem().equals(ItemsRegister.COIN_FIFTY.get())) {
                distribution[7] += item.getCount();
            }
        }

        return distribution;
    }
}
