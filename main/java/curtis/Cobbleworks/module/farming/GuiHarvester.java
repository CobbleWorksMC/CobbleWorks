package curtis.Cobbleworks.module.farming;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GuiHarvester extends GuiContainer {
	
	TileHarvester th;
	public static final ResourceLocation background = new ResourceLocation(Cobbleworks.MODID, "textures/gui/guiHarvester.png");
	private int rfBarHeight;
	
	public GuiHarvester(IInventory playerInv, TileHarvester th) {
		super(new ContainerHarvester(playerInv, th));
		this.th = th;
		
		this.xSize = 227;
		this.ySize = 165;
		this.rfBarHeight = (int)(52*(this.th.power.getEnergyStored()/(float)this.th.power.getMaxEnergyStored()));
	}
	
	@Override
	protected void actionPerformed(GuiButton b) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("b_id", b.id);
		
		CommonProxy.packetHandler.sendToServer(new PacketHarvesterSync(this.th, nbt));
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		int q = 0;
		
		this.buttonList.add(new GuiButtonHarvester(++q, guiLeft + 160, guiTop + 17, 16, 16, "+")); //Increase Radius
		this.buttonList.add(new GuiButtonHarvester(++q, guiLeft + 160, guiTop + 53, 16, 16, "-")); //Decrease Radius
		this.buttonList.add(new GuiButtonHarvester(++q, guiLeft + 142, guiTop + 17, 16, 16, " ")); //Toggle logic harvest
		this.buttonList.add(new GuiButtonHarvester(++q, guiLeft + 178, guiTop + 17, 16, 16, " ")); //Toggle agricraft harvest
		this.buttonList.add(new GuiButtonHarvester(++q, guiLeft + 142, guiTop + 53, 16, 16, " ")); //Toggle collect mode
		this.buttonList.add(new GuiButtonHarvester(++q, guiLeft + 178, guiTop + 53, 16, 16, " ")); //Toggle void mode
		this.buttonList.add(new GuiButtonHarvester(++q, guiLeft + 142, guiTop + 35, 16, 16, " ")); //Toggle tall plants
		this.buttonList.add(new GuiButtonHarvester(++q, guiLeft + 178, guiTop + 35, 16, 16, " ")); //Toggle trees
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(background);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
	    this.drawRect(guiLeft+8, guiTop+69-(this.rfBarHeight), guiLeft+16, guiTop+69, 0xffff0000);
	    
	    this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.GOLDEN_HOE), guiLeft + 142, guiTop + 17);
	    this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.STICK), guiLeft + 178, guiTop + 17);
	    this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Blocks.HOPPER), guiLeft + 142, guiTop + 53);
	    this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.LAVA_BUCKET), guiLeft + 178, guiTop + 53);
	    this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.REEDS), guiLeft + 142, guiTop + 35);
	    this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Blocks.SAPLING), guiLeft + 178, guiTop + 35);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.rfBarHeight = (int)(52*(this.th.power.getEnergyStored()/(float)this.th.power.getMaxEnergyStored()));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		String s = this.th.getName();
	    this.fontRendererObj.drawString(s, 113 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
	    
	    if ((mouseX > guiLeft+7) && (mouseX < guiLeft+16) && (mouseY > guiTop+17) && (mouseY < guiTop+69)) {
	    	
	    	String text = ("RF: " + this.th.power.getEnergyStored() + "/" + this.th.power.getMaxEnergyStored() + ", using " + this.th.rfCost + " RF per harvest");
		    List display = Arrays.asList(text);
	    	this.drawHoveringText(display, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if ((mouseX > guiLeft+160) && (mouseX < guiLeft+176) && (mouseY > guiTop+17) && (mouseY < guiTop+33)) {
	    	//Tooltip for increase radius
	    	String str = "Increase the radius of the harvester. Max 9, Currently: " + th.radius;
	    	this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if ((mouseX > guiLeft+160) && (mouseX < guiLeft+176) && (mouseY > guiTop+53) && (mouseY < guiTop+69)) {
	    	//Tooltip for decrease radius
	    	String str = "Decrease the radius of the harvester. Min 1, Currently: " + th.radius;
	    	this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if ((mouseX > guiLeft+142) && (mouseX < guiLeft+158) && (mouseY > guiTop+17) && (mouseY < guiTop+33)) {
	    	//Tooltip for logic mode
	    	String str = "Toggle harvesting of vanilla-like plants. Currently: " + (th.logic == true ? "Enabled" : "Disabled");
	    	this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if ((mouseX > guiLeft+178) && (mouseX < guiLeft+194) && (mouseY > guiTop+17) && (mouseY < guiTop+33)) {
	    	//Tooltip for agricraft mode
	    	String str = "Toggle harvesting of Agircraft plants. Currently: " + (th.rightClick == true ? "Enabled" : "Disabled");
	    	this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if ((mouseX > guiLeft+142) && (mouseX < guiLeft+158) && (mouseY > guiTop+53) && (mouseY < guiTop+69)) {
	    	//Tooltip for collect mode
	    	String str = "Toggle collecting dropped items. Currently: " + (th.collectItems == true ? "Enabled" : "Disabled");
	    	this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if ((mouseX > guiLeft+178) && (mouseX < guiLeft+194) && (mouseY > guiTop+53) && (mouseY < guiTop+69)) {
	    	//Tooltip for void mode
	    	String str = "Toggle voiding extra picked up items. Use with caution. Currently: " + (th.voidExtra == true ? "Enabled" : "Disabled");
	    	this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if ((mouseX > guiLeft+142) && (mouseX < guiLeft+158) && (mouseY > guiTop+35) && (mouseY < guiTop+51)) {
	    	//Tooltip for tall plants
	    	String str = "Toggle breaking tall plants, like sugarcane. Currently: " + (th.tallPlant == true ? "Enabled" : "Disabled");
	    	this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	    
	    if ((mouseX > guiLeft+178) && (mouseX < guiLeft+194) && (mouseY > guiTop+35) && (mouseY < guiTop+51)) {
	    	//tooltip for trees
	    	String str = "Toggle cutting down trees. Currently: " + (th.tree == true ? "Enabled" : "Disabled");
	    	this.drawHoveringText(Arrays.asList(str), mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
	    }
	}
}
