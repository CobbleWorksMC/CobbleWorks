package curtis.Cobbleworks.module.spawner;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiSummoner extends GuiContainer {
	
	private IInventory playerInv;
	private TileEntityPendulumSummoner teps;
	
	public GuiSummoner(IInventory playerInv, TileEntityPendulumSummoner teps) {
		super(new ContainerSpawner(playerInv, teps));
		
		this.playerInv = playerInv;
		this.teps = teps;
		this.xSize = 175;
		this.ySize = 165;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(new ResourceLocation("cobbleworks", "textures/gui/guiSpawner.png"));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

}
