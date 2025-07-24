

package net.exmo.excareerwar.potion;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class CanChangeCareerMobEffect extends MobEffect {

	public CanChangeCareerMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -6684673);

	}


	@Override
	public void applyEffectTick(LivingEntity entity, int p_19468_) {
		if (entity instanceof ServerPlayer serverPlayer){
			serverPlayer.sendSystemMessage(Component.literal("按下\"H\"选择职业"),true);
		}
		super.applyEffectTick(entity, p_19468_);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
