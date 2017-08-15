package curtis.cobbleworks.manual;

import java.util.ArrayList;
import java.util.List;

import curtis.cobbleworks.Config;
import net.minecraft.util.ResourceLocation;

public class PageRegistry {

private List<BookPage> pages;
	
	public PageRegistry() {
		pages = new ArrayList<BookPage>();
	}
	
	public List<BookPage> getRegistry() {
		return pages;
	}
	
	public void registerPages(BookPage... pagesToAdd) {
		
		for (BookPage page : pagesToAdd) {
			pages.add(page);
		}
	}
	
	public void registerPage(BookPage pageToAdd) {
		pages.add(pageToAdd);
		//System.out.println("added a page to the manual: " + pageToAdd.getButtonName());
	}

	public ResourceLocation getTextureForPage(int majorPage, int minorPage) {
		return pages.get(majorPage).getTexture(minorPage);
	}
	
	public BookPage getPageAt(int index) {
		return pages.get(index);
	}
	
	public void launchPages() {
		
		TableOfContents ToC = new TableOfContents();
		
		PageOverview POV = new PageOverview();
		
		if (Config.enableCobblegen) {
			PageBasicCobbleworks PBC = new PageBasicCobbleworks();
		}
		
		if (Config.enableAdvancedgen) {
			PageAdvancedCobbleworks PAC = new PageAdvancedCobbleworks();
		}
		
		if (Config.enableCustomgen) {
			PageCustomCobblegen PCC = new PageCustomCobblegen();
		}
		
		PageCobbleUpgrades PCU = new PageCobbleUpgrades();
	}
}
