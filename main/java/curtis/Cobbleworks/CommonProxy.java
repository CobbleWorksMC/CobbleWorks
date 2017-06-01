package curtis.Cobbleworks;

import java.io.File;

import com.infinityraider.agricraft.apiimpl.SoilRegistry;

import curtis.Cobbleworks.module.armor.ArmorPayday;
import curtis.Cobbleworks.module.book.ItemBook;
import curtis.Cobbleworks.module.book.PageRegistry;
import curtis.Cobbleworks.module.cobblegen.BlockAdvancedGen;
import curtis.Cobbleworks.module.cobblegen.BlockCobbleGen;
import curtis.Cobbleworks.module.cobblegen.BlockCustomCobblegen;
import curtis.Cobbleworks.module.cobblegen.GuiProxy;
import curtis.Cobbleworks.module.cobblegen.TileEntityCustomGen;
import curtis.Cobbleworks.module.cobblegen.upgrades.CobbleUpgrade;
import curtis.Cobbleworks.module.farming.BlockHarvester;
import curtis.Cobbleworks.module.farming.BlockHydroponicSoil;
import curtis.Cobbleworks.module.farming.PacketHarvesterSync;
import curtis.Cobbleworks.module.magic.AncientStaff;
import curtis.Cobbleworks.module.magic.EntityIceBarrage;
import curtis.Cobbleworks.module.magic.RenderIceBarrage;
import curtis.Cobbleworks.module.spawner.BlockPendulumSummoner;
import curtis.Cobbleworks.module.spawner.BoosterPack;
import curtis.Cobbleworks.module.spawner.ItemMobCard;
import curtis.Cobbleworks.module.spawner.MobRegistry;
import curtis.Cobbleworks.module.spawner.SpaceScale;
import curtis.Cobbleworks.module.spawner.TimeScale;
import curtis.Cobbleworks.module.tool.EntityMantaIllusion;
import curtis.Cobbleworks.module.tool.LightSource;
import curtis.Cobbleworks.module.tool.LightWand;
import curtis.Cobbleworks.module.tool.RenderMantaIllusion;
import curtis.Cobbleworks.module.tool.SpeedBoots;
import curtis.Cobbleworks.module.tool.StormShield;
import curtis.Cobbleworks.module.tool.SuperAxe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage.ModList;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CommonProxy {
	
	private static int packetID = 0;
	
	public static Configuration config;
	public static Configuration config2;
	
	public static SimpleNetworkWrapper packetHandler = null;
	
	public static CreativeTabs tabcobbleworks = new CreativeTabs(Cobbleworks.MODID) {
		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.COBBLESTONE);
		}
	};
	
	public static final ToolMaterial materialStar = EnumHelper.addToolMaterial(Cobbleworks.MODID +".materialStar", 3, 9000, 12.0F, 0.5F, 1);
	
	//The Manual
	public static ItemBook modManual;
	public static PageRegistry manualRegistry;
	
	//Stuff for cobble gen
	public static BlockCobbleGen cobblegen;
	public static BlockAdvancedGen advgen;
	public static BlockCustomCobblegen customGen;
	public static CobbleUpgrade up1;
	
	//Stuff for tools
	public static SoundEvent mantaSuccess;
	public static SoundEvent cooldown;
	public static SoundEvent illusionDeath;
	public static SoundEvent iceBarrageCast;
	public static SoundEvent iceBarrageImpact;
	
	//Tools
	public static SuperAxe manta;
	public static AncientStaff ancientStaff;
	public static LightWand lightWand;
	public static LightSource lightSource;
	public static SpeedBoots speedBoosters;
	public static StormShield stormShield;
	
	//Spawner
	public static ItemMobCard PendulumMonster;
	public static MobRegistry pendulumRegistry;
	public static BlockPendulumSummoner summoner;
	public static BoosterPack booster;
	public static TimeScale timeScale;
	public static SpaceScale spaceScale;
	public static LootHandler lootations;
	
	//Armor
	public static ArmorMaterial payday;
	public static ArmorPayday suitHat;
	public static ArmorPayday suitChest;
	public static ArmorPayday suitLegs;
	public static ArmorPayday suitBoots;
	
	//Farm
	public static BlockHarvester harvester;
	public static BlockHydroponicSoil farmland;
	
	public void preInit(FMLPreInitializationEvent e) {
		
		packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(Cobbleworks.MODID);
		
		File cd = new File(e.getModConfigurationDirectory(), Cobbleworks.DOMAIN);
		config = new Configuration(new File(cd, "Cobbleworks.cfg"));
		config2 = new Configuration(new File(cd, "Custom_Cobblegen.cfg"));
		Config.load();
		
		modManual = new ItemBook();
		
		if (Config.enableCobblegen) {
			cobblegen = new BlockCobbleGen();
			advgen = new BlockAdvancedGen();
			customGen = new BlockCustomCobblegen();
			up1 = new CobbleUpgrade();
		}
		
		if (Config.enableTools) {
			manta = new SuperAxe();
			EntityRegistry.registerModEntity(EntityMantaIllusion.class, "Illusion", 0, Cobbleworks.instance, 32, 1, true);
			lightWand = new LightWand();
			lightSource = new LightSource();
			speedBoosters = new SpeedBoots();
			stormShield = new StormShield();
		}
		
		if (Config.enableMagic) {
			ancientStaff = new AncientStaff();
			EntityRegistry.registerModEntity(EntityIceBarrage.class, "iceBarrage", 1, Cobbleworks.instance, 32, 1, true);
		}
		
		if (Config.enableSpawner) {
			summoner = new BlockPendulumSummoner();
			PendulumMonster = new ItemMobCard();
			booster = new BoosterPack();
			timeScale = new TimeScale();
			spaceScale = new SpaceScale();
			pendulumRegistry = new MobRegistry();
		}
		
		if (Config.enableArmor) {
			payday = EnumHelper.addArmorMaterial("PAYDAY", "cobbleworks:PAYDAY", 3, new int[] {0,0,0,0}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f);
			suitHat = new ArmorPayday(EntityEquipmentSlot.HEAD, 1, Config.dodgeChanceHat);
			suitChest = new ArmorPayday(EntityEquipmentSlot.CHEST, 1, Config.dodgeChanceChest);
			suitLegs = new ArmorPayday(EntityEquipmentSlot.LEGS, 2, Config.dodgeChanceLegs);
			suitBoots = new ArmorPayday(EntityEquipmentSlot.FEET, 1, Config.dodgeChanceBoots);
		}
		
		if (Config.enableFarm) {
			harvester = new BlockHarvester();
			farmland = new BlockHydroponicSoil();
		}
		
		mantaSuccess = registerSound("MantaStyle");
		cooldown = registerSound("cooldown");
		illusionDeath = registerSound("illusionDeath");
		iceBarrageCast = registerSound("iceBarrageCast");
		iceBarrageImpact = registerSound("iceBarrageImpact");
	}
	
	public void init(FMLInitializationEvent e) {
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Cobbleworks.instance, new GuiProxy());
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(modManual), new Object[] {"CCC", "SBS", "CCC", 'C', Blocks.COBBLESTONE, 'S', Blocks.STONE, 'B', Items.BOOK}));
		
		if (Config.enableCobblegen) {
			GameRegistry.addRecipe(new ShapedOreRecipe(cobblegen, new Object[] {"III", "LDW", "III", 'I', "ingotIron", 'L', Items.LAVA_BUCKET, 'D', Items.DIAMOND_PICKAXE, 'W', Items.WATER_BUCKET}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(up1, 1, 0), new Object[] {"IGI", "FCP", "IGI", 'I', "ingotIron", 'G', "ingotGold", 'F', Blocks.FURNACE, 'C', Blocks.COAL_BLOCK, 'P', Blocks.PISTON}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(up1, 1, 1), new Object[] {"GDG", "TUT", "GDG", 'G', "ingotGold", 'D', "gemDiamond", 'T', Blocks.CRAFTING_TABLE, 'U', new ItemStack(up1, 1, 0)}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(up1, 1, 2), new Object[] {"CDC", "NUN", "CDC", 'C', Items.MAGMA_CREAM, 'D', "gemDiamond", 'N', Blocks.NETHER_BRICK, 'U', new ItemStack(up1, 1, 1)}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(up1, 1, 3), new Object[] {"QBQ", "LUL", "QBQ", 'Q', Blocks.QUARTZ_BLOCK, 'B', Items.BLAZE_ROD, 'L', Items.LAVA_BUCKET, 'U', new ItemStack(up1, 1, 2)}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(up1, 1, 4), new Object[] {"DSD", "TUT", "GGG", 'D', "gemDiamond", 'S', Items.NETHER_STAR, 'T', Items.GHAST_TEAR, 'G', "ingotGold", 'U', new ItemStack(up1, 1, 3)}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(advgen), new Object[] {"GSG", "BDB", "GQG", 'G', "ingotGold", 'S', Items.NETHER_STAR, 'B', Items.WATER_BUCKET, 'D', Items.DIAMOND_PICKAXE, 'Q', Blocks.QUARTZ_BLOCK}));
			if (TileEntityCustomGen.isActive()) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(customGen), new Object[] {" q ", " g ", " r ", 'q', cobblegen, 'g', advgen, 'r', Items.NETHER_STAR}));
			}
		}
		
		if (Config.enableTools) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(manta), new Object[] {"ss ","sd "," i ", 's', Items.NETHER_STAR, 'd', Items.DIAMOND_AXE, 'i', Items.GOLDEN_SWORD}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(lightWand), new Object[] {"GBG", "GRG", "GRG", 'G', Items.GLOWSTONE_DUST, 'B', Blocks.GLOWSTONE, 'R', Items.BLAZE_ROD}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(speedBoosters), new Object[] {"OWO", "FBF", "FSF", 'B', Items.DIAMOND_BOOTS, 'O', Blocks.OBSIDIAN, 'F', Items.FIREWORKS, 'S', Blocks.SLIME_BLOCK, 'W', Blocks.WOOL}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stormShield), new Object[] {"GSG", "ODO", "OOO", 'G', "ingotGold", 'S', Items.SHIELD, 'D', "gemDiamond", 'O', Blocks.OBSIDIAN}));
			MinecraftForge.EVENT_BUS.register(speedBoosters);
			MinecraftForge.EVENT_BUS.register(stormShield);
		}
		
		if (Config.enableSpawner) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(summoner), new Object[] {"OCO", "SRT", "OKO", 'O', Blocks.OBSIDIAN, 'K', Items.END_CRYSTAL, 'S', spaceScale, 'R', Items.NETHER_STAR, 'T', timeScale, 'C', Items.CLOCK}));
			
			lootations = new LootHandler();
			MinecraftForge.EVENT_BUS.register(lootations);
		}
		
		if (Config.enableArmor) {
			MinecraftForge.EVENT_BUS.register(suitHat);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(suitHat), new Object[] {"WLW", "WBW", 'W', Blocks.WOOL, 'B', "dyeBlack", 'L', Items.LEATHER}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(suitChest), new Object[] {"WBW", "WLW", "WWW", 'W', Blocks.WOOL, 'B', "dyeBlack", 'L', Items.LEATHER}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(suitLegs), new Object[] {"WLW", "WBW", "W W", 'W', Blocks.WOOL, 'B', "dyeBlack", 'L', Items.LEATHER}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(suitBoots), new Object[] {"WBW", "WLW", 'W', Blocks.WOOL, 'B', "dyeBlack", 'L', Items.LEATHER}));
		}
		
		if (Config.enableFarm) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(harvester), new Object[] {"ADH", "PQL", "GCG", 'A', Items.GOLDEN_AXE, 'D', "gemDiamond", 'H', Items.GOLDEN_HOE, 'P', Blocks.HOPPER, 'Q', Blocks.QUARTZ_BLOCK, 'L', Blocks.REDSTONE_LAMP, 'G', Items.GOLD_INGOT, 'C', Blocks.CHEST}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(farmland, 8), new Object[] {"DDD", "DWD", "DDD", 'D', Blocks.DIRT, 'W', Items.WATER_BUCKET}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(farmland, 8), new Object[] {"DDD", "DWD", "DDD", 'D', Blocks.DIRT, 'W', Items.POTIONITEM}));
		}
		
		packetHandler.registerMessage(PacketSync.PacketSyncHandler.class, PacketSync.class, packetID++, Side.SERVER);
		packetHandler.registerMessage(PacketHarvesterSync.PacketHarvesterSyncHandler.class, PacketHarvesterSync.class, packetID++, Side.SERVER);
	}
	
	public void postInit(FMLPostInitializationEvent e) {
		
		if (Config.enableSpawner) {
			pendulumRegistry.postInitVanillaMobs();
		}
		
		if (config.hasChanged()) {
			config.save();
		}
		
		if (config2.hasChanged()) {
			config2.save();
		}
	}
	
	public SoundEvent registerSound(String name) {
		final ResourceLocation resLoc = new ResourceLocation(Cobbleworks.MODID, name);
		return GameRegistry.register(new SoundEvent(resLoc).setRegistryName(resLoc));
	}
}
