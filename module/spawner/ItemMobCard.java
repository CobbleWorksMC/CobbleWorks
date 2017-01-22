package curtis.Cobbleworks.module.spawner;

import java.util.List;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.spawner.MobRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMobCard extends Item {
	
	private PendulumMob mob;
	
	
	public ItemMobCard() {
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(Cobbleworks.MODID + ".mobCard");
		this.setRegistryName("mobCard");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		GameRegistry.register(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelBakery.registerItemVariants(this, model);
		ModelLoader.setCustomMeshDefinition(this, stack -> { return model; });
	}
	
	public static String getMob(ItemStack card) {
		String mob = "Bat";
		
		if (card.hasTagCompound()) {
			NBTTagCompound q = card.getTagCompound();
			if (q.hasKey("mob")) {
				mob = q.getString("mob");
			}
		}
		
		return mob;
	}
	
	public static int getScale(ItemStack card) {
		
		int scale = 0;
		
		if (card.hasTagCompound()) {
			NBTTagCompound q = card.getTagCompound();
			if (q.hasKey("scale")) {
				scale = q.getInteger("scale");
			}
		}
		
		return scale;
	}
	
	public static int getLevel(ItemStack card) {
		int level = 0;
		
		if (card.hasTagCompound()) {
			NBTTagCompound q = card.getTagCompound();
			if (q.hasKey("level")) {
				level = q.getInteger("level");
			}
		}
		
		return level;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List subItems) {
		
		for (PendulumMob mob : MobRegistry.PendulumMobs) {
			//System.out.println("Registered a sub item mob card for: " + mob.getMob());
			ItemStack newCard = new ItemStack(item);
			newCard.setTagCompound(mob.getTag());
			subItems.add(newCard);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack card, EntityPlayer caster, List list, boolean bool) {
		if (card.hasTagCompound()) {
			World world = caster.getEntityWorld();
			NBTTagCompound q = card.getTagCompound();
			String str;
			if (q.hasKey("mob")) {
				
				Entity elb = EntityList.createEntityByName(q.getString("mob"), world);
				
				if (q.hasKey("alt")) {
					if (q.getString("alt") == "wither") {
						str = "Wither Skeleton";
					} else if (q.getString("alt") == "stray") {
						str = "Stray";
					} else {
						str = " ";
					}
					
					list.add(new String("Mob contained: " + str));
				} else {
					if (q.getString("mob") == "Villager") {
						list.add("Mob Contained: Villager");
					} else {
						list.add(new String("Mob contained: " + elb.getDisplayName().getFormattedText()));
					}
				}
			}
			
			if (q.hasKey("level")) {
				list.add(new String("Level: " + q.getInteger("level")));
			}
			
			if (q.hasKey("scale")) {
				list.add(new String("Scale: " + q.getInteger("scale")));
			}
		} else {
			list.add(new String("Brimming with potential..."));
		}
	}
}