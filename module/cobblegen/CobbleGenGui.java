package curtis.Cobbleworks.module.cobblegen;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.PacketSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CobbleGenGui extends GuiContainer {

	private IInventory playerInv;
	private TileEntityCobbleGen te;
	private int rfBarHeight, progressBarHeight;
	private int lavaBarHeight = 0;
	private boolean flag = false; //True if the advanced block, so I can be lazy and use the same GUI for both.
	private static final String[] contained = new String[] {"cobblestone" , "stone", "sand", "glass", "gravel", "flint", "stone bricks", "sandstone", "dirt"};
	private static final String[] advContained = new String[] {"mossy cobble", "netherrack", "soul sand", "ice", "end stone", "clay", "snow", "prismarine", "obsidian"};
	private static final int[] cost = new int[] {0, 250, 500, 1000, 1500, 2000};
	private static final int[] advCost = new int[] {0, 500, 1000, 2000, 3000, 4000};
	private static final ItemStack[] stacks = new ItemStack[] {new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.STONE), new ItemStack(Blocks.SAND), 
			new ItemStack(Blocks.GLASS), new ItemStack(Blocks.GRAVEL), new ItemStack(Items.FLINT), 
			new ItemStack(Blocks.STONEBRICK), new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.DIRT)};
	private static final ItemStack[] stacksOnStacks = new ItemStack[] {new ItemStack(Blocks.MOSSY_COBBLESTONE), new ItemStack(Blocks.NETHERRACK), new ItemStack(Blocks.SOUL_SAND), 
			new ItemStack(Blocks.ICE), new ItemStack(Blocks.END_STONE), new ItemStack(Blocks.CLAY), 
			new ItemStack(Blocks.SNOW), new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Blocks.OBSIDIAN)};
	
	public CobbleGenGui(IInventory playerInv, TileEntityCobbleGen te) {
		super(new ContainerCobblegen(playerInv, te));
		
		this.playerInv = playerInv;
		this.te = te;
		this.rfBarHeight = (int)(52*(this.te.power.getEnergyStored()/(float)this.te.power.getMaxEnergyStored()));
		this.progressBarHeight = (int)(52*(this.te.getProgress()/(float)100));
		this.xSize = 227;
		this.ySize = 165;
		
		if (te instanceof TileEntityAdvancedGen) {
			flag = true;
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
		
		if (flag) {
			this.lavaBarHeight = ((TileEntityAdvancedGen)this.te).calcLavaBarHeight();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		if (flag) {
			this.mc.getTextureManager().bindTexture(new ResourceLocation("cobbleworks", "textures/gui/guiAdvancedWorks.png"));
		} else {
			this.mc.getTextureManager().bindTexture(new ResourceLocation("cobbleworks", "textures/gui/guiCobbleWorks.png"));
		}
		
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.drawRect(guiLeft+8, guiTop+69-(this.rfBarHeight), guiLeft+16, guiTop+69, 0xffff0000);
		this.drawRect(guiLeft+212, guiTop+69-(this.progressBarHeight), guiLeft+220, guiTop+69, 0xff008000);
		
		if (flag) {
			for (int i = 0; i < 9; i++) {
				this.itemRender.renderItemAndEffectIntoGUI(stacksOnStacks[i], guiLeft+34+18*i, guiTop+34);
			}
		} else {
			for (int i = 0; i < 9; i++) {
				this.itemRender.renderItemAndEffectIntoGUI(stacks[i], guiLeft+34+18*i, guiTop+34);
			}
		}
		
		if (flag) {
			this.drawRect(guiLeft+20, guiTop+69-(this.lavaBarHeight), guiLeft+28, guiTop+69, 0xffa50000);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		String s = "Manage Thy " + this.te.getName();
	    this.fontRendererObj.drawString(s, 113 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
	    
	    
	    if ((mouseX > guiLeft+7) && (mouseX < guiLeft+16) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    	
	    	int rfcost;
	    	int level = this.te.getLevel();
	    	if (flag) {
	    		rfcost = this.advCost[level];
	    	} else {
	    		rfcost = this.cost[level];
	    	}
	    	
	    	String text = ("RF: " + this.te.power.getEnergyStored() + "/" + this.te.power.getMaxEnergyStored() + ", using " + rfcost + " RF/t");
		    List display = Arrays.asList(text);
	    	this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if ((mouseX > guiLeft+211) && (mouseX < guiLeft+220) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    	String text = ("Progress: " + this.te.getProgress() + "/100");
		    List display = Arrays.asList(text);
		    this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if (flag) {
	    	if ((mouseX > guiLeft+19) && (mouseX < guiLeft+29) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    		float cost = ((TileEntityAdvancedGen)this.te).calcLavaCostFloat();
	    		String text = ("Lava: " + ((TileEntityAdvancedGen)this.te).lava.getFluidAmount() + "/" + ((TileEntityAdvancedGen)this.te).lava.getCapacity() + ", using " + cost + " mB of lava per tick");
	    		List display = Arrays.asList(text);
	    		this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    	}
	    }
	    
	    
	    for (int qw = 0; qw < 9; qw++) {
	    	if (mouseY > guiTop + 16 && mouseY < guiTop + 69) {
	    		if (mouseX > guiLeft+34+18*qw && mouseX < guiLeft+50+18*qw) {
	    			String str;
	    			if (!flag) {
	    				str = "Generating: " + this.te.getProduceAmount(qw) + " " + this.contained[qw];
	    			} else {
	    				str = "Generating: " + this.te.getProduceAmount(qw) + " " + this.advContained[qw];
	    			}
	    			this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    		}
	    	}
	    }
	}
}
