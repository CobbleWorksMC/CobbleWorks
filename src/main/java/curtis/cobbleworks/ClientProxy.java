package curtis.cobbleworks;

import curtis.cobbleworks.manual.PageRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		
		modManual.initModel();
		manualRegistry = new PageRegistry();
		up1.initModel();
	}
	
	public void Init(FMLInitializationEvent e) {
		super.init(e);
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent e) {
		if (Config.enableCobblegen) {
			cobblegen.initModel();
		}
		
		if (Config.enableAdvancedgen) {
			advgen.initModel();
		}
		
		if (Config.enableCustomgen) {
			customgen.initModel();
		}
	}
	
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
		
		manualRegistry.launchPages();
	}
}
