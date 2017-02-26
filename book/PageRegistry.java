package curtis.Cobbleworks.module.book;

import java.util.ArrayList;
import java.util.List;

import curtis.Cobbleworks.Config;
import curtis.Cobbleworks.module.book.pages.PageAdvancedCobbleworks;
import curtis.Cobbleworks.module.book.pages.PageAncientStaff;
import curtis.Cobbleworks.module.book.pages.PageBasicCobbleworks;
import curtis.Cobbleworks.module.book.pages.PageCobbleUpgrades;
import curtis.Cobbleworks.module.book.pages.PageLightWand;
import curtis.Cobbleworks.module.book.pages.PageMantaStyle;
import curtis.Cobbleworks.module.book.pages.PageSpawner;
import curtis.Cobbleworks.module.book.pages.TableOfContents;
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
		if (Config.enableCobblegen) {
			PageBasicCobbleworks PBC = new PageBasicCobbleworks();
			PageAdvancedCobbleworks PAC = new PageAdvancedCobbleworks();
			PageCobbleUpgrades PCU = new PageCobbleUpgrades();
		}
		if (Config.enableTools) {
			PageMantaStyle PMS = new PageMantaStyle(); //Not poor man's shield :(
			PageLightWand PLW = new PageLightWand();
		}
		if (Config.enableMagic) {
			PageAncientStaff PAS = new PageAncientStaff();
		}
		if (Config.enableSpawner) {
			PageSpawner PS = new PageSpawner();
		}
	}
	
}
