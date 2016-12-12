package curtis.Api;

import net.minecraft.entity.EntityLivingBase;

public interface IPendulumRegistry {
	
	/*
	 * Register a mob to the list of mobs compatible with Cobbleworks' Pendulum Summoning / mob spawning system
	 * 
	 * @Param mob The registry name of the mob to be registered.
	 * @Param level The level of the mob. Remember that you must have scales such that scale1 < level < scale2 or scale1 > level > scale2 to spawn this mob.
	 * @Param scale The scale of the mob, used to determine what mobs this one can be used to spawn.
	 * @Return true if the mob was successfully registered, false if either it was already or there was an error.
	 * */
	public boolean registerPendulumMob(String mobID, int level, int scale);
}
