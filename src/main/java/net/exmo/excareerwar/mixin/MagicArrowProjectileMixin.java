package net.exmo.excareerwar.mixin;

import io.redspace.ironsspellbooks.entity.spells.EchoingStrikeEntity;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowProjectile;
import io.redspace.ironsspellbooks.spells.ender.EchoingStrikesSpell;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.exmo.excareerwar.content.CareerSkill.isSameTeam;

@Mixin(MagicArrowProjectile.class)
public class MagicArrowProjectileMixin {
    @Inject(method = "onHitBlock", at = @At("HEAD"))
    public void onHitBlock(BlockHitResult pResult, CallbackInfo ci) {
        if (pResult.getType()== HitResult.Type.MISS)return;

        MagicArrowProjectile magicArrow = (MagicArrowProjectile) (Object) this;
        CompoundTag persistentData = magicArrow.getPersistentData();
        if (!persistentData.contains("Plus") || !persistentData.getBoolean("Plus"))return;
        if (persistentData.contains("HITis") && persistentData.getBoolean("HITis"))return;
        persistentData.putBoolean("HITis", true);
        Level level = magicArrow.level();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleHelper.UNSTABLE_ENDER, magicArrow.position().x, magicArrow.position().y, magicArrow.position().z, 14, 0.7, 0.7, 0.7, 0.7);

            var owner = ((LivingEntity) magicArrow.getOwner());
            int radius = 2;
            EchoingStrikeEntity echo = new EchoingStrikeEntity(serverLevel, owner, 7f, 3.5f);

            echo.setPos(magicArrow.getBoundingBox().getCenter().subtract(0, echo.getBbHeight() * .5f, 0));
            serverLevel.addFreshEntity(echo);
            List<Entity> entities = level.getEntities(magicArrow, AABB.ofSize(magicArrow.position(), radius * 2, radius, radius * 2));
            for (Entity entity : entities) {

                if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive() && livingEntity!= owner && !isSameTeam(entity, owner)){
                    livingEntity.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), magicArrow), 3);
                    livingEntity.addEffect(new MobEffectInstance(CareerWarModMobEffects.VULNERABILITY.get(), 100, 0));
                    livingEntity.addEffect(new MobEffectInstance(CareerWarModMobEffects.ImprisonMobEffect.get(), 30, 5));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 10, 5));
                }
            }

        }else {
            level.playLocalSound(magicArrow.position().x, magicArrow.position().y, magicArrow.position().z, SoundEvents.DRAGON_FIREBALL_EXPLODE, magicArrow.getSoundSource(), 1, 1, false);

        }
    }
    @Inject(method = "onHitEntity", at = @At("HEAD"))
    public void onHitEntity(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (entityHitResult.getType()== HitResult.Type.MISS)return;
        MagicArrowProjectile magicArrow = (MagicArrowProjectile) (Object) this;
        CompoundTag persistentData = magicArrow.getPersistentData();
        if (!persistentData.contains("Plus") || !persistentData.getBoolean("Plus"))return;
        if (persistentData.contains("HITis") && persistentData.getBoolean("HITis"))return;
        persistentData.putBoolean("HITis", true);
        Level level = magicArrow.level();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleHelper.UNSTABLE_ENDER, magicArrow.position().x, magicArrow.position().y, magicArrow.position().z, 14, 0.7, 0.7, 0.7, 0.7);

            var owner = ((LivingEntity) magicArrow.getOwner());
            int radius = 2;
            EchoingStrikeEntity echo = new EchoingStrikeEntity(serverLevel, owner, 8f, 3.5f);

            echo.setPos(magicArrow.getBoundingBox().getCenter().subtract(0, echo.getBbHeight() * .5f, 0));
            serverLevel.addFreshEntity(echo);
            List<Entity> entities = level.getEntities(magicArrow, AABB.ofSize(magicArrow.position(), radius * 2, radius, radius * 2));
            for (Entity entity : entities) {

                if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive() && livingEntity!= owner && !isSameTeam(entity, owner)){
                    livingEntity.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), magicArrow), 3);
                    livingEntity.addEffect(new MobEffectInstance(CareerWarModMobEffects.VULNERABILITY.get(), 100, 0));
                    livingEntity.addEffect(new MobEffectInstance(CareerWarModMobEffects.ImprisonMobEffect.get(), 30, 5));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 10, 5));
                }
            }

        }
    }
}
