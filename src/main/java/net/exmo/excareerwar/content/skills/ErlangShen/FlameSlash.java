package net.exmo.excareerwar.content.skills.ErlangShen;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.command.CastCommand;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.flame_strike.FlameStrike;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.init.CareerWarSpellRegistry;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.init.RegisterOther;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.item.enchantment.ThornsEnchantment.getDamage;

@AutoInit
public class FlameSlash extends CareerSkill {
    public FlameSlash() {
        super(CareerWarSpellRegistry.FLAM_SLASH.get(),5);
        this.CoolDown = 40*12;
        this.IconTexture = SpellRegistry.FLAMING_STRIKE_SPELL.get().getSpellIconResource();
        this.LocalDescription = "FlameSlash_d";
    }

    @Override
    public void use(Player entity) {
        if (!setCoolDown(entity))return;
        if (entity.level().isClientSide){
            ClientSpellCastHelper.handleClientBoundOnCastStarted(entity.getUUID(),"irons_spellbooks:flaming_strike",5);
        }
        Excareerwar.queueServerWork(7, () -> {
            if (entity==null)return;
            if (entity.isDeadOrDying())return;
        Level level = entity.level();
        entity.heal(2.5f);
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 70, 2, false, false,false));

        float radius = 4.3f;
        float distance = 2.3f;
        Vec3 forward = entity.getForward();
        Vec3 hitLocation = entity.position().add(0, entity.getBbHeight() * .3f, 0).add(forward.scale(distance));
        var entities = level.getEntities(entity, AABB.ofSize(hitLocation, radius * 2, radius, radius * 2));
        var damageSource = new DamageSource(level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC),entity);
        for (Entity targetEntity : entities) {
            if (isSameTeam(targetEntity, entity))continue;

            if (targetEntity instanceof LivingEntity && targetEntity.isAlive() && entity.isPickable() && targetEntity.position().subtract(entity.getEyePosition()).dot(forward) >= 0 && entity.distanceToSqr(targetEntity) < radius * radius && Utils.hasLineOfSight(level, entity.getEyePosition(), targetEntity.getBoundingBox().getCenter(), true)) {
                Vec3 offsetVector = targetEntity.getBoundingBox().getCenter().subtract(entity.getEyePosition());
                if (offsetVector.dot(forward) >= 0) {
                    if (DamageSources.applyDamage(targetEntity, (float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE)*0.9f+2.5f), damageSource)) {
                        ((LivingEntity) targetEntity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 6, false, false,false));
                        ((LivingEntity) targetEntity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 6, false, false,false));
                        ((LivingEntity) targetEntity).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 6, false, false,false));
                        ((LivingEntity) targetEntity).addEffect(new MobEffectInstance(CareerWarModMobEffects.ImprisonMobEffect.get(), 40, 1, false, false,false));

                        MagicManager.spawnParticles(level, ParticleHelper.EMBERS, targetEntity.getX(), targetEntity.getY() + targetEntity.getBbHeight() * .5f, targetEntity.getZ(), 50, targetEntity.getBbWidth() * .5f, targetEntity.getBbHeight() * .5f, targetEntity.getBbWidth() * .5f, .03, false);
                        EnchantmentHelper.doPostDamageEffects(entity, targetEntity);
                        entity.addEffect(new MobEffectInstance(CareerWarModMobEffects.ReallyDamageEffect.get(), (int) (20*4.5), 1, false, false,false));
                        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, (int) (20*4.5), 1, false, false,false));

                    }
                }
            }
        }

        MagicData magicData = MagicData.getPlayerMagicData(entity);
        boolean mirrored = magicData.getCastingEquipmentSlot().equals(SpellSelectionManager.OFFHAND);
        FlameStrike flameStrike = new FlameStrike(level, mirrored);
        flameStrike.moveTo(hitLocation);
        flameStrike.setYRot(entity.getYRot());
        flameStrike.setXRot(entity.getXRot());
        level.addFreshEntity(flameStrike);
        });
    }


}
