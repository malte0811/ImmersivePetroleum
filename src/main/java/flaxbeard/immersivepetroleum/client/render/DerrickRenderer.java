package flaxbeard.immersivepetroleum.client.render;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import flaxbeard.immersivepetroleum.ImmersivePetroleum;
import flaxbeard.immersivepetroleum.common.blocks.tileentities.DerrickTileEntity;
import flaxbeard.immersivepetroleum.common.util.MCUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.data.EmptyModelData;

public class DerrickRenderer implements BlockEntityRenderer<DerrickTileEntity>{
	
	/** Temporary */
	static Random RANDOM = new Random();
	
	static ResourceLocation DERRICK_PIPE_RL;
	static Function<ResourceLocation, BakedModel> f = rl -> MCUtil.getBlockRenderer().getBlockModelShaper().getModelManager().getModel(rl);
	
	/* Called from ClientProxy during ModelRegistryEvent */
	public static final void init(){
		DERRICK_PIPE_RL = new ResourceLocation(ImmersivePetroleum.MODID, "multiblock/dyn/derrick_pipe");
		ForgeModelBakery.addSpecialModel(DERRICK_PIPE_RL);
	}
	
	@Override
	public boolean shouldRenderOffScreen(DerrickTileEntity te){
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void render(DerrickTileEntity te, float partialTicks, PoseStack matrix, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn){
		if(!te.formed || te.isDummy() || !te.getLevelNonnull().hasChunkAt(te.getBlockPos())){
			return;
		}
		
		renderPipe(matrix, bufferIn, te, partialTicks, combinedLightIn, combinedOverlayIn);
	}
	
	static final Vector3f Y_AXIS = new Vector3f(0.0F, 1.0F, 0.0F);
	private void renderPipe(PoseStack matrix, MultiBufferSource buffer, DerrickTileEntity te, float partialTicks, int light, int overlay){
		matrix.pushPose();
		{
			float rot = te.rotation + (te.drilling ? 9 * partialTicks : 0);
			
			matrix.translate(0.5, 0.0, 0.5);
			matrix.mulPose(new Quaternion(Y_AXIS, rot, true));
			List<BakedQuad> quads = f.apply(DERRICK_PIPE_RL).getQuads(null, null, RANDOM, EmptyModelData.INSTANCE);
			Pose last = matrix.last();
			VertexConsumer solid = buffer.getBuffer(RenderType.solid());
			for(BakedQuad quad:quads){
				solid.putBulkData(last, quad, 1.0F, 1.0F, 1.0F, light, overlay);
			}
		}
		matrix.popPose();
	}
}
