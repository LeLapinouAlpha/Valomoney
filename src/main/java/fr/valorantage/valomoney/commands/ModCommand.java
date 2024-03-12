package fr.valorantage.valomoney.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public abstract class ModCommand {
    protected CommandDispatcher<CommandSourceStack> dispatcher;

    public ModCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        this.dispatcher = dispatcher;
        register();
    }

    protected abstract void register();
}
