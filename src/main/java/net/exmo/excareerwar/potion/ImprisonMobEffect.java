
package net.exmo.excareerwar.potion;


import net.exmo.excareerwar.init.CareerWarModAttributes;
import net.exmo.exmodifier.util.EntityAttrUtil;

import net.exmo.exmodifier.util.gether.AttrGether;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class ImprisonMobEffect extends MobEffect {
	public ImprisonMobEffect() {
		super(MobEffectCategory.HARMFUL, -16777216);
		attrGether2 =new AttrGether(Attributes.KNOCKBACK_RESISTANCE,new AttributeModifier(UUID.fromString("f0f0f0f0-f0f0-f0f0-f0f0-f0f0f0f0f0f1"),"Imprsion",10,AttributeModifier.Operation.ADDITION));

	}
	private static AttrGether attrGether2;
	private static AttrGether attrGether = new AttrGether(Attributes.MOVEMENT_SPEED,new AttributeModifier(UUID.fromString("f0f0f0f0-f0f0-f0f0-f0f0-f0f0f0f0f0f0"),"Imprsion",-10,AttributeModifier.Operation.ADDITION));
	private static AttrGether attrGether3 = new AttrGether(CareerWarModAttributes.JUMPHIGHT.get(),new AttributeModifier(UUID.fromString("f0f0f0f0-f0f0-f0f0-f0f0-f0f0f0f0f0f2"),"Imprsion",-1,AttributeModifier.Operation.ADDITION));
	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {

		//EntityAttrUtil.entityAddAttrTF(attrGether, entity, EntityAttrUtil.WearOrTake.WEAR);
		EntityAttrUtil.entityAddAttrTF(attrGether3, entity, EntityAttrUtil.WearOrTake.WEAR);
		EntityAttrUtil.entityAddAttrTF(attrGether2, entity, EntityAttrUtil.WearOrTake.WEAR);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {

	//	EntityAttrUtil.entityAddAttrTF(attrGether, entity, EntityAttrUtil.WearOrTake.TAKE);
		EntityAttrUtil.entityAddAttrTF(attrGether2, entity, EntityAttrUtil.WearOrTake.TAKE);
		EntityAttrUtil.entityAddAttrTF(attrGether3, entity, EntityAttrUtil.WearOrTake.TAKE);
	}
	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity.level() instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.SMOKE,entity.getX(), entity.getY()+entity.getBbHeight()*0.5, entity.getZ(), 8, 0.4, 0.4, 0.4, 0.05 );	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
