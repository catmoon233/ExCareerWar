
package net.exmo.excareerwar.potion;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class SpellDamageBoostEMobEffect extends MobEffect {
	protected final double multiplier =0.1;
	private final static UUID SPELL_POWER_UUID = UUID.fromString("f7f0c0d5-f6f8-4f6f-b6f6-6f6f6f6f6f6f");
	public SpellDamageBoostEMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -6684673);
		addAttributeModifier(AttributeRegistry.SPELL_POWER.get(), SPELL_POWER_UUID.toString(),  0, AttributeModifier.Operation.MULTIPLY_BASE);

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
