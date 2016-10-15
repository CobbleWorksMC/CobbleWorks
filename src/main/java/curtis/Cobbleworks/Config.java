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
	
	public static void load() {
		
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			
			cfg.addCustomCategoryComment("general", "General_configuration");
			enableCobblegen = cfg.getBoolean("enableCobblegen", "general", enableCobblegen, "Set this to false to disable the Cobbleworks block.");
			enableTools = cfg.getBoolean("enableTools", "general", enableTools, "Set this to false to disable the overpowerd tools this mod adds.");
			maxRecursiveIterations = cfg.getInt("maxRecursiveIterations", "general", 16, 1, 255, "This will essentially limit the radius of overpowered tools.");
			renderMantasAs = cfg.getString("renderMantasAs", "general", "STEVE", "Choose what model to give to Manta Style illusions. Can be set to STEVE or ALEX.", new String[]{"STEVE", "ALEX"});
			enableMagic = cfg.getBoolean("enableMagic", "general", true, "Set this to false to disable magical weaponry");
			
		} catch (Exception donacdum) {
			
		} finally {
			
		}
		
	}
	
	public static void initConfig(Configuration config) {
		
	}
	
	public void postInit(FMLPostInitializationEvent e) {
		
	}
	
}
