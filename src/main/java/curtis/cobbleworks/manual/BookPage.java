package curtis.cobbleworks.manual;

import curtis.cobbleworks.CommonProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BookPage {
	
	private int sizeX, sizeY, minorPage;
	public static final ResourceLocation textureGeneric = new ResourceLocation("cobbleworks", "textures/gui/guiBookGeneric.png");
	public static final ResourceLocation textureCrafting = new ResourceLocation("cobbleworks", "textures/gui/guiBookCrafting.png");
	private int[] pageTypes;
	
	public BookPage(int minor, int[] types) {
		
		minorPage = minor;
		pageTypes = types;
		CommonProxy.manualRegistry.registerPage(this);
	}
	
	//0 = generic page, 1 = page with crafting display
	public void setTexure(int index, int type) {
		this.pageTypes[index] = type;
	}
	
	public boolean isCraftingPage(int minor) {
		return pageTypes[minor] == 1;
	}
	
	public ResourceLocation getTexture(int minorPage) {
		
		if (minorPage >= 0 && minorPage <= this.minorPage) {
			return pageTypes[minorPage] == 0 ? textureGeneric : textureCrafting;
		}
		
		return textureGeneric;
	}
	
	public String getButtonName() {
		return "Default Name";
	}
	
	public String getDisplayText(int minor) {
		return "Default text";
	}
	
	public String getTooltipText(int minor) {
		return "Default tooltip";
	}
	
	public int getMinorPages() {
		return minorPage;
	}
	
	//Override this for recipe pages, 0 through 8 are considered the recipe input slots, 9 is the output.
	//in the order:
	// 0 1 2
	// 3 4 5 -> 9
	// 6 7 8
	//This will be called for all recipe pages, and will not be called otherwise.
	public ItemStack[] getRecipeRender(int minor) {
		return null;
	}
}

