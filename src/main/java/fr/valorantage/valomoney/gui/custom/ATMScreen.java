package fr.valorantage.valomoney.gui.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import fr.valorantage.valomoney.ValomoneyMod;
import fr.valorantage.valomoney.network.packet.TransactionKind;
import fr.valorantage.valomoney.network.packet.TransactionPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

public class ATMScreen extends AbstractContainerScreen<ATMMenu> {
    private final static Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(ValomoneyMod.MODID,
            "textures/gui/atm/atm_gui.png");

    private EditBox amountEditBox;
    private Button creditButton;
    private Button debitButton;

    public ATMScreen(ATMMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        // Set real size of GUI inside the texture (the non-transparent part)
        this.imageWidth = 190;
        this.imageHeight = 170;
    }

    @Override
    protected void init() {
        String lastAmountStr = "0.00";
        if (this.amountEditBox != null) {
            lastAmountStr = this.amountEditBox.getValue();
        } else {
            lastAmountStr = String.valueOf(this.menu.getBlockEntity().getLastAmount());
        }

        super.init();

        // Create the money amount edit box
        this.amountEditBox = new EditBox(this.font, this.leftPos + 50, this.topPos + 22, 120, 20,
                Component.literal("Amount"));
        this.amountEditBox.setMaxLength(10);
        this.amountEditBox.setVisible(true);
        this.amountEditBox.setValue(lastAmountStr);
        this.addRenderableWidget(this.amountEditBox);

        // Create the credit button
        this.creditButton = Button.builder(Component.literal("Credit"), this::onCreditButtonClicked)
                .pos(this.leftPos + 25, this.topPos + 50)
                .size(40, 20)
                .build();
        this.addRenderableWidget(this.creditButton);

        // Create the debit button
        this.debitButton = Button.builder(Component.literal("Debit"), this::onDebitButtonClicked)
                .pos(this.leftPos + 80, this.topPos + 50)
                .size(40, 20)
                .build();
        this.addRenderableWidget(this.debitButton);
    }

    @Override
    public void removed() {
        // Save current value back to the block entity so reopen preserves it
        this.menu.getBlockEntity().setLastAmount(Float.parseFloat(this.amountEditBox.getValue()));
        super.removed();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void onCreditButtonClicked(Button button) {
        try {
            float amount = Float.parseFloat(this.amountEditBox.getValue());

            var payload = new TransactionPayload(TransactionKind.CREDIT, amount);
            LOGGER.debug("Sending payload to server: {}", payload);
            PacketDistributor.sendToServer(payload);

        } catch (NumberFormatException numberFormatException) {
            LOGGER.error("Could not parse amount: '{}'", this.amountEditBox.getValue());
        }
    }

    private void onDebitButtonClicked(Button button) {
        try {
            float amount = Float.parseFloat(this.amountEditBox.getValue());

            var payload = new TransactionPayload(TransactionKind.DEBIT, amount);
            LOGGER.debug("Sending payload to server: {}", payload);
            PacketDistributor.sendToServer(payload);

        } catch (NumberFormatException numberFormatException) {
            LOGGER.error("Could not parse amount: '{}'", this.amountEditBox.getValue());
        }
    }
}
