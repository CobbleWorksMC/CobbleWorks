package curtis.cobbleworks.gui;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import curtis.cobbleworks.CommonProxy;
import curtis.cobbleworks.Config;
import curtis.cobbleworks.cobblegen.TileEntityAdvancedgen;
import curtis.cobbleworks.cobblegen.TileEntityCobblegen;
import curtis.cobbleworks.cobblegen.TileEntityCustomgen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CobbleGenGui extends GuiContainer {

	private IInventory playerInv;
	private TileEntityCobblegen te;
	private int rfBarHeight, progressBarHeight;
	private int lavaBarHeight = 0;
	private int flag = 0; //1 if the advanced block, or 2 for custom, so I can be lazy and use the same GUI for all.
	private static final String[] contained = new String[] {"cobblestone" , "stone", "sand", "glass", "gravel", "flint", "stone bricks", "sandstone", "dirt"};
	private static final String[] advContained = new String[] {"mossy cobble", "netherrack", "soul sand", "ice", "end stone", "clay", "snow", "prismarine", "obsidian"};
	private static final int[] cost = new int[] {0, 250, 500, 1000, 1500, 2000};
	private static final int[] advCost = new int[] {0, 500, 1000, 2000, 3000, 4000};
	private static final int[] incLim = new int[] {9, 36, 72, 144, 288, 576};
	private static final int[] advLim = new int[] {9, 18, 36, 72, 144, 288};
	private static final ItemStack[] stacks = new ItemStack[] {new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.STONE), new ItemStack(Blocks.SAND), 
			new ItemStack(Blocks.GLASS), new ItemStack(Blocks.GRAVEL), new ItemStack(Items.FLINT), 
			new ItemStack(Blocks.STONEBRICK), new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.DIRT)};
	private static final ItemStack[] stacksOnStacks = new ItemStack[] {new ItemStack(Blocks.MOSSY_COBBLESTONE), new ItemStack(Blocks.NETHERRACK), new ItemStack(Blocks.SOUL_SAND), 
			new ItemStack(Blocks.ICE), new ItemStack(Blocks.END_STONE), new ItemStack(Blocks.CLAY), 
			new ItemStack(Blocks.SNOW), new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Blocks.OBSIDIAN)};
	
	public CobbleGenGui(IInventory playerInv, TileEntityCobblegen te) {
		super(new ContainerCobblegen(playerInv, te));
		
		this.playerInv = playerInv;
		this.te = te;
		this.rfBarHeight = (int)(52*(this.te.power.getEnergyStored()/(float)this.te.power.getMaxEnergyStored()));
		this.progressBarHeight = (int)(52*(this.te.getProgress()/(float)100));
		this.xSize = 227;
		this.ySize = 165;
		
		if (te instanceof TileEntityAdvancedgen) {
			flag = 1;
		}
		
		if (te instanceof TileEntityCustomgen) {
			flag = 2;
		}
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
		
		if (GuiScreen.isShiftKeyDown()) {
			amount *= 64;
		} else if (GuiScreen.isCtrlKeyDown()) {
			amount *= 16;
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
		
		if (flag != 0) {
			this.lavaBarHeight = ((TileEntityAdvancedgen)this.te).calcLavaBarHeight();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		
		if (flag != 0) {
			this.mc.getTextureManager().bindTexture(new ResourceLocation("cobbleworks", "textures/gui/guiAdvancedWorks.png"));
		} else {
			this.mc.getTextureManager().bindTexture(new ResourceLocation("cobbleworks", "textures/gui/guiCobbleWorks.png"));
		}
		
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.drawRect(guiLeft+8, guiTop+69-(this.rfBarHeight), guiLeft+16, guiTop+69, 0xffff0000);
		this.drawRect(guiLeft+212, guiTop+69-(this.progressBarHeight), guiLeft+220, guiTop+69, 0xff008000);
		
		if (flag == 2) {
			for (int i = 0; i < 9; i++) {
				this.itemRender.renderItemAndEffectIntoGUI(TileEntityCustomgen.generated[i], guiLeft+34+18*i, guiTop+34);
			}
		} else if (flag == 1) {
			for (int i = 0; i < 9; i++) {
				this.itemRender.renderItemAndEffectIntoGUI(stacksOnStacks[i], guiLeft+34+18*i, guiTop+34);
			}
		} else {
			for (int i = 0; i < 9; i++) {
				this.itemRender.renderItemAndEffectIntoGUI(stacks[i], guiLeft+34+18*i, guiTop+34);
			}
		}
		
		if (flag != 0) {
			this.drawRect(guiLeft+20, guiTop+69-(this.lavaBarHeight), guiLeft+28, guiTop+69, 0xffa50000);
		}
		
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		String s = "Manage Thy " + this.te.getName();
	    this.fontRenderer.drawString(s, 113 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
	    
	    
	    if ((mouseX > guiLeft+7) && (mouseX < guiLeft+16) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    	
	    	int rfcost;
	    	int level = this.te.getLevel();
	    	if (flag == 2) {
	    		rfcost = Config.customPower[level];
	    	} else if (flag == 1) {
	    		rfcost = this.advCost[level];
	    	} else {
	    		rfcost = this.cost[level];
	    	}
	    	float actualCost = (rfcost * (float)te.calcProduced());
	    	if (flag != 1) {
	    		actualCost /= advLim[te.getLevel()];
	    	} else {
	    		actualCost /= incLim[te.getLevel()];
	    	}
	    	
	    	String text = ("RF: " + this.te.power.getEnergyStored() + "/" + this.te.power.getMaxEnergyStored() + ", using " + actualCost + " RF/t");
		    List display = Arrays.asList(text);
	    	this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
	    }
	    
	    if ((mouseX > guiLeft+211) && (mouseX < guiLeft+220) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    	String text = ("Progress: " + this.te.getProgress() + "/100");
		    List display = Arrays.asList(text);
		    this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
	    }
	    
	    if (flag != 0) {
	    	if ((mouseX > guiLeft+19) && (mouseX < guiLeft+29) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    		float cost = ((TileEntityAdvancedgen)this.te).calcLavaCostFloat();
	    		if (flag == 2) {
	    			cost = ((TileEntityCustomgen)this.te).calcLavaCostFloat();
	    		}
	    		String text = ("Lava: " + ((TileEntityAdvancedgen)this.te).lava.getFluidAmount() + "/" + ((TileEntityAdvancedgen)this.te).lava.getCapacity() + ", using " + cost + " mB of lava per tick");
	    		List display = Arrays.asList(text);
	    		this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
	    	}
	    }
	    
	    
	    for (int qw = 0; qw < 9; qw++) {
	    	if (mouseY > guiTop + 16 && mouseY < guiTop + 69) {
	    		if (mouseX > guiLeft+34+18*qw && mouseX < guiLeft+50+18*qw) {
	    			String str;
	    			if (flag == 0) {
	    				str = "Generating: " + this.te.getProduceAmount(qw) + " " + this.contained[qw];
	    			} else if (flag == 1) {
	    				str = "Generating: " + this.te.getProduceAmount(qw) + " " + this.advContained[qw];
	    			} else if (flag == 2) {
	    				str = "Generating: " + this.te.getProduceAmount(qw) + " " + TileEntityCustomgen.generated[qw].getDisplayName();
	    			} else {
	    				str = " ";
	    			}
	    			this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRenderer);
	    		}
	    	}
	    }
	}
}
