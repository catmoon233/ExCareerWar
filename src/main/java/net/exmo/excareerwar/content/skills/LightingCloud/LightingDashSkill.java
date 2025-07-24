package net.exmo.excareerwar.content.skills.LightingCloud;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.LightningStrike;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.excareerwar.util.EntityPointer;
import net.exmo.excareerwar.util.PathGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static io.redspace.ironsspellbooks.effect.ThunderstormEffect.getDamageFromAmplifier;
import static net.exmo.excareerwar.content.skills.LightingCloud.LightingA.canHit;

@AutoInit
public class LightingDashSkill extends CareerSkill {
    public LightingDashSkill() {
        super(SpellRegistry.CHAIN_LIGHTNING_SPELL.get(), 1);
        this.CoolDown = (int) (4*20);
    }

    @Override
    public void use(Player player) {

        if (isInCooldown(player))return;
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10, 0, false, false, false));
        if (player.level() instanceof ServerLevel l) {
            player.heal(1.25f);
            ServerLevel level = (ServerLevel) player.level();
            float damage = (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue()*0.5f;
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 0, false, false, false));
            SkillHandle.sendDashMessage(player, -0.34, 0.7);
            CareerSkill skill = SkillHandle.getSkill(SpellRegistry.SHOCKWAVE_SPELL.get().getSpellName());
            Integer skillV = SkillHandle.getSkillV(player, skill.LocalName);
             SkillHandle.ChangeSkillV(player, skill.LocalName, Math.max(skillV - 25, 0));
            ((ServerPlayer)player).playSound(SoundEvents.TRIDENT_HIT_GROUND);
            var pos = player.position();
            l.sendParticles(ParticleHelper.ELECTRICITY, pos.x, pos.y, pos.z, 10, 0.8, 0.8, 0.8, 0.8);
            level.getEntities(player, AABB.ofSize(pos, (double) (3 * 2.0F), (double) (3 * 2.0F), (double) (3 * 2.0F)), (target) -> canHit(player, target)).forEach((target) -> {
                if ((target instanceof LivingEntity a)) {
                    a.invulnerableTime = 0;
                    DamageSources.applyDamage(target, damage, new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player));
                    a.invulnerableTime = 5;
                    SkillHandle.vmove2(a, 0.3, 0);
                    a.knockback((double)((float)1.2 * 0.5F), (double) Mth.sin(player.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(player.getYRot() * ((float)Math.PI / 180F))));

                }
            });

        }
        CareerWarModVariables.PlayerVariables v = player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CareerWarModVariables.PlayerVariables());

        super.use(player);

        if (player.hasEffect(MobEffectRegistry.ASCENSION.get())){
            Excareerwar.queueServerWork(3,()->{
                var path = PathGenerator.generatePath(player.position(), EntityPointer.raycastForEntity(player.level(), player, 7, false).getLocation());
                for (int i = 0; i < path.size(); i++) {
                    if (i%2!=0)continue;
                    Vec3 a = path.get(i);
                    LightningStrike lightningStrike = new LightningStrike(player.level());
                    lightningStrike.setOwner(player);
                    lightningStrike.setDamage(getDamageFromAmplifier(5, player));
                    lightningStrike.setPos(a);
                    player.level().addFreshEntity(lightningStrike);
                }
            });

            v.playercooldown.put(this.LocalName, (int) (this.CoolDown-2.7*20));
        }else {
            {
                CareerSkill skill1 = SkillHandle.getSkill(SpellRegistry.
                        ASCENSION_SPELL.get().getSpellName());
                Integer skillV = SkillHandle.getSkillV(player, skill1.LocalName);
                SkillHandle.ChangeSkillV(player, skill1.LocalName, Math.max(skillV - 40, 0));
            }
        }
    }
}
