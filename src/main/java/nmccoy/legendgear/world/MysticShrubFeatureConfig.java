package nmccoy.legendgear.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;

public record MysticShrubFeatureConfig(float starChance) implements FeatureConfig {

	public static final Codec<MysticShrubFeatureConfig> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Codecs.POSITIVE_FLOAT.fieldOf("star_chance").forGetter(MysticShrubFeatureConfig::starChance)
				).apply(instance, MysticShrubFeatureConfig::new));
	
}
