
package net.exmo.excareerwar.potion;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.exmodifier.init.ExAttribute;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class FastCastEMobEffect extends MobEffect {
	protected final double multiplier =0.1;
	private final static UUID CAST_TIME_REDUCTION_UUID = UUID.fromString("f7f0c0d5-f6f6-4f6f-b6f6-6f6f6f6f6f6f");
	public FastCastEMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -6684673);
		addAttributeModifier(AttributeRegistry.CAST_TIME_REDUCTION.get(), CAST_TIME_REDUCTION_UUID.toString(),  0, AttributeModifier.Operation.MULTIPLY_BASE);

	}

	@Override
	public double getAttributeModifierValue(int p_19457_, AttributeModifier p_19458_) {
		return (p_19457_+1) * multiplier;
	}



	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
