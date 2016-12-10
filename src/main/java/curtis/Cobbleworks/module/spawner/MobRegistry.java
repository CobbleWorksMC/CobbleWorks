package curtis.Cobbleworks.module.spawner;

import java.util.ArrayList;
import java.util.List;

import curtis.Api.IPendulumRegistry;
import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MobRegistry implements IPendulumRegistry {
	
	public MobRegistry() {
		
	}
	
	public static List<PendulumMob> PendulumMobs = new ArrayList<PendulumMob>();

	@Override
	public boolean registerPendulumMob(String mob, int level, int scale) {
		
		PendulumMob mobToAdd = new PendulumMob(mob, level, scale, null);
		
		try {
		
			PendulumMobs.add(mobToAdd);
			return true;
		
		} catch (Exception e) {
		
			System.out.println("An error occured trying to register " + mob + " to the pendulum mobs registry. Aborting!");
			return false;
		}
	
	}
	
	//Internal use only
	public boolean registerPendulumMobWithAlternative(String mob, int level, int scale, String alternate) {
		
		PendulumMob mobToAdd = new PendulumMob(mob, level, scale, alternate);
		
		try {
		
			PendulumMobs.add(mobToAdd);
			return true;
		
		} catch (Exception e) {
		
			System.out.println("An error occured trying to register " + mob + " to the pendulum mobs registry. Aborting!");
			return false;
		}
	}
	
	public ItemStack getRandomMob() {
		ItemStack result = new ItemStack(CommonProxy.PendulumMonster);
		
		int get = Cobbleworks.rand.nextInt(CommonProxy.pendulumRegistry.PendulumMobs.size());
		
		PendulumMob mob = CommonProxy.pendulumRegistry.PendulumMobs.get(get);
		
		NBTTagCompound q = new NBTTagCompound();
		q.setString("mob", mob.getMob());
		q.setInteger("level", mob.getLevel());
		q.setInteger("scale", mob.getScale());
		
		result.setTagCompound(q);
		
		return result;
	}
	
	public void postInitVanillaMobs() {
		
		//Passive Mobs
		String bat = "Bat";
		String chicken = "Chicken";
		String cow = "Cow";
		String mooshroom = "MushroomCow";
		String pig = "Pig";
		String rabbit = "Rabbit";
		String sheep = "Sheep";
		String squid = "Squid";
		String villager = "Villager";
		
		//Neutral Mobs
		String caveSpider = "CaveSpider";
		String enderman = "Enderman";
		String polarBear = "PolarBear";
		String spider = "Spider";
		String zombiePigman = "PigZombie";
		
		//Hostile Mobs
		String justBlaze = "Blaze";
		String CREEEEEEPERRRR = "Creeper";
		String guardian = "Guardian";
		String ghast = "Ghast";
		String endermite = "Endermite";
		String magmaCube = "LavaSlime";
		String shulk = "Shulker";
		String ironFish = "Silverfish";
		String skeleton = "Skeleton";
		String slime = "Slime";
		//String stray = "Skeleton"; //Not working :(
		String witch = "Witch";
		//String witherSkeleton = "Skeleton"; //Not working :(
		String zombie = "Zombie";
		
		//Tameable Mobs
		String horse = "EntityHorse";
		String kitty = "Ozelot";
		String dog = "Wolf";
		
		//Utility Mobs
		String ironGolem = "VillagerGolem";
		String snowGolem = "SnowMan";
		
		//Actually registering vanilla mobs
		CommonProxy.pendulumRegistry.registerPendulumMob(bat, 3, 4);
		CommonProxy.pendulumRegistry.registerPendulumMob(chicken, 3, 4);
		CommonProxy.pendulumRegistry.registerPendulumMob(cow, 5, 4);
		CommonProxy.pendulumRegistry.registerPendulumMob(mooshroom, 5, 4);
		CommonProxy.pendulumRegistry.registerPendulumMob(pig, 4, 4);
		CommonProxy.pendulumRegistry.registerPendulumMob(rabbit, 4, 5);
		CommonProxy.pendulumRegistry.registerPendulumMob(sheep, 4, 4);
		CommonProxy.pendulumRegistry.registerPendulumMob(squid, 4, 4);
		CommonProxy.pendulumRegistry.registerPendulumMob(villager, 8, 4);
		
		CommonProxy.pendulumRegistry.registerPendulumMob(caveSpider, 6, 5);
		CommonProxy.pendulumRegistry.registerPendulumMob(enderman, 7, 1);
		CommonProxy.pendulumRegistry.registerPendulumMob(polarBear, 4, 5);
		CommonProxy.pendulumRegistry.registerPendulumMob(spider, 5, 6);
		CommonProxy.pendulumRegistry.registerPendulumMob(zombiePigman, 7, 3);
		
		CommonProxy.pendulumRegistry.registerPendulumMob(justBlaze, 3, 8);
		CommonProxy.pendulumRegistry.registerPendulumMob(CREEEEEEPERRRR, 4, 3);
		CommonProxy.pendulumRegistry.registerPendulumMob(guardian, 3, 8);
		CommonProxy.pendulumRegistry.registerPendulumMob(ghast, 7, 2);
		CommonProxy.pendulumRegistry.registerPendulumMob(endermite, 2, 7);
		CommonProxy.pendulumRegistry.registerPendulumMob(magmaCube, 4, 3);
		CommonProxy.pendulumRegistry.registerPendulumMob(shulk, 2, 10);
		CommonProxy.pendulumRegistry.registerPendulumMob(ironFish, 7, 3);
		CommonProxy.pendulumRegistry.registerPendulumMob(skeleton, 4, 5);
		CommonProxy.pendulumRegistry.registerPendulumMob(slime, 2, 6);
		//CommonProxy.pendulumRegistry.registerPendulumMobWithAlternative(stray, 3, 4, "stray");
		CommonProxy.pendulumRegistry.registerPendulumMob(witch, 8, 8);
		//CommonProxy.pendulumRegistry.registerPendulumMobWithAlternative(witherSkeleton, 8, 1, "wither");
		CommonProxy.pendulumRegistry.registerPendulumMob(zombie, 4, 3);
		
		CommonProxy.pendulumRegistry.registerPendulumMob(horse, 4, 5);
		CommonProxy.pendulumRegistry.registerPendulumMob(kitty, 4, 3);
		CommonProxy.pendulumRegistry.registerPendulumMob(dog, 4, 6);
		
		CommonProxy.pendulumRegistry.registerPendulumMob(ironGolem, 10, 2);
		CommonProxy.pendulumRegistry.registerPendulumMob(snowGolem, 7, 3);
		
	}

}
