package curtis.Cobbleworks.module.spawner;

import curtis.Cobbleworks.Cobbleworks;
import curtis.Cobbleworks.CommonProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpaceScale extends Item {
	
	public SpaceScale() {
		this.setMaxStackSize(1);
		this.setUnlocalizedName(Cobbleworks.MODID + ".spaceScale");
		this.setRegistryName("spaceScale");
		this.setCreativeTab(CommonProxy.tabcobbleworks);
		GameRegistry.register(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation model = new ModelResourceLocation(getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, model);
	}
	
	
}
