package curtis.cobbleworks.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import curtis.cobbleworks.ClientProxy;
import curtis.cobbleworks.CommonProxy;
import curtis.cobbleworks.manual.BookPage;
import curtis.cobbleworks.manual.ItemManual;
import curtis.cobbleworks.manual.TableOfContents;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiBook extends GuiContainer {

	ItemManual book;
	ResourceLocation texture;
	GuiButton index, foreward, back;
	int majorPage = 0, minorPage = 0;
	
	public GuiBook(ItemManual book) {
		super(new ContainerBook(book));
		
		this.xSize = 174;
		this.ySize = 181;
		this.book = book;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		texture = ClientProxy.manualRegistry.getTextureForPage(majorPage, minorPage);
		this.mc.getTextureManager().bindTexture(texture);
		
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.clear();
		
		index = new GuiButtonBook(0, guiLeft + 82, guiTop + 162, 20, 10, " << ", new ResourceLocation("cobbleworks", "textures/gui/guiBookButton.png"));
		foreward = new GuiButtonBook(2, guiLeft + 132, guiTop + 162, 20, 10, " -> ", new ResourceLocation("cobbleworks", "textures/gui/guiBookButton.png"));
		back = new GuiButtonBook(1, guiLeft + 32, guiTop + 162, 20, 10, " <- ", new ResourceLocation("cobbleworks", "textures/gui/guiBookButton.png"));
		
		this.buttonList.add(index);
		this.buttonList.add(foreward);
		this.buttonList.add(back);
		
		if (this.majorPage == 0) {
			int i = 3;
			for (BookPage page : CommonProxy.manualRegistry.getRegistry()) {
				if (!(page instanceof TableOfContents)) {
					this.buttonList.add(new GuiButtonBook(i, guiLeft + 32, guiTop + 10 * i - 10, 118, 10, CommonProxy.manualRegistry.getPageAt(i - 2).getButtonName(), new ResourceLocation("cobbleworks", "textures/gui/guiBookButtonGeneric.png")));
					i++;
				}
			}
		}
		
		this.updateButtons();
	}
	
	private void updateButtons() {
		
		if (this.majorPage != 0) {
			for (int i = 3; i < this.buttonList.size(); i++) {
				this.buttonList.get(i).visible = false;
			}
		} else if (majorPage == 0) {
			for (int i = 3; i < this.buttonList.size(); i++) {
				this.buttonList.get(i).visible = true;
			}
		}
		
		this.index.visible = majorPage != 0;
		this.foreward.visible = minorPage < CommonProxy.manualRegistry.getPageAt(majorPage).getMinorPages();
		this.back.visible = minorPage > 0;
	}
	
	//Button zero sends you to the index
	//Button one is go back a page
	//Button two is go foreward a page
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		switch (button.id) {
		case 0: {
			majorPage = 0;
			minorPage = 0;
			break;
			}
		case 1: {
				if (minorPage > 0) {
					minorPage--; break;
				}
			}
		case 2: {
				if (minorPage < CommonProxy.manualRegistry.getPageAt(majorPage).getMinorPages()) {
					minorPage++; break;
				}
			}
		default: majorPage = button.id - 2;
		}
		
		this.updateButtons();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		
		boolean recipePage = CommonProxy.manualRegistry.getPageAt(majorPage).isCraftingPage(minorPage);
		String display = CommonProxy.manualRegistry.getPageAt(majorPage).getDisplayText(minorPage);
		
		if (recipePage) {
			this.fontRenderer.drawSplitString(display, 33, 70, 118, 0);
			
			ItemStack[] stacks = CommonProxy.manualRegistry.getPageAt(majorPage).getRecipeRender(minorPage);
			this.itemRender.renderItemAndEffectIntoGUI(stacks[0], 52, 16);
			this.itemRender.renderItemAndEffectIntoGUI(stacks[1], 68, 16);
			this.itemRender.renderItemAndEffectIntoGUI(stacks[2], 84, 16);
			this.itemRender.renderItemAndEffectIntoGUI(stacks[3], 52, 32);
			this.itemRender.renderItemAndEffectIntoGUI(stacks[4], 68, 32);
			this.itemRender.renderItemAndEffectIntoGUI(stacks[5], 84, 32);
			this.itemRender.renderItemAndEffectIntoGUI(stacks[6], 52, 48);
			this.itemRender.renderItemAndEffectIntoGUI(stacks[7], 68, 48);
			this.itemRender.renderItemAndEffectIntoGUI(stacks[8], 84, 48);
			this.itemRender.renderItemAndEffectIntoGUI(stacks[9], 116, 32);
			this.itemRender.renderItemOverlayIntoGUI(fontRenderer, stacks[9], 116, 32, null);
			
			this.fontRenderer.drawSplitString(CommonProxy.manualRegistry.getPageAt(majorPage).getTooltipText(minorPage), 33, 149, 118, 0);
		} else {
			this.fontRenderer.drawSplitString(display, 33, 16, 118, 0);
		}
		
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}

}
