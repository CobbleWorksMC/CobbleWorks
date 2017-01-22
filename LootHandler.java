package curtis.Cobbleworks;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//I had to look specifically at Botania to see how this was done. Shoutout to Vazkii for having legible code.
public class LootHandler {
	
	public static final List<String> lootList = ImmutableList.of(
			"loot/stronghold_corridor",
			"loot/jungle_temple",
			"loot/nether_bridge",
			"loot/end_city_treasure",
			"loot/desert_pyramid"
			);
	
	public LootHandler() {
		for (String str : lootList) {
			LootTableList.register(new ResourceLocation(Cobbleworks.MODID, str));
		}
	}
	
	@SubscribeEvent
	public void lootLoad(LootTableLoadEvent e) {
		String prefix = "minecraft:chests/";
		String name = e.getName().toString();
		
		if (name.startsWith(prefix)) {
			String file = name.substring(name.indexOf(prefix) + prefix.length());
			//System.out.println("WATCH FOR THIS YOU MORON");
			//System.out.println(file);
			switch (file) {
			case "stronghold_corridor": 	e.getTable().addPool(getPool(file)); break;
			case "jungle_temple": 			e.getTable().addPool(getPool(file)); break;
			case "nether_bridge" :			e.getTable().addPool(getPool(file)); break;
			case "end_city_treasure": 		e.getTable().addPool(getPool(file)); break;
			case "desert_pyramid": 			e.getTable().addPool(getPool(file)); break;
			default: break;
			}
		}
	}

	private LootPool getPool(String file) {
		return new LootPool(new LootEntry[] {getLootEntry(file, 1) }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0, 1), Cobbleworks.MODID + "_loot_pool");
	}

	private LootEntry getLootEntry(String file, int i) {
		return new LootEntryTable(new ResourceLocation(Cobbleworks.MODID, "loot/" + file), i, 0, new LootCondition[0], Cobbleworks.MODID + "_loot_pool");
	}
}