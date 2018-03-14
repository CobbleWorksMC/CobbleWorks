package curtis.cobbleworks;

import java.util.Random;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Cobbleworks.MODID, name = Cobbleworks.MOD_NAME, version = Cobbleworks.VERSION, acceptedMinecraftVersions = "[1.12,1.13)")
public class Cobbleworks {
	
	public static final String MODID = "cobbleworks";
	public static final String DOMAIN = "cobbleworks";
	public static final String MOD_NAME = "Cobbleworks";
    public static final String VERSION = "2.5.0";
    
    public static Logger logger;
    
    @Instance(Cobbleworks.MODID)
    public static Cobbleworks instance;
    
    @SidedProxy(clientSide = "curtis.cobbleworks.ClientProxy", serverSide = "curtis.cobbleworks.CommonProxy")
    public static CommonProxy proxy;
    
    public static final Random rand = new Random();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	logger = e.getModLog();
    	proxy.preInit(e);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent e) {
    	proxy.init(e);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	proxy.postInit(e);
    }
	
}
