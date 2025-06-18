package fr.valorantage.valomoney.item.custom;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.component.ModDataComponentTypes;
import fr.valorantage.valomoney.network.packet.PlayerMoneyPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BankCardItem extends Item {
    private final static Logger LOGGER = LogUtils.getLogger();

    public static float PLAYER_MONEY = 0.f;

    public BankCardItem() {
        super(new Item.Properties());

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            LOGGER.debug("{} used bank card", player.getDisplayName().getString());

            var stack = player.getItemInHand(usedHand);
            var storedPlayerUUID = getBoundPlayerUUID(stack);
            if (storedPlayerUUID == null) {
                setBoundPlayerUUID(stack, player.getUUID());
                LOGGER.debug("Bound bank card to player '{}'", player.getDisplayName().getString());
            } else {
                LOGGER.debug("This bank card has already been bind to player '{}'", player.getDisplayName().getString());
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        UUID storedPlayerUUID = getBoundPlayerUUID(stack);
        if (storedPlayerUUID != null) {
            var storedPlayer = context.level().getPlayerByUUID(storedPlayerUUID);
            if (storedPlayer != null) {
                // TODO: Optimize the number of packets send to the server (one packet sent by frame rendered)
                PacketDistributor.sendToServer(new PlayerMoneyPayload(storedPlayerUUID, -1.f));

                tooltipComponents.add(Component.literal(String.format("Money: %.2f$", PLAYER_MONEY)));
            }
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public UUID getBoundPlayerUUID(ItemStack stack) {
        var storedPlayerUUIDString = stack.get(ModDataComponentTypes.PLAYER_UUID);
        return storedPlayerUUIDString == null ? null : UUID.fromString(storedPlayerUUIDString);
    }

    public void setBoundPlayerUUID(ItemStack stack, @NotNull UUID playerUUID) {
        stack.set(ModDataComponentTypes.PLAYER_UUID, playerUUID.toString());
    }
}
