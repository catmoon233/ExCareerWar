package net.exmo.excareerwar.content.skills.Garen;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.content.events.SwingEvent;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static net.minecraftforge.fml.common.Mod.*;

@AutoInit
@EventBusSubscriber
class Judgment extends CareerSkill {

    public Judgment() {
        super("Judgment");
        this.Icon = Items.GOLDEN_SWORD;
        this.CoolDown = 12 * 20; // 45 seconds in ticks
        this.LocalDescription = "对敌人造成高额伤害并减速";
    }
    @SubscribeEvent
    public static void atTick(TickEvent.PlayerTickEvent e){
        Player player = e.player;
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (capability.playercooldown.containsKey("JUDGMENT")&& Integer.valueOf(capability.playercooldown.get("JUDGMENT"))>0) {
                if (player.level() instanceof ServerLevel level){
                    SkillHandle.sendParticleCircle(level,player,ParticleTypes.GLOW_SQUID_INK,3,2);
                }
            }
        });

    }
    @SubscribeEvent
    public static void atSwing(SwingEvent e) {
        if (e.getEntity() instanceof Player player) {
            player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                Map<String, Integer> playercooldown = capability.playercooldown;
                if (playercooldown.containsKey("JUDGMENT") && (playercooldown.get("JUDGMENT")) > 0) {
                    Level level = player.level();

                    final Vec3 center = player.position();
                    List<Entity> entfound = level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(10 / 2d), a -> true)
                            .stream()
                            .sorted(Comparator.comparingDouble(entcnd -> entcnd.distanceToSqr(center)))
                            .toList();

                    for (Entity entityiterator : entfound) {
                        if (entityiterator != player && entityiterator instanceof LivingEntity entity) {
                            playercooldown.put("JUDGMENT", 0);
                            if (playercooldown.containsKey("DEMACIANJUSTICE") && playercooldown.get("DEMACIANJUSTICE") >0) {
                                entityiterator.level().explode(player, entity.getX(), entity.getY(), entity.getZ(),1,false, Level.ExplosionInteraction.NONE);
                            }
                            entity.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 7f);
                            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 2)); // Slowdown effect for 5 seconds
                            if (entity.level() instanceof ServerLevel level1){
                                level1.sendParticles(ParticleTypes.SWEEP_ATTACK, entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0, 0, 0);
                            }
                        }
                    }
                }
                capability.syncPlayerVariables(player);
            });
        }
    }

    @Override
    public void use(Player player) {
        if (!this.setCoolDown(player)) return;
        if (!player.level().isClientSide) {
            SpellRegistry.GUST_SPELL.get().castSpell(player.level(),1, (ServerPlayer) player, CastSource.COMMAND, false);
        }
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3 * 20, 1)); // Increase attack damage for 5 seconds
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 3 * 20, 0)); // Increase attack damage for 5 seconds

        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            SkillHandle.ChangeSkillV(player, "JUDGMENT", 100);
            capability.syncPlayerVariables(player);
        });
    }
}
