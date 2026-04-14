package fr.valorantage.valomoney.item.custom;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.component.ModDataComponentTypes;
import fr.valorantage.valomoney.network.cache.BankCardClientCache;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;

public class BankCardItem extends Item {
    private final static Logger LOGGER = LogUtils.getLogger();

    public BankCardItem() {
        super(new Item.Properties());

    }

    public UUID getBoundPlayerUUID(ItemStack stack) {
        var storedPlayerUUIDString = stack.get(ModDataComponentTypes.PLAYER_UUID);
        return storedPlayerUUIDString == null ? null : UUID.fromString(storedPlayerUUIDString);
    }

    public void BindToPlayer(ItemStack stack, Player player) {
        var storedPlayerUUID = getBoundPlayerUUID(stack);
        if (storedPlayerUUID == null) {
            stack.set(ModDataComponentTypes.PLAYER_UUID, player.getUUID().toString());

            LOGGER.debug("Bound bank card to player '{}'", player.getDisplayName().getString());
        } else {
            LOGGER.debug("This bank card has already been bind to player '{}'",
                    player.getDisplayName().getString());
        }
    }

    public Float getBoundPlayerMoney(ItemStack stack) {
        var storedPlayerUUID = getBoundPlayerUUID(stack);
        if (storedPlayerUUID == null) {
            return null;
        }

        BankCardClientCache.maybeRequestMoney(storedPlayerUUID);
        return BankCardClientCache.getCachedMoney(storedPlayerUUID);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            LOGGER.debug("{} used bank card", player.getDisplayName().getString());

            var stack = player.getItemInHand(usedHand);
            this.BindToPlayer(stack, player);
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        var money = this.getBoundPlayerMoney(stack);
        if (money != null) {
            tooltipComponents.add(Component.literal(String.format("Money: %.2f$", money)));
        } else {
            tooltipComponents.add(Component.literal("Money: Loading..."));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
