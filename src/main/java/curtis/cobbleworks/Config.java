package curtis.cobbleworks;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static boolean enableCobblegen = true;
	public static boolean enableAdvancedgen = true;
	public static boolean enableCustomgen = true;
	public static boolean forceCustomgen = false;
	
	public static int basicPowerCapacity = 250000;
	public static int advPowerCapacity = 500000;
	public static int customPowerCapacity = 1000000;
	
	public static int advLavaCapacity = 16000;
	public static int customLavaCapacity = 16000;
	
	public static String[] customItems = new String[] {""};
	public static int[] customTiers = new int[] {0, 1, 1, 1, 2, 2, 2, 3, 3};
	public static int[] customPower = new int[] {0, 100, 200, 300, 400, 500};
	public static int[] customLava = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
	
	public static void load() {
		
		Configuration cfg = CommonProxy.config;
		Configuration cfg2 = CommonProxy.config2;
		
		try {
			cfg.load();
			
			cfg.addCustomCategoryComment("general", "General_configuration");
			enableCobblegen = cfg.getBoolean("enableCobblegen", "general", enableCobblegen, "Set this to false to disable the Cobbleworks block. :(.");
			enableAdvancedgen = cfg.getBoolean("enableAdvancedgen", "general", enableAdvancedgen, "Set this to false to disable the Advanced Cobbleworks block.");
			enableCustomgen = cfg.getBoolean("enableCustomgen", "general", enableCustomgen, "Set this to false to disable the Custom Cobbleworks block.");
			forceCustomgen = cfg.getBoolean("forceCustomgen", "general", forceCustomgen, "Set this to true to force the Custom Cobblegen to register as a block, even if the configuration for it is invalid.\nThis is only here for debug purposes, and is likely to cause more crashes than fixes.\nYou should probably leave it false.");
			
			basicPowerCapacity = cfg.getInt("basicPowerCapacity", "general", basicPowerCapacity, 10000, Integer.MAX_VALUE, "Adjust the maximum amount of RF stored in the Basic Cobbleworks.");
			advPowerCapacity = cfg.getInt("advPowerCapacity", "general", advPowerCapacity, 20000, Integer.MAX_VALUE, "Adjust the maximum amount of RF stored in the Advanced Cobbleworks.");
			customPowerCapacity = cfg.getInt("customPowerCapacity", "general", customPowerCapacity, 1, Integer.MAX_VALUE, "Adjust the maximum amount of RF stored in the Custom Cobbleworks.");
			
			advLavaCapacity = cfg.getInt("advLavaCapacity", "general", advLavaCapacity, 1000, Integer.MAX_VALUE, "Adjust the amount of Lava stored in the Advanced Cobbleworks.");
			customLavaCapacity = cfg.getInt("customLavaCapacity", "general", customLavaCapacity, 1000, Integer.MAX_VALUE, "Adjust the amount of Lava stored in the Custom Cobbleworks.");
			
			if (enableCustomgen) {
				cfg2.load();
				cfg2.addCustomCategoryComment("general", "General_Configuration");
				customItems = cfg2.getStringList("customItems", "general", new String[] {"minecraft:cobblestone", "minecraft:stone", "minecraft:stone,1", "minecraft:stone,2", "minecraft:stone,3", "minecraft:stone,4", "minecraft:stone,5", "minecraft:stone,6", "minecraft:cobblestone"}, "Use this to change the items produced by the Custom Cobbleworks. \nUse the format modid:itemname,metadata. For example, minecraft:cobblestone gives you cobblestone, or minecraft:stone,2 gives you polished granite.\nNBT tags are not yet implemented.\nIf an item is invalid, the custom cobbleworks will not have a recipe, so be careful if using an existing one. \nThere needs to be nine items.");
				customTiers = cfg2.get("general", "customTiers", new int[] {0, 1, 1, 1, 2, 2, 2, 3, 3}, "Use this to change the tiers the Custom Cobbleworks produces items at. \nThere needs to be nine values.").getIntList();
				customPower = cfg2.get("general", "customPower", new int[] {0, 100, 200, 300, 400, 500}, "Set the power consumed at each tier by the Custom Cobbleworks. \nThere needs to be six values.").getIntList();
				customLava = cfg2.get("general", "customLava", new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0}, "Use this to set the lava consumed per tick, per item or block created, in micro-buckets (a value of 1000 will be 1mb/t per item). \nThere needs to be nine values").getIntList();
			}
			
		} catch (Exception donacdum) {
			
		} finally {
			
		}
		
	}
}
