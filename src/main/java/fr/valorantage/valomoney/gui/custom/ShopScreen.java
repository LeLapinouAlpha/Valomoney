package fr.valorantage.valomoney.gui.custom;

import org.slf4j.Logger;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;

import fr.valorantage.valomoney.ValomoneyMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ShopScreen extends AbstractContainerScreen<ShopBlockMenu> {
    private final static Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(ValomoneyMod.MODID,
            "textures/gui/shop/shop_gui.png");

    public ShopScreen(ShopBlockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        // Set real size of GUI inside the texture (the non-transparent part)
        this.imageWidth = 190;
        this.imageHeight = 208;
        this.inventoryLabelX = 15;
        this.inventoryLabelY = 110;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

}
