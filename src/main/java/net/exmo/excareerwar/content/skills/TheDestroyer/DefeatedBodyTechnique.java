package net.exmo.excareerwar.content.skills.TheDestroyer;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.command.CastCommand;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.content.careers.TheDestroyer;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.excareerwar.util.EntityPointer;
import net.exmo.excareerwar.util.PathGenerator;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.ScaffoldingBlock.getDistance;

@AutoInit
public class DefeatedBodyTechnique extends CareerSkill {
    public DefeatedBodyTechnique() {
        super("DefeatedBodyTechnique");
        this.CoolDown = (int) (40 * 8);
        this.IconTexture = SpellRegistry.BLOOD_STEP_SPELL.get().getSpellIconResource();
        this.LocalDescription = "DefeatedBodyTechnique_d";
    }

    @Override
    public void use(Player player) {
       if( SkillHandle.getSkillV(player, getLocalName())>0 &&SkillHandle.getSkillV( player,getLocalName())<this.CoolDown*0.5){
           player.getPersistentData().putInt("DefeatedBodyTechniqueP",0);

       }
        if (player.getCooldowns().isOnCooldown(ItemRegistry.KEEPER_FLAMBERGE.get()))return;
        int defeatedBodyTechniqueP = player.getPersistentData().getInt("DefeatedBodyTechniqueP");
        if (defeatedBodyTechniqueP >0 ){

            Level level = player.level();
            if (level.isClientSide){
                ClientSpellCastHelper.handleClientBoundOnCastStarted(player.getUUID(),"irons_spellbooks:blood_step",5);
            }
            if (defeatedBodyTechniqueP==1){
                if (level instanceof ServerLevel serverLevel){
                    Vec3 first = player.position();
                    SkillHandle.sendParticleCircle(serverLevel, player, ParticleTypes.LARGE_SMOKE, 3, 7);
                    if (!level.isClientSide) player.teleportTo(player.getPersistentData().getDouble("FirstPosDBTX"), player.getPersistentData().getDouble("FirstPosDBTY"), player.getPersistentData().getDouble("FirstPosDBTZ"));
                    {
                        player.playSound(SoundEvents.TRIDENT_RETURN);
                        final Vec3 _center = player.position();
                        List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                        for (Entity entityiterator : _entfound) {
                            if (_entfound instanceof FrozenHumanoid frozenHumanoid){
                                if (frozenHumanoid.getSummoner()==player){
                                    frozenHumanoid.remove(Entity.RemovalReason.DISCARDED);
                                }
                            }
                            if (entityiterator != player) {
                                if (isSameTeam(entityiterator, player))continue;

                                if (entityiterator instanceof LivingEntity living) {
                                    player.heal(0.25F);
                                    entityiterator.invulnerableTime = 0;
                                    living.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), player), (float) ((getAttackDamage(player)*0.75f+2)));
                                    TheDestroyer.addEffectB(living,1);
                                }
                            }

                        }
                    }
                    List<Vec3> vl = PathGenerator.generatePath(player.position(), first);
                    for (Vec3 v : vl) {

                            serverLevel.sendParticles(ParticleRegistry.BLOOD_PARTICLE.get(), v.x(), v.y(), v.z(), 5, 0.3, 0.3, 0.3, 0.1);
                            {
                                final Vec3 _center = v;
                                List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                                for (Entity entityiterator : _entfound) {
                                    if (entityiterator != player) {
                                        if (isSameTeam(entityiterator, player))continue;

                                        if (entityiterator instanceof LivingEntity living) {
                                            if (entityiterator instanceof FrozenHumanoid)continue;
                                            player.heal(0.25F);
                                            entityiterator.invulnerableTime = 0;
                                            entityiterator.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), player), (float) ((float) (getAttackDamage(player) * 0.6 + 2)*0.2));
                                            entityiterator.setDeltaMovement(new Vec3(0, 0.1, 0));
                                            TheDestroyer.addEffectB(living,1);
                                        }

                                    }

                            }
                        }
                    }
                }
            }
            else  {

                if (level.isClientSide()){
                    Vec3 forward = player.getForward().normalize();
                    for (int i = 0; i < 35; i++) {
                        Vec3 motion = forward.scale(Utils.random.nextDouble() * .25f);
                        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, player.getRandomX(.4f), player.getRandomY(), player.getRandomZ(.4f), motion.x, motion.y, motion.z);
                    }
                }
                if (defeatedBodyTechniqueP == 3 ) {

                    FrozenHumanoid shadow = new FrozenHumanoid(level, player);
                    shadow.setShatterDamage(1);
                    shadow.setDeathTimer((int) (this.CoolDown*0.25));
                    level.addFreshEntity(shadow);
                    if (!level.isClientSide) {
                        player.getPersistentData().putDouble("FirstPosDBTX", player.getX());
                        player.getPersistentData().putDouble("FirstPosDBTY", player.getY());
                        player.getPersistentData().putDouble("FirstPosDBTZ", player.getZ());
                    }
                }
                Vec3 first = player.position();

                    Vec3 dest = null;
                    MagicData magicData = MagicData.getPlayerMagicData(player);
                    var teleportData = (TeleportSpell.TeleportData) magicData.getAdditionalCastData();
                    if (teleportData != null) {
                        var potentialTarget = teleportData.getTeleportTargetPosition();
                        if (potentialTarget != null) {
                            dest = potentialTarget;
                            player.teleportTo(dest.x, dest.y, dest.z);
                        }
                    } else {
                        HitResult hitResult = EntityPointer.raycastForEntity(level, player, 8, true);
                        if (player.isPassenger()) {
                            player.stopRiding();
                        }
                        if (hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) hitResult).getEntity() instanceof LivingEntity target) {
                            //dest = target.position().subtract(new Vec3(0, 0, 1.5).yRot(target.getYRot()));
                            for (int i = 0; i < 8; i++) {
                                dest = target.position().subtract(new Vec3(0, 0, 1.5).yRot(-(target.getYRot() + i * 45) * Mth.DEG_TO_RAD));
                                if (level.getBlockState(BlockPos.containing(dest).above()).isAir())
                                    break;

                            }
                            player.teleportTo(dest.x, dest.y + 1f, dest.z);
                            player.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition().subtract(0, .15, 0));
                        } else {
                            dest = TeleportSpell.findTeleportLocation(level, player, 10);
                            player.teleportTo(dest.x, dest.y, dest.z);

                        }
                    }
                    player.resetFallDistance();
                    level.playSound(null, dest.x, dest.y, dest.z, Optional.of(SoundRegistry.BLOOD_STEP.get()).get(), SoundSource.NEUTRAL, 1f, 1f);

                    //Invis take 1 tick to set in
                    player.setInvisible(true);
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.TRUE_INVISIBILITY.get(), 10, 0, false, false, true));

                List<Vec3> vl = PathGenerator.generatePath(new Vec3(dest.x, dest.y, dest.z), first);
                for (Vec3 v : vl) {

                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleRegistry.BLOOD_PARTICLE.get(), v.x(), v.y(), v.z(), 5, 0.3, 0.3, 0.3, 0.1);
                        {
                            final Vec3 _center = v;
                            List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                            for (Entity entityiterator : _entfound) {
                                if (isSameTeam(entityiterator, player))continue;

                                if (entityiterator instanceof FrozenHumanoid)continue;
                                if (entityiterator != player) {
                                    if (entityiterator instanceof LivingEntity living) {
                                        entityiterator.invulnerableTime = 0;
                                        player.heal(0.8F);
                                        entityiterator.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), player), (float) ((float) (getAttackDamage(player) * 0.6 + 2)*0.2));
                                        entityiterator.setDeltaMovement(new Vec3(0, 0.1, 0));
                                        TheDestroyer.addEffectB(living,1);
                                    }

                                }
                            }
                        }
                    }
                }
            }
            player.getPersistentData().putInt("DefeatedBodyTechniqueP",defeatedBodyTechniqueP-1);
        }
        if (isInCooldown(player))return;
        if (player.getPersistentData().getInt("DefeatedBodyTechniqueP") !=0) {
            if (!player.getCooldowns().isOnCooldown(ItemRegistry.KEEPER_FLAMBERGE.get()))
                player.getCooldowns().addCooldown(ItemRegistry.KEEPER_FLAMBERGE.get(), 6);
        }
        player.getPersistentData().putInt("DefeatedBodyTechniqueP",3);

        super.use(player);
        this.use(player);

    }
}
