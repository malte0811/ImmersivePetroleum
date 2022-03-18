package flaxbeard.immersivepetroleum.common.particle;

import java.util.Random;

import com.mojang.math.Vector3f;

import flaxbeard.immersivepetroleum.ImmersivePetroleum;
import flaxbeard.immersivepetroleum.common.util.MCUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

public class FlareFire extends SimpleAnimatedParticle{
	final double ogMotionY;
	final float red, green, blue;
	final float rotation;
	protected FlareFire(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet spriteWithAge){
		super(world, x, y, z, spriteWithAge, 0.0F);
		setSize(0.5F, 0.5F);
		setSpriteFromAge(spriteWithAge);
		this.friction = 1.0F;
		setColor(1.0F, 1.0F, 1.0F);
		setLifetime(60);
		// this.canCollide = false;
		this.quadSize = 8 / 16F;
		
		this.red = this.green = this.blue = 1.0F;
		
		this.ogMotionY = motionY;
		
		this.rotation = 0.250F * (world.random.nextFloat() - 0.5F);
		
		this.oRoll = 360.0F * world.random.nextFloat();
		this.roll = this.oRoll + (this.rotation * world.random.nextFloat());
		
		// These arent actualy used, setting them to 0 anyway though just incase
		this.xd = this.yd = this.zd = 0.0;
	}
	
	@Override
	public void tick(){
		float f = (this.age / (float) this.lifetime);
		Vector3f vec = Wind.getDirection();
		
		if(this.age++ >= this.lifetime){
			remove();
		}
		if(this.age == this.lifetime - 36){
			this.rCol = this.gCol = this.bCol = (float)(0.4F * Math.random());
		}
		setSpriteFromAge(this.sprites);
		
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.oRoll = this.roll;
		
		this.move(vec.x() * f, this.ogMotionY * (1F - f), vec.z() * f);
		this.roll += this.rotation;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType>{
		private final SpriteSet spriteSet;
		
		public Factory(SpriteSet spriteSet){
			this.spriteSet = spriteSet;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed){
			return new FlareFire(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}
	
	/**
	 * Global presudo-wind for the flarestack flame
	 * 
	 * @author TwistedGate
	 */
	@Mod.EventBusSubscriber(modid = ImmersivePetroleum.MODID, value = Dist.CLIENT)
	public static class Wind{
		private static Vector3f vec = new Vector3f(0.0F, 0.0F, 0.0F);
		private static long lastGT;
		private static float lastDirection;
		private static float thisDirection;
		
		public static Vector3f getDirection(){
			return vec;
		}
		
		@SubscribeEvent
		public static void clientTick(TickEvent.ClientTickEvent event){
			if(event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.START){
				ClientLevel world = MCUtil.getLevel();
				if(world == null)
					return;
				
				long gameTime = world.getGameTime();
				if((gameTime / 20) != lastGT){
					lastGT = gameTime / 20;
					
					double fGameTime = (gameTime / 20D);
					Random lastRand = new Random(Mth.floor(fGameTime));
					Random thisRand = new Random(Mth.ceil(fGameTime));
					
					lastDirection = lastRand.nextFloat() * 360;
					thisDirection = thisRand.nextFloat() * 360;
				}
				
				double interpDirection = Mth.lerp(((gameTime % 20) / 20F), lastDirection, thisDirection);
				
				float xSpeed = (float) Math.sin(interpDirection) * .1F;
				float zSpeed = (float) Math.cos(interpDirection) * .1F;
				
				vec = new Vector3f(xSpeed, 0.0F, zSpeed);
			}
		}
	}
}
