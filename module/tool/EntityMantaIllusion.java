package curtis.Cobbleworks.module.tool;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityMantaIllusion extends EntityTameable {
	
	private static final int timedLifeMax = 500;

	public EntityMantaIllusion(World worldIn) {
		super(worldIn);
		
		setTamed(true);
		setAttackTarget((EntityLivingBase)null);
		
		//tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(2, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
		
        //this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD));
        
        setCanPickUpLoot(false);
        
        setSize(0.6F, 1.8F);
	}
	
	@Override
	public boolean canDropLoot() {
		return false;
	}
	
	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}
	
	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
		//this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase target) {
		if (target == this.getOwner() || (target instanceof EntityMantaIllusion) && ((EntityMantaIllusion)target).getOwner() == this.getOwner()) {
			return;
		}
		super.setAttackTarget(target);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (this.worldObj.isRemote) {
			return;
		}
		
		EntityLivingBase owner = this.getOwner();
		if (owner == null || owner.isDead || owner.dimension != this.dimension || this.ticksExisted >= EntityMantaIllusion.timedLifeMax) {
			this.attackEntityFrom(DamageSource.generic, this.getMaxHealth());
		}
	}
	
	@Override
	public Item getDropItem() {
		return Item.getItemById(-1);
	}
	
	@Override
	public boolean isBreedingItem(ItemStack is) {
		return false;
	}
	
	@Override
	public boolean canMateWith(EntityAnimal ea) {
		return false;
	}
	
	@Override
	public int getMaxSpawnedInChunk() {
		return 0;
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	public void setOwner(EntityPlayer ep) {
		setOwnerId(ep.getUniqueID());
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance d, IEntityLivingData ld) {
		return super.onInitialSpawn(d, ld);
	}
	
	/*
	 * Copied from EntityWolf and EntityZombie
	 * */
	@Override
	public boolean attackEntityAsMob(Entity target) {
		
		boolean flag = super.attackEntityAsMob(target);
		flag = target.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (flag) {
            this.applyEnchantments(this, target);
        }

        return flag;
    }
	
	
	@Override
	protected SoundEvent getDeathSound() {
		return CommonProxy.illusionDeath;
	}
}