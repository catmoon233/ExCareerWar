package net.exmo.excareerwar.content.skills.Garen;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@AutoInit
@Mod.EventBusSubscriber
class DemacianJustice extends CareerSkill {

    public DemacianJustice() {
        super("DemacianJustice");
        this.Icon = Items.DIAMOND_SWORD;
        this.CoolDown = 18 * 40; // 60 seconds in ticks
        this.LocalDescription = "对周围敌人造成范围伤害并增加自身攻击力";
    }

    @SubscribeEvent
    public static void atTick(TickEvent.PlayerTickEvent e) {
        Player player = e.player;
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (capability.playercooldown.containsKey("DEMACIANJUSTICE") && Integer.valueOf(capability.playercooldown.get("DEMACIANJUSTICE")) > 0) {
                Level level = player.level();
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.ENCHANT , player.getX(), player.getY(), player.getZ(), 5, 0.1, 2, 0.1, 2);
                }
                capability.syncPlayerVariables(player);
            }
        });
    }

    @Override
    public void use(Player player) {
        if (!this.setCoolDown(player)) return;
        if (!player.level().isClientSide) {
            SpellRegistry.SUMMON_HORSE_SPELL.get().attemptInitiateCast(ItemStack.EMPTY, 1, player.level(), (ServerPlayer) player, CastSource.COMMAND, false, "command");
        }
        player.addEffect(new MobEffectInstance(MobEffectRegistry.ANGEL_WINGS.get(), 30 * 20, 1));
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            SkillHandle.ChangeSkillV(player, "DEMACIANJUSTICE", 100);
            capability.syncPlayerVariables(player);
        });
        Level level = player.level();
        final Vec3 center = player.position();
        List<Entity> entfound = level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(8 / 2d), a -> true)
                .stream()
                .filter(entityiterator -> entityiterator != player && entityiterator instanceof LivingEntity && !isSameTeam(entityiterator, player))
                .toList();

        for (Entity entityiterator : entfound) {
            if (isSameTeam(entityiterator, player))continue;

            ((LivingEntity) entityiterator).hurt(new DamageSource(player.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 10f);
        }

        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5 * 20, 1)); // Increase attack damage for 5 seconds
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 5 * 20, 0)); // Increase attack damage for 5 seconds
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 8 * 20, 1)); // Increase attack damage for 5 seconds

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY(), player.getZ(), 10, 0.5, 0.5, 0.5, 1);
        }
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 8 * 20, 1));
    }
}