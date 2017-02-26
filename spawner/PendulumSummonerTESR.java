package curtis.Cobbleworks.module.spawner;

import org.lwjgl.opengl.GL11;

import curtis.Cobbleworks.Cobbleworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PendulumSummonerTESR extends TileEntitySpecialRenderer<TileEntityPendulumSummoner> {
	
	private IModel model;
	private IBakedModel bakedModel;
	
	public PendulumSummonerTESR() {}
	
	private IBakedModel getBakedModel() {
		
		if (bakedModel == null) {
			try {
			model = ModelLoaderRegistry.getModel(new ResourceLocation(Cobbleworks.MODID, "block/newSummoner.obj"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
		
		return bakedModel;
	}
	
	@Override
	public void renderTileEntityAt(TileEntityPendulumSummoner te, double x, double y, double z, float partialTicks, int destroyStage) {
		
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		
		Tessellator tess = Tessellator.getInstance();
		World world = te.getWorld();
		
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		GlStateManager.translate(x, y, z);
		
		GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ()+1);
		GlStateManager.disableRescaleNormal();
		
		tess.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, getBakedModel(), world.getBlockState(te.getPos()), te.getPos(), Tessellator.getInstance().getBuffer(), false);
		tess.draw();
		
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}
	
	
}
