package curtis.Cobbleworks;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {
	
	public static boolean enableCobblegen = true;
	public static boolean enableTools = true;
	public static boolean enableMagic = true;
	public static int maxRecursiveIterations = 16;
	public static String renderMantasAs = "STEVE";
	public static boolean enableSpawner = true;
	public static int spawnerDelay = 600;
	public static String animals = "save";
	public static boolean enableFarm = true;
	public static int soilGrowChance = 3;
	public static boolean enableArmor = true;
	public static int dodgeChanceHat = 10;
	public static int dodgeChanceChest = 20;
	public static int dodgeChanceLegs = 15;
	public static int dodgeChanceBoots = 10;
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
			enableTools = cfg.getBoolean("enableTools", "general", enableTools, "Set this to false to disable the overpowerd tools this mod adds.");
			maxRecursiveIterations = cfg.getInt("maxRecursiveIterations", "general", 16, 1, 255, "This will essentially limit the radius of overpowered tools.");
			renderMantasAs = cfg.getString("renderMantasAs", "general", "STEVE", "Choose what model to give to Manta Style illusions. Can be set to STEVE or ALEX.", new String[]{"STEVE", "ALEX"});
			enableMagic = cfg.getBoolean("enableMagic", "general", true, "Set this to false to disable magical weaponry");
			enableSpawner = cfg.getBoolean("enableSpawner", "general", true, "Set this to false to disable the YuGiOh themed mob spawning system");
			spawnerDelay = cfg.getInt("spawnerDelay", "general", 600, 1, Integer.MAX_VALUE, "If the spawner is enabled, set the delay between spawns here. You can set it to be rediculous if you want.");
			animals = cfg.getString("gamesDoneQuickJoke", "general", "save", "Do you want to save or kill the animals?", new String[] {"kill", "save"});
			enableFarm = cfg.getBoolean("enableFarm", "general", true, "Set this to false to disable the harvester block.");
			soilGrowChance = cfg.getInt("soilGrowChance", "general", 3, 0, Integer.MAX_VALUE, "When the Hydroponic Soil block recieves a random update tick, it will have a one in this value chance to pass it on to the plant above it. Zero disables this feature. \nSimply put, the soil will auto bonemeal plants occasionally, the bigger this number the less often.");
			enableArmor = cfg.getBoolean("enableArmor", "general", true, "Set this to false to disable the PAYDAY themed armor.");
			dodgeChanceHat = cfg.getInt("helmDodge", "general", 10, 0, 100, "If enabled, adjust the dodge chance added by the suit hat.");
			dodgeChanceChest = cfg.getInt("suitDodge", "general", 20, 0, 100, "If enabled, adjust the dodge chance added by the suit chestpiece.");
			dodgeChanceLegs = cfg.getInt("legsDodge", "general", 15, 0, 100, "If enabled, adjust the dodge chance added by the suit leggings.");
			dodgeChanceBoots = cfg.getInt("bootDodge", "general", 10, 0, 100, "If enabled, adjust the dodge chance added by the suit boots.");
			
			cfg2.load();
			cfg2.addCustomCategoryComment("general", "General_Configuration");
			customItems = cfg2.getStringList("customItems", "general", new String[] {""}, "Use this to change the items produced by the Custom Cobbleworks. \nUse the format modid:itemname. For example, minecraft:cobblestone gives you cobblestone. \nIf an item is invalid, the custom cobbleworks will not be loaded when the pack loads.");
			customTiers = cfg2.get("general", "customTiers", new int[] {0, 1, 1, 1, 2, 2, 2, 3, 3}, "Use this to change the tiers the Custom Cobbleworks produces items at.").getIntList();
			customPower = cfg2.get("general", "customPower", new int[] {0, 100, 200, 300, 400, 500}, "Set the power consumed at each tier by the Custom Cobbleworks.").getIntList();
			customLava = cfg2.get("general", "customLava", new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0}, "Use this to set the lava consumed per tick, per item or block created, in micro-buckets (a value of 1000 will be 1mb/t per item).").getIntList();
			
		} catch (Exception donacdum) {
			
		} finally {
			
		}
		
	}
	
	public static void initConfig(Configuration config) {
		
	}
	
	public void postInit(FMLPostInitializationEvent e) {
		
	}
	
}
