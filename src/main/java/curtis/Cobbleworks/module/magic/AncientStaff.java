package curtis.Cobbleworks.module.magic;

import java.util.List;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import curtis.Cobbleworks.module.tool.SuperAxe;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AncientStaff extends Item {
	
	public static final int maxCD = 60;
	public static final int maxDelay = 37;
	public int cd = 0;
	public int delay = 0;
	
	List<EntityLivingBase> targets;
	
	public AncientStaff() {
		this.setUnlocalizedName(Cobbleworks.MODID + ".ancientStaff");
		this.setRegistryName("ancientStaff");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		this.setMaxStackSize(1);
		GameRegistry.register(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("cd", 0);
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity caster, int itemSlot, boolean isSelected) {
		
		if (worldIn.isRemote) {
			return;
		}
		
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("cd", 0);
		}
		
		int cd = stack.getTagCompound().getInteger("cd");
		
		if (cd > 0) {
			cd--;
		}
		//Sanity check
		if (cd < 0 || cd > AncientStaff.maxCD) {
			cd = AncientStaff.maxCD;
		}
		
		if (delay > 0) {
			delay--;
		}
		if (delay < 0 || delay > AncientStaff.maxDelay) {
			delay = 0;
		}
		
		if (delay == 1) {
			actuallySpellcast(caster);
		}
		
		stack.getTagCompound().setInteger("cd", cd);
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
	tooltip.add("A fine treasure from the desert.");	
	    if (stack.hasTagCompound()) {
			if (stack.getTagCompound().hasKey("cd")) {
				int cd = stack.getTagCompound().getInteger("cd");
				if (cd > 0) {
					tooltip.add("Cooldown remaining: " + cd + " ticks.");
				} else {
					tooltip.add("Ready to use. 3 second cooldown.");
				}
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack is, World worldIn, EntityPlayer caster, EnumHand hand) {
		
		if (!is.hasTagCompound()) {
			return new ActionResult(EnumActionResult.FAIL, is);
		}
		
		if (worldIn.isRemote) {
			return new ActionResult(EnumActionResult.PASS, is);
		}
		
		if (caster instanceof FakePlayer) {
			return new ActionResult(EnumActionResult.FAIL, is);
		}
		
		if (is.getTagCompound().getInteger("cd") == 0) {
			boolean bool = iceBarrage(caster);
			if (bool) {
				worldIn.playSound(null, caster.getPosition(), CommonProxy.iceBarrageCast, SoundCategory.PLAYERS, 0.75F, 1.0F);
				is.getTagCompound().setInteger("cd", this.maxCD);
				delay = this.maxDelay;
				for (EntityLivingBase target: targets) {
		    		if (target != caster) {
		    			target.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 400, 7, false, false));
		    		}
		    	}
			}
		}
		
		return new ActionResult(EnumActionResult.SUCCESS, is);
	}
	
	public boolean iceBarrage(EntityPlayer caster) {
		
		if (caster == null) {
    		return false;
    	}
		if (caster instanceof FakePlayer) {
			return false;
		}
		
		EntityLivingBase primeTarget = rayCast(caster);
		
		if (primeTarget == null) {
			return false;
		}
		
		BlockPos pos = primeTarget.getPosition();
		
    	this.targets = (caster.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX()-2, pos.getY()-2, pos.getZ()-2, pos.getX()+2, pos.getY()+2, pos.getZ()+2)));
    	
    	if (targets == null || targets.size() == 0) {
    		return false;
    	}
    	
    	return true;
	}
	
	public boolean actuallySpellcast(Entity caster) {
		if (this.targets == null) {
			return false;
		}
		
		double kbr = 0;
		EntityIceBarrage eib = null;
		
		for (EntityLivingBase target: targets) {
    		if (target != caster) {
    			
    			DamageSource ds = DamageSource.causeIndirectMagicDamage(caster, target);
    			float damage = 2.5f + 7.5f * Cobbleworks.rand.nextFloat();
    			
    			if (target.isImmuneToFire()) {
    				damage *= 1.5;
    			} else if (target.isBurning()){
    				damage *= 1.5;
    				target.extinguish();
    			}
    			
    			kbr = target.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue();
    			
    			target.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    			target.attackEntityFrom(ds, damage);
    			target.worldObj.playSound(null, target.getPosition(), CommonProxy.iceBarrageImpact, SoundCategory.HOSTILE, 0.75f, 1.0f);
    			eib = new EntityIceBarrage(caster.worldObj, caster, target);
    			
    			if (eib != null) {
    				caster.worldObj.spawnEntityInWorld(eib);
    			}
    			
    			target.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(kbr);
    		}
    	}
		
		return true;
	}
	
	public EntityLivingBase rayCast(Entity caster) {
		
		List<EntityLivingBase> potentialTargets = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(caster.posX-32, caster.posY-32, caster.posZ-32, caster.posX+32, caster.posY+32, caster.posZ+32));
		
		Vec3d casterEyes = new Vec3d(caster.posX, caster.posY + (double)caster.getEyeHeight(), caster.posZ);
		Vec3d casterLook = caster.getLook(1.0f);
		//System.out.println(casterLook.toString());
		Vec3d targetVec = null;
		
		if (potentialTargets.contains(caster)) {
			potentialTargets.remove(caster);
		}
		double d2;
		
		for (EntityLivingBase potentialTarget : potentialTargets) {
			EntityLivingBase target = potentialTarget;
            AxisAlignedBB axisalignedbb = target.getEntityBoundingBox().expand(target.width, target.width, target.height);
            
            d2 = casterEyes.distanceTo(new Vec3d(target.posX, target.posY + (double)target.getEyeHeight(), target.posZ));
            targetVec = casterEyes.addVector(casterLook.xCoord * d2, casterLook.yCoord * d2, casterLook.zCoord * d2);
            
            if (axisalignedbb.isVecInside(targetVec)) {
            	return target;
            }
		}
		
		
		return null;
	}
}
