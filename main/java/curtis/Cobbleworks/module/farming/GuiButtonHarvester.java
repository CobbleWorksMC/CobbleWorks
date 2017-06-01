package curtis.Cobbleworks.module.farming;

import curtis.Cobbleworks.Cobbleworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonHarvester extends GuiButton {
	
	public ResourceLocation texture;
	
	public GuiButtonHarvester(int buttonId, int x, int y, String buttonText) {
		this(buttonId, x, y, 200, 20, buttonText, "textures/gui/button/guiButtonCobbleworks.png");
	}
	
	public GuiButtonHarvester(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String texture) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.texture = new ResourceLocation(Cobbleworks.MODID, texture);
	}
	
	public GuiButtonHarvester(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		this(buttonId, x, y, widthIn, heightIn, buttonText, "textures/gui/button/guiButtonCobbleworks.png");
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(texture);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }
}
