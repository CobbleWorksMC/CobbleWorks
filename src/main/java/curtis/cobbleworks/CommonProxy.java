package curtis.cobbleworks;

import java.io.File;

import curtis.cobbleworks.cobblegen.BlockAdvancedGen;
import curtis.cobbleworks.cobblegen.BlockCobbleGen;
import curtis.cobbleworks.cobblegen.BlockCustomgen;
import curtis.cobbleworks.cobblegen.CobbleUpgrade;
import curtis.cobbleworks.cobblegen.TileEntityAdvancedgen;
import curtis.cobbleworks.cobblegen.TileEntityCobblegen;
import curtis.cobbleworks.cobblegen.TileEntityCustomgen;
import curtis.cobbleworks.gui.GuiProxy;
import curtis.cobbleworks.gui.PacketSync;
import curtis.cobbleworks.manual.ItemManual;
import curtis.cobbleworks.manual.PageRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	private static int packetID = 0;
	
	private static boolean flagCCW = false; //Tracks if the Custom Cobbleworks properly registered all its items
	
	public static Configuration config;
	public static Configuration config2;
	
	public static SimpleNetworkWrapper packetHandler = null;
	
	//The Manual
	@GameRegistry.ObjectHolder(Cobbleworks.MODID + ":guideBook")
	public static ItemManual modManual;
	public static PageRegistry manualRegistry;
	
	//Stuff for cobble gen
	@GameRegistry.ObjectHolder(Cobbleworks.MODID + ":cobblegen")
	public static BlockCobbleGen cobblegen;
	@GameRegistry.ObjectHolder(Cobbleworks.MODID + ":advgen")
	public static BlockAdvancedGen advgen;
	@GameRegistry.ObjectHolder(Cobbleworks.MODID + ":customgen")
	public static BlockCustomgen customgen;
	@GameRegistry.ObjectHolder(Cobbleworks.MODID + ":cobbleUpgrade")
	public static CobbleUpgrade up1;
	
	public void preInit(FMLPreInitializationEvent e) {
		
		packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(Cobbleworks.MODID);
		
		File cd = new File(e.getModConfigurationDirectory(), Cobbleworks.DOMAIN);
		config = new Configuration(new File(cd, "Cobbleworks.cfg"));
		config2 = new Configuration(new File(cd, "Custom_Cobblegen.cfg"));
		Config.load();
		
		modManual = new ItemManual();
		
		if (TileEntityCustomgen.isActive()) {
			flagCCW = true;
		}
		
		if (Config.enableCobblegen) {
			cobblegen = new BlockCobbleGen();
		}
		
		if (Config.enableAdvancedgen) {
			advgen = new BlockAdvancedGen();
		}
		
		if (Config.enableCustomgen && flagCCW) {
			customgen = new BlockCustomgen();
		}
		
		up1 = new CobbleUpgrade();
		
		packetHandler.registerMessage(PacketSync.PacketSyncHandler.class, PacketSync.class, packetID++, Side.SERVER);
	}
	
	public void init(FMLInitializationEvent e) {
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Cobbleworks.instance, new GuiProxy());
		
		//Recipes
		//GameRegistry.addShapedRecipe(name, group, output, params);
	}

	public void postInit(FMLPostInitializationEvent e) {
		
		if (config.hasChanged()) {
			config.save();
		}
		
		if (config2.hasChanged()) {
			config2.save();
		}
		
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
	
		System.out.println("Registering blocks...");
		if (Config.enableCobblegen) {
			event.getRegistry().register(cobblegen);
		}
		
		if (Config.enableAdvancedgen) {
			event.getRegistry().register(advgen);
		}
		
		if (Config.enableCustomgen && flagCCW) {
			event.getRegistry().register(customgen);
		}
		
		registerTiles();
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		System.out.println("Registering items...");
		event.getRegistry().register(modManual);
		
		System.out.println("Registering itemblocks...");
		if (Config.enableCobblegen) {
			event.getRegistry().register(new ItemBlock(cobblegen).setRegistryName(cobblegen.getRegistryName()));
		}
		
		if (Config.enableAdvancedgen) {
			event.getRegistry().register(new ItemBlock(advgen).setRegistryName(advgen.getRegistryName()));
		}
		
		if (Config.enableCustomgen && flagCCW) {
			event.getRegistry().register(new ItemBlock(customgen).setRegistryName(customgen.getRegistryName()));
		}
		
		event.getRegistry().register(up1);
	}
	
	public static void registerTiles() {
	
		GameRegistry.registerTileEntity(TileEntityCobblegen.class, Cobbleworks.MODID + "_cobblegen");
		GameRegistry.registerTileEntity(TileEntityAdvancedgen.class, Cobbleworks.MODID + "_advgen");
		GameRegistry.registerTileEntity(TileEntityCustomgen.class, Cobbleworks.MODID + "_customGen");
	}
	
	public static CreativeTabs tabcobbleworks = new CreativeTabs(Cobbleworks.MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.COBBLESTONE);
		}
	};
}
