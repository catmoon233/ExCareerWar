package net.exmo.excareerwar.potion;

import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber
public class ReallyDamageEffect extends MobEffect {
    public ReallyDamageEffect() {
        super(MobEffectCategory.BENEFICIAL, -6684673);
    }

    @SubscribeEvent
    public static void atHurt(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) return;
        Entity entity = event.getSource().getEntity();
        if (entity != null) {
            if (entity instanceof LivingEntity living) {
                if (living.hasEffect(CareerWarModMobEffects.ReallyDamageEffect.get())) {
                    Holder.Reference<DamageType> holderOrThrow = living.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC);

                    // 保存原始标签
                    List<TagKey<DamageType>> originalTags = holderOrThrow.tags().toList();

                    // 添加新的标签
                    holderOrThrow.bindTags(List.of(
                            DamageTypeTags.BYPASSES_COOLDOWN,
                            DamageTypeTags.BYPASSES_INVULNERABILITY,
                            DamageTypeTags.BYPASSES_ARMOR,
                            DamageTypeTags.BYPASSES_RESISTANCE,
                            DamageTypeTags.BYPASSES_EFFECTS,
                            DamageTypeTags.BYPASSES_EFFECTS
                    ));
                    if (event.getEntity().isDeadOrDying()) {
                        holderOrThrow.bindTags(originalTags);
                        return;
                    }
                    // 处理伤害
                    event.getEntity().hurt(new DamageSource(holderOrThrow, living), living.getEffect(CareerWarModMobEffects.ReallyDamageEffect.get()).getAmplifier() * 1);

                    // 重置标签
                    holderOrThrow.bindTags(originalTags);

                }
            }
        }
    }

}
