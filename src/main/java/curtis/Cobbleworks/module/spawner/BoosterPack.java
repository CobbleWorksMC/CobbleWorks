package curtis.Cobbleworks.module.spawner;

import java.util.List;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BoosterPack extends Item {
	
	public BoosterPack() {
		this.setMaxStackSize(1);
		this.setUnlocalizedName(Cobbleworks.MODID + ".boosterPack");
		this.setRegistryName("boosterPack");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		GameRegistry.register(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}
	
	@Override
	public void addInformation(ItemStack card, EntityPlayer caster, List list, boolean bool) {
		list.add("Contains three random cards, used with the pendulum summoning mechanism.");
		list.add("Use on the air to break open. Cards may spill out at your feet.");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack is, World worldIn, EntityPlayer caster, EnumHand hand) {
		if (worldIn.isRemote) {
			return new ActionResult(EnumActionResult.PASS, is);
		}
		
		if (caster instanceof FakePlayer) {
			return new ActionResult(EnumActionResult.FAIL, is);
		}
		
		ItemStack pulls;
		BlockPos pos = caster.getPosition();
		
		for (int i = 0; i < 3; i++) {
			pulls = CommonProxy.pendulumRegistry.getRandomMob();
			worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), pulls));
		}
		
		is.stackSize--;
		
		return new ActionResult(EnumActionResult.SUCCESS, is);
	}
	
	
}
