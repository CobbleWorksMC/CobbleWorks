package curtis.Cobbleworks;

import java.util.Random;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Cobbleworks.MODID, name = Cobbleworks.MOD_NAME, version = Cobbleworks.VERSION)
public class Cobbleworks {
	
	public static final String MODID = "cobbleworks";
	public static final String DOMAIN = "cobbleworks";
	public static final String MOD_NAME = "Cobbleworks";
    public static final String VERSION = "1.0";
    
    @Mod.Instance
    public static Cobbleworks instance;
    
    @SidedProxy(clientSide = "curtis.Cobbleworks.ClientProxy", serverSide = "curtis.Cobbleworks.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
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
