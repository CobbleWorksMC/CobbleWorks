package curtis.Cobbleworks.module.tool;

import java.util.List;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpeedBoots extends ItemArmor {
	
	private static final int maxSprintTime = 90;
	private static final int maxChargeTime = 60;
	
	public SpeedBoots() {
		super(ArmorMaterial.DIAMOND, 0, EntityEquipmentSlot.FEET);
		this.setUnlocalizedName(Cobbleworks.MODID + ".speedBoosters");
		this.setRegistryName("speedBoosters");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		GameRegistry.register(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}
	
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return "cobbleworks:textures/armor/speedBoosters.png";
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer caster) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("sprintTime", 0);
			stack.getTagCompound().setInteger("chargeTime", 0);
			stack.getTagCompound().setBoolean("shinespark", false);
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity caster, int itemSlot, boolean isSelected) {
		
		if (worldIn.isRemote) {
			return;
		}
		
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("sprintTime", 0);
			stack.getTagCompound().setInteger("chargeTime", 0);
			stack.getTagCompound().setBoolean("shinespark", false);
		}
		
		int sprint = stack.getTagCompound().getInteger("sprintTime");
		int charge = stack.getTagCompound().getInteger("chargeTime");
		
		if (sprint > maxSprintTime) {
			sprint = maxSprintTime;
		} else if (charge < 0) {
			charge = 0;
		}
		if (charge > maxChargeTime) {
			charge = maxChargeTime;
		} else if (charge < 0) {
			charge = 0;
		}
		
		if (sprint > 80) {
			charge = maxChargeTime;
		} else {
			--charge;
		}
		
		if (caster.isSprinting()) {
			++sprint;
			if (sprint > maxSprintTime) {
				sprint = maxSprintTime;
			}
			
		} else {
			sprint -= 10;
			if (sprint < 0) {
				sprint = 0;
			}
		}
		
		stack.getTagCompound().setInteger("sprintTime", sprint);
		stack.getTagCompound().setInteger("chargeTime", charge);
		
		if (caster.onGround) {
			stack.getTagCompound().setBoolean("shinespark", false);
		} else if (stack.getTagCompound().getBoolean("shinespark")) {
			caster.fallDistance = 0;
		}
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, EntityPlayer caster, List<String> tooltip, boolean advanced) {
		tooltip.add("Makes you go fast. Sometimes really fast.");
	}
	
	public float getSpeed(ItemStack boots) {
		
		if (!(boots.getItem() instanceof SpeedBoots)) {
			return 0F;
		}
		
		int sprint = boots.getTagCompound().getInteger("sprintTime");
		return (0.015F * ((sprint / 10) + 1));
	}
	
	@SubscribeEvent
	public void updatePlayerStepStatus(LivingUpdateEvent event) {
		
		if (!(event.getEntityLiving() instanceof EntityPlayer)) {
			return;
		}
		
		EntityPlayer caster = (EntityPlayer)event.getEntityLiving();
		ItemStack boots = caster.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		
		if (boots == null) {
			return;
		}
		
		if (!(boots.getItem() instanceof SpeedBoots)) {
			return;
		}
		
		//looked at botania's sash (ItemTravelBelt) for help with this one
		if (caster.worldObj.isRemote) {
			if ((caster.onGround || caster.capabilities.isFlying) && caster.moveForward > 0F && !caster.isInsideOfMaterial(Material.WATER)) {
				caster.moveRelative(0F, 1.0F, getSpeed(boots));
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		
		if (!(event.getEntityLiving() instanceof EntityPlayer)) {
			return;
		}
		
		if (event.getEntityLiving().worldObj.isRemote) {
			return;
		}
		
		EntityPlayer caster = (EntityPlayer)event.getEntityLiving();
		ItemStack boots = caster.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		
		if (boots == null) {
			return;
		}
		
		if (!(boots.getItem() instanceof SpeedBoots)) {
			return;
		}
		
		if (!caster.isSneaking() || caster.capabilities.isFlying || caster.isInsideOfMaterial(Material.WATER)) {
			return;
		}
		
		//Shinespark time!
		if (boots.getTagCompound().getInteger("chargeTime") > 0) {
			
			float energyAvailable = .7F * caster.getHealth();
			Vec3d direction = caster.getLookVec();
			
			double x = direction.xCoord;
			double y = direction.yCoord;
			double z = direction.zCoord;
			
			x *= (energyAvailable / 2.0);
			y *= (energyAvailable / 2.4);
			z *= (energyAvailable / 2.0);
			
			caster.addVelocity(x, y, z);
			caster.attackEntityFrom(DamageSource.magic, energyAvailable);
			
			boots.getTagCompound().setBoolean("shinespark", true);
		}
		
	}
}
