package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.book.BookPage;

public class TableOfContents extends BookPage {

	public TableOfContents() {
		super(0, new int[] {0});
	}
	
	@Override
	public String getDisplayText(int minor) {
		return "";
	}
	
	@Override
	public String getTooltipText(int minor) {
		return "";
	}
}