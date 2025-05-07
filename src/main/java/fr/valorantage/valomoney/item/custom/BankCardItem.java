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
import org.slf4j.Logger;

import java.util.List;
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

            // FIXME: Refactor the way to read/write player UUID from data component
            var stack = player.getItemInHand(usedHand);
            String storedPlayerUUID = stack.get(ModDataComponentTypes.PLAYER_UUID);
            if (storedPlayerUUID == null) {
                player.getItemInHand(usedHand).set(ModDataComponentTypes.PLAYER_UUID, player.getStringUUID());
                LOGGER.debug("Bound bank card to player '{}'", player.getDisplayName().getString());
            } else {
                // FIXME: Must check if storedPlayer is null
                var storedPlayer = level.getPlayerByUUID(UUID.fromString(storedPlayerUUID));
                LOGGER.debug("This bank card has already been bind to player '{}'", storedPlayer.getDisplayName().getString());
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        // FIXME: Refactor the way to read/write player UUID from data component
        String storedPlayerUUID = stack.get(ModDataComponentTypes.PLAYER_UUID);
        if (storedPlayerUUID != null) {
            var storedPlayer = context.level().getPlayerByUUID(UUID.fromString(storedPlayerUUID));
            if (storedPlayer != null) {
                // TODO: Optimize the number of packets send to the server (one packet sent by frame rendered)
                // FIXME: storedPlayer is not used, so any player will see it's balance, so the data component is useless
                PacketDistributor.sendToServer(new PlayerMoneyPayload(0));

                tooltipComponents.add(Component.literal(String.format("Money: %.2f$", PLAYER_MONEY)));
            }
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
