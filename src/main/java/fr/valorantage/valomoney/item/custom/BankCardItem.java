package fr.valorantage.valomoney.item.custom;

import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.attachment.ModAttachmentTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.slf4j.Logger;

import java.util.List;

public class BankCardItem extends Item {
    private final static Logger LOGGER = LogUtils.getLogger();

    private Player player;

    public BankCardItem() {
        super(new Item.Properties());

        this.player = null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            LOGGER.debug("{} used bank card", player.getDisplayName().getString());
            if (this.player == null) {
                this.player = player;
                LOGGER.debug("Bound bank card to player '{}'", this.player.getDisplayName().getString());
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (this.player != null) {
            float playerMoney = player.getData(ModAttachmentTypes.MONEY);
            tooltipComponents.add(Component.literal(String.format("Money: %.2f$", playerMoney)));
        }
    }
}
