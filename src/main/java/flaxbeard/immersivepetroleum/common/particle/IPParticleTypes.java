package flaxbeard.immersivepetroleum.common.particle;

import com.mojang.serialization.Codec;

import flaxbeard.immersivepetroleum.ImmersivePetroleum;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

public class IPParticleTypes{
	public static final SimpleParticleType FLARE_FIRE = createBasicParticle("flare_fire", false);
	public static final ParticleType<FluidParticleData> FLUID_SPILL = createParticleWithData("fluid_spill", FluidParticleData.DESERIALIZER, FluidParticleData.CODEC);
	
	private static SimpleParticleType createBasicParticle(String name, boolean alwaysShow){
		SimpleParticleType particleType = new SimpleParticleType(alwaysShow);
		particleType.setRegistryName(new ResourceLocation(ImmersivePetroleum.MODID, name));
		return particleType;
	}
	
	@SuppressWarnings("deprecation")
	private static <T extends ParticleOptions> ParticleType<T> createParticleWithData(String name, ParticleOptions.Deserializer<T> deserializer, Codec<T> codec){
		ParticleType<T> type = new ParticleType<T>(false, deserializer){
			@Override
			public Codec<T> codec(){
				return codec;
			}
		};
		type.setRegistryName(new ResourceLocation(ImmersivePetroleum.MODID, name));
		return type;
	}
}
