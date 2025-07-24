package net.exmo.excareerwar.content.skills.Garen;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.entity.mobs.SummonedHorse;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.content.events.SwingEvent;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.excareerwar.util.EntityPointer;
import net.exmo.excareerwar.util.PathGenerator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AutoInit
@Mod.EventBusSubscriber
class BladeRush extends CareerSkill {

    public BladeRush() {
        super("BladeRush");
        this.Icon = Items.IRON_SWORD;
        this.CoolDown = 7 * sendCondTick; // 30 seconds in ticks
        this.LocalDescription = "快速冲向目标并造成伤害";
    }
    @SubscribeEvent
    public static void atTick(TickEvent.PlayerTickEvent e){
        Player player = e.player;
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (capability.playercooldown.containsKey("BLADERUSH")&& capability.playercooldown.get("BLADERUSH") >0) {
                if (player.level() instanceof ServerLevel level){
                    SkillHandle.sendParticleCircle(level,player,ParticleTypes.END_ROD,2,2);
                }
            }
        });

    }
    @SubscribeEvent
    public static void atSwing(SwingEvent e) {
        if (e.getEntity() instanceof Player player) {
            player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                if (capability.playercooldown.containsKey("BLADERUSH") && (capability.playercooldown.get("BLADERUSH")) > 0) {
                    Level level = player.level();
                    LivingEntity entityiterator = EntityPointer.raycastForEntityTo(player.level(), player, 7, true);
                    if (entityiterator == null) {
                        Optional<LivingEntity> targetedEntity = EntityPointer.findTargetedEntity(player, 10);
                        if (targetedEntity.isEmpty()) return;
                        entityiterator = targetedEntity.get();
                    }
                    int aHorse = 2;
                    if (entityiterator != null ) {
                    if (entityiterator instanceof SummonedHorse horse){
                        SkillHandle.vmove(horse,-0.8,1);
                        horse.addEffect(new MobEffectInstance(MobEffectRegistry.EVASION.get(), 30, 1));
                        horse.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 50, 1));
                     //   horse.addEffect(new MobEffectInstance(MobEffectRegistry.BURNING_DASH.get(), 20, 2));
                        Level level1 = horse.level();
                        if( level1 instanceof ServerLevel serverLevel){
                           MagicData magicData = MagicData.getPlayerMagicData(horse);
                            AbstractSpell spell1 = SpellRegistry.ACID_ORB_SPELL.get();
                            if (!spell1.checkPreCastConditions(level1, 1, horse, magicData)) {
                                return ;
                            }

                            spell1.onCast(level1, 1, horse, CastSource.COMMAND, magicData);
                            spell1.onServerCastComplete(level1, 1, horse, magicData, false);
                           AbstractSpell spell = SpellRegistry.TELEPORT_SPELL.get();
                           if (!spell.checkPreCastConditions(level1, 1, horse, magicData)) {
                               return ;
                           }

                           spell.onCast(level1, 1, horse, CastSource.COMMAND, magicData);
                           spell.onServerCastComplete(level1, 1, horse, magicData, false);

                       }
                        aHorse = 4;
                    }
                    List<Vec3> vl = PathGenerator.generatePath(Vec3.atCenterOf(player.blockPosition()), (entityiterator.position()));
                    for (Vec3 v : vl){
                        if (player.level() instanceof ServerLevel serverLevel){
                            serverLevel.sendParticles(ParticleTypes.SMOKE, v.x(), v.y(), v.z(), 4, 0.3, 0.3, 0.3, 0.1);
                            {
                                final Vec3 _center = v;
                                List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(aHorse / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                                for (Entity entityiterator1 : _entfound) {
                                    if (entityiterator1 != player) {
                                        if (isSameTeam(entityiterator, player))continue;

                                        if (entityiterator1 instanceof LivingEntity) {
                                            player.heal(0.5f);
                                            entityiterator1.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 2+aHorse);
                                          if (!(entityiterator instanceof SummonedHorse))
                                            entityiterator1.setDeltaMovement(new Vec3(0,0.65,0));
                                        }
                                    }
                                }
                            }
                        }
                    }
                            capability.playercooldown.replace("BLADERUSH", 0);
                            player.teleportTo(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ());
//                            if (!player.level().isClientSide)
//                                SpellRegistry.HEAL_SPELL.get().attemptInitiateCast(ItemStack.EMPTY, 1, player.level(), (ServerPlayer) player, CastSource.COMMAND, false, "command");
                            player.heal(4.5f);
                            entityiterator.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 8f);
                            entityiterator.setDeltaMovement(0,1,0);
                            if (entityiterator.level() instanceof ServerLevel p)p.sendParticles(ParticleTypes.CLOUD, entityiterator.getX(), entityiterator.getY(), entityiterator.getZ(), 15, 0, 2, 0, 3);
                            player.playSound(SoundEvents.ANVIL_PLACE);
                        }

                }
                capability.syncPlayerVariables(player);
            });
        }
    }
    @SubscribeEvent
    public static void ATTACK(LivingAttackEvent e) {
        if (e.getEntity() instanceof Player player){
            player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                if (capability.career!=null) {
                    if (capability.career.equals("Garen")) {
                        player.removeEffect(MobEffectRegistry.REND.get());
                    }
                }
            });
            }
    }
    @Override
    public void use(Player player) {
        if (!this.setCoolDown(player)) return;

        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (!player.level().isClientSide) {
            SpellRegistry.GUIDING_BOLT_SPELL.get().attemptInitiateCast(ItemStack.EMPTY, 7, player.level(), (ServerPlayer) player, CastSource.COMMAND, false, "command");
            if (capability.playercooldown.containsKey("DEMACIANJUSTICE")&&capability.playercooldown.get("DEMACIANJUSTICE") >0){
                Excareerwar.queueServerWork(3, () -> {
                    SpellRegistry.GUIDING_BOLT_SPELL.get().attemptInitiateCast(ItemStack.EMPTY, 7, player.level(), (ServerPlayer) player, CastSource.COMMAND, false, "command");
                });


            }
        }
            SkillHandle.ChangeSkillV(player, "BLADERUSH", 65);
            capability.syncPlayerVariables(player);
        });
    }
}