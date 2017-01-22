package curtis.Cobbleworks;

import curtis.Cobbleworks.module.cobblegen.BlockCobbleGen;
import curtis.Cobbleworks.module.magic.EntityIceBarrage;
import curtis.Cobbleworks.module.magic.RenderIceBarrage;
import curtis.Cobbleworks.module.tool.EntityMantaIllusion;
import curtis.Cobbleworks.module.tool.RenderMantaIllusion;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		
		OBJLoader.INSTANCE.addDomain(Cobbleworks.MODID);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityIceBarrage.class, new IRenderFactory<EntityIceBarrage>() {
			@Override
			public Render<? super EntityIceBarrage> createRenderFor(RenderManager rm) {
				return new RenderIceBarrage(rm);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityMantaIllusion.class, new IRenderFactory<EntityMantaIllusion>() {
			@Override
			public Render<? super EntityMantaIllusion> createRenderFor(RenderManager rm) {
				return new RenderMantaIllusion(rm);
			}
		});
		
		if (Config.enableCobblegen) {
			cobblegen.initModel();
			advgen.initModel();
			up1.initModel();
		}
		
		if (Config.enableTools) {
			manta.initModel();
			//pf.initModel();
		}
		
		if (Config.enableMagic) {
			ancientStaff.initModel();
		}
		
		if (Config.enableSpawner) {
			PendulumMonster.initModel();
			summoner.initModel();
			booster.initModel();
			timeScale.initModel();
			spaceScale.initModel();
		}
	}
	
	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}
	
}
