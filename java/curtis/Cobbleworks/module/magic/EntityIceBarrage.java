package curtis.Cobbleworks.module.magic;

import java.util.List;
import java.util.Random;

import curtis.Cobbleworks.Cobbleworks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityIceBarrage extends Entity {
	
	public EntityIceBarrage(World worldIn) {
		super(worldIn);
	}
	
	public EntityIceBarrage(World worldIn, Entity caster, EntityLivingBase target) {
		super(worldIn);
		this.setPosition(target.posX, target.posY, target.posZ);
	}

	@Override
	protected void entityInit() {
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (this.ticksExisted >= 50) {
			this.setDead();
		}
	}
}
