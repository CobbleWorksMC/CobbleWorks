package curtis.Cobbleworks.module.cobblegen;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.PacketSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CobbleGenGui extends GuiContainer {

	private IInventory playerInv;
	private TileEntityCobbleGen te;
	int rfBarHeight, progressBarHeight;
	private static final String[] contained = {"cobblestone" , "stone", "sand", "glass", "gravel", "flint", "stone bricks", "sandstone", "dirt"};

	public CobbleGenGui(IInventory playerInv, TileEntityCobbleGen te) {
		super(new ContainerCobblegen(playerInv, te));
		
		this.playerInv = playerInv;
		this.te = te;
		this.rfBarHeight = (int)(52*(this.te.power.getEnergyStored()/(float)this.te.power.getMaxEnergyStored()));
		this.progressBarHeight = (int)(52*(this.te.getProgress()/(float)100));
		this.xSize = 227;
		this.ySize = 165;
	}
	
	@Override
	protected void actionPerformed(GuiButton b) {
		NBTTagCompound nbt = new NBTTagCompound();
		int amount = 0;
		
		nbt.setInteger("index", b.id/2);
		
		if (b.id % 2 == 0) {
			amount = 1;
		} else {
			amount = -1;
		}
		
		nbt.setInteger("amount", amount);
		
		CommonProxy.packetHandler.sendToServer(new PacketSync(this.te, nbt));
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		int q = 0;
		for(int i = 0; i < 9; i++) {
			this.buttonList.add(new GuiButtonCobbleworks(q++, guiLeft+34+18*i, guiTop+16, 16, 16, "+"));
			this.buttonList.add(new GuiButtonCobbleworks(q++, guiLeft+34+18*i, guiTop+53, 16, 16, "-"));
		}
	}
	
	@Override
	public void drawScreen(int x, int y, float p) {
		super.drawScreen(x, y, p);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.rfBarHeight = (int)(52*(this.te.power.getEnergyStored()/(float)this.te.power.getMaxEnergyStored()));
		this.progressBarHeight = (int)(52*(this.te.getProgress()/(float)100)); //Max progress is 100 ticks.
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(new ResourceLocation("cobbleworks", "textures/gui/guiCobbleWorks.png"));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.drawRect(guiLeft+8, guiTop+69-(this.rfBarHeight), guiLeft+16, guiTop+69, 0xffff0000);
		this.drawRect(guiLeft+212, guiTop+69-(this.progressBarHeight), guiLeft+220, guiTop+69, 0xff008000);
		
		//for (int i = 0; i < 9; i++) {
		//	this.drawModalRectWithCustomSizedTexture(guiLeft+34, guiTop+34, 0, 166, 16, 16, 32, 32);
		//}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		String s = "Manage Thy Tier " + this.te.getLevel() + " Cobblegen";
	    this.fontRendererObj.drawString(s, 113 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
	    
	    
	    if ((mouseX > guiLeft+7) && (mouseX < guiLeft+16) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    	String text = ("RF: " + this.te.power.getEnergyStored() + "/" + this.te.power.getMaxEnergyStored());
		    List display = Arrays.asList(text);
	    	this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if ((mouseX > guiLeft+211) && (mouseX < guiLeft+220) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    	String text = ("Progress: " + this.te.getProgress() + "/100");
		    List display = Arrays.asList(text);
		    this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    
	    for (int qw = 0; qw < 9; qw++) {
	    	if (mouseY > guiTop + 16 && mouseY < guiTop + 69) {
	    		if (mouseX > guiLeft+34+18*qw && mouseX < guiLeft+50+18*qw) {
	    			String str = "Generating: " + this.te.getProduceAmount(qw) + " " + this.contained[qw];
	    			this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    		}
	    	}
	    }
	}
}
