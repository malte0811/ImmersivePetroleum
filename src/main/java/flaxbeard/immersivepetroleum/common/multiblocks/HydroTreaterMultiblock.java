package flaxbeard.immersivepetroleum.common.multiblocks;

import com.mojang.blaze3d.vertex.PoseStack;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import flaxbeard.immersivepetroleum.ImmersivePetroleum;
import flaxbeard.immersivepetroleum.common.IPContent;
import flaxbeard.immersivepetroleum.common.IPContent.Multiblock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HydroTreaterMultiblock extends IETemplateMultiblock{
	public static final HydroTreaterMultiblock INSTANCE = new HydroTreaterMultiblock();
	
	public HydroTreaterMultiblock(){
		super(new ResourceLocation(ImmersivePetroleum.MODID, "multiblocks/hydrotreater"),
				new BlockPos(1, 0, 2), new BlockPos(1, 1, 3), new BlockPos(3, 3, 4),
				() -> IPContent.Multiblock.hydrotreater.defaultBlockState());
	}
	
	@Override
	public float getManualScale(){
		return 12.0F;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRenderFormedStructure(){
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	private static ItemStack renderStack;
	
	@Override
	public void renderFormedStructure(PoseStack transform, MultiBufferSource buffer){
		if(renderStack == null)
			renderStack = new ItemStack(Multiblock.hydrotreater);
		
		// "Undo" the GUI Perspective Transform
		transform.translate(1.5, 0.5, 2.5);
		
		ClientUtils.mc().getItemRenderer().renderStatic(
				renderStack,
				ItemTransforms.TransformType.NONE,
				0xf000f0,
				OverlayTexture.NO_OVERLAY,
				transform, buffer);
	}
}
