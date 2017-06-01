package curtis.Cobbleworks.module.book.pages;

import curtis.Cobbleworks.module.book.BookPage;

public class PageAncientStaff extends BookPage {
	
	private static final int[] pageTypes = new int[] {0, 0};
	
	public PageAncientStaff() {
		super(1, pageTypes);
	}
	
	public String getButtonName() {
		return "Ancient Staff";
	}
	
	public String getDisplayText(int minor) {
		switch (minor) {
		case 0: return "A magical trinket passed down by a race of powerful wizards, the Ancient Staff is a weapon capable of stopping your enemies cold. When activated while looking at an entity, you will freeze and damage them and everyone near them." + '\n' + "Only found in chests in desert temples.";
		case 1: return "Damage: 1.25 to 5 hearts, plus 50 percent for enemies either on fire (will extinguish them) or immune to fire." + '\n' + "60 tick cooldown between casts, 36 tick delay between cast and damage application (freeze applies on cast).";
		}
		return "";
	}
}
