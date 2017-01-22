package curtis.Cobbleworks.module.spawner;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class PendulumMob {
	
	private String theMob;
	private int theLevel;
	private int theScale;
	private String alternate;
	
	public PendulumMob(String mob, int level, int scale, @Nullable String alt) {
		this.theMob = mob;
		this.theLevel = level;
		this.theScale = scale;
		if (alt != null) {
			this.alternate = alt;
		}
	}
	
	public PendulumMob(String mob, int level, @Nullable String alt) {
		this(mob, level, 0, alt);
	}
	
	public String getMob() {
		return this.theMob;
	}
	
	public int getLevel() {
		return this.theLevel;
	}
	
	public int getScale() {
		return this.theScale;
	}
	
	public String getAlt() {
		return alternate == null ? null : alternate;
	}
	
	public NBTTagCompound getTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setString("mob", this.theMob);
		nbt.setInteger("level", this.theLevel);
		nbt.setInteger("scale", this.theScale);
		if (alternate != null) {
			nbt.setString("alt", this.alternate);
		}
		
		return nbt;
	}
}