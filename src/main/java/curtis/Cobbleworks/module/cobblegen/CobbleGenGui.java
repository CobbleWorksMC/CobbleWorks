package curtis.Cobbleworks.module.cobblegen;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.PacketSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CobbleGenGui extends GuiContainer {

	private IInventory playerInv;
	private TileEntityCobbleGen te;
	int rfBarHeight, progressBarHeight;
	private static final String[][] contained = {{"cobblestone" , "stone", "sand"}, 
												 {"glass", "gravel", "flint"}, 
												 {"stone bricks", "sandstone", "dirt"}};

	public CobbleGenGui(IInventory playerInv, TileEntityCobbleGen te) {
		super(new ContainerCobblegen(playerInv, te));
		
		this.playerInv = playerInv;
		this.te = te;
		this.rfBarHeight = (int)(52*(this.te.power.getEnergyStored()/(float)this.te.power.getMaxEnergyStored()));
		this.progressBarHeight = (int)(52*(this.te.getProgress()/(float)100));
		this.xSize = 175;
		this.ySize = 165;
	}
	
	@Override
	protected void actionPerformed(GuiButton b) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("Toggle", b.id);
		CommonProxy.packetHandler.sendToServer(new PacketSync(this.te, nbt));
	}
	
	@Override
	public void initGui() {
		//System.out.println("called initGui() for cobblegen");
		super.initGui();
		this.buttonList.clear();
		int q = 0;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				this.buttonList.add(new GuiButton(q, guiLeft+26+18*j, guiTop+17+18*i, 16, 16, Integer.toString(q)));
				q++;
			}
		}
	}
	
	@Override
	public void drawScreen(int x, int y, float p) {
		super.drawScreen(x, y, p);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		//PacketSync.sendToServer();
		this.rfBarHeight = (int)(52*(this.te.power.getEnergyStored()/(float)this.te.power.getMaxEnergyStored()));
		this.progressBarHeight = (int)(52*(this.te.getProgress()/(float)100)); //Max progress is 100 ticks.
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(new ResourceLocation("cobbleworks", "textures/gui/guiCobbleWorks.png"));
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.drawRect(guiLeft+8, guiTop+69-(this.rfBarHeight), guiLeft+16, guiTop+69, 0xffff0000);
		this.drawRect(guiLeft+93, guiTop+69-(this.progressBarHeight), guiLeft+101, guiTop+69, 0xff008000);		
	} 
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		String s = "Tier " + this.te.getLevel() + " Cobblegen";
	    this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
	    
	    
	    if((mouseX > guiLeft+7) && (mouseX < guiLeft+16) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    	String text = ("RF: " + this.te.power.getEnergyStored() + "/" + this.te.power.getMaxEnergyStored());
		    List display = Arrays.asList(text);
	    	this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if((mouseX > guiLeft+93) && (mouseX < guiLeft+100) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    	String text = ("Progress: " + this.te.getProgress() + "/100");
		    List display = Arrays.asList(text);
		    this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    this.tooltip(mouseX, mouseY);
	}
	
	protected String extraTip(int i) {
		String result;
		if(this.te.getAbility(i)) {
			result = "Currently Enabled";
		} else {
			result = "Currently Disabled";
		}
		return result;
	}
	
	protected void tooltip(int x, int y) {
		for(int j = 0; j < 3; j++){
			for(int i = 0; i < 3; i++) {
				if((x > guiLeft+26+18*j) && (x < guiLeft+26+18*(j+1)) && (y > guiTop+17+18*i) && (y < guiTop+17+18*(i+1))){
					List tooltips = Arrays.asList("Toggle " + this.contained[i][j] + ". " + extraTip(i*3+j));
					this.drawHoveringText(tooltips, x - guiLeft, y - guiTop, fontRendererObj);
				}
			}
		}
	}
}
