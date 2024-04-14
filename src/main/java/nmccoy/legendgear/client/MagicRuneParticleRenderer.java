package nmccoy.legendgear.client;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class MagicRuneParticleRenderer extends SpriteBillboardParticle {
	
	private double hue;

	private MagicRuneParticleRenderer(ClientWorld clientWorld, double x, double y, double z, double vX, double vY, double vZ) {
		super(clientWorld, x, y, z);
		
		this.x = x;
		this.y = y;
		this.z = z;
		velocityX = vX;
		velocityY = vY;
		velocityZ = vZ;
		prevPosX = x - vX;
		prevPosY = y - vY;
		prevPosZ = z - vZ;

        hue = Math.random() * Math.PI * 2;
		collidesWithWorld = false;
		scale = 0.15f;
		maxAge = 30;
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void move(double dx, double dy, double dz) {
		this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
		this.repositionFromBoundingBox();
	}

	@Override
	public void tick() {
		if (age++ >= maxAge) {
			markDead();
			return;
		} 
		
		// Recolor particles
		hue += 0.3;
        float r = (float) Math.sin(hue) / 2 + 0.5f;
        float g = (float) Math.sin(hue + Math.PI *2/3) / 2 + 0.5f;
        float b = (float) Math.sin(hue - Math.PI *2/3) / 2 + 0.5f;
        
        float freshness = 1 - age * 1.0F / maxAge;
        red = freshness + (1 - freshness) * r;
        green = freshness + (1 - freshness) * g;
        blue = freshness + (1 - freshness) * b;
        alpha = freshness;

        // Move particles
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		
		float f = 0.3f;
		x = prevPosX + velocityX * f;
		y = prevPosY + velocityY * f;
		z = prevPosZ + velocityZ * f;
	}
	
	
	public static class Factory implements ParticleFactory<DefaultParticleType> {

		private final SpriteProvider spriteSet;
		
		public Factory(SpriteProvider spriteSet) {
			this.spriteSet = spriteSet;
		}
		
		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double vX, double vY, double vZ) {
			MagicRuneParticleRenderer renderer = new MagicRuneParticleRenderer(world, x, y, z, vX, vY, vZ);
			renderer.setSprite(spriteSet);
			return renderer;
		}
		
	}
	
}
