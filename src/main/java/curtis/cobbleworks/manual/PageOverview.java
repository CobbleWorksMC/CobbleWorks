package curtis.cobbleworks.manual;

public class PageOverview extends BookPage {

	public PageOverview() {
		super(2, new int[] {0, 0, 0});
	}
	
	public String getButtonName() {
		return "Mod Overview";
	}
	
	public String getDisplayText(int minor) {
		String result = "";
		
		switch (minor) {
		case 0: { result = "Hello! You have likely installed this mod because you, like the developers, are tired of gathering basic materials. Well this modd aims to help, with devices like the titular Cobbleworks block."; break; }
		case 1: { result = "In ages past (1.10.2), this mod shipped with lots of auxilliary content that had nothing to do with cobblestone generation. That content will be ported in the future to a different mod, as it does not fit the theme of this one."; break; }
		case 2: { result = "You can disable the generators you do not want. The configuration files contains options to change any features you would like to. Note that disabling a feature will remove it from the game entirely, not just the way to get it, so it is best to configure first, then make a world."; break; }
		}
		
		return result;
	}
}
