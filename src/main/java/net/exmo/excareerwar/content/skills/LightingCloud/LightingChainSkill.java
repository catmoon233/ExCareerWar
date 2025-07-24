package net.exmo.excareerwar.content.skills.LightingCloud;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static net.exmo.excareerwar.content.skills.LightingCloud.LightingA.canHit;

@AutoInit
public class LightingChainSkill extends CareerSkill {
    public LightingChainSkill() {
        super(SpellRegistry.SHOCKWAVE_SPELL.get(), 4);
        this.CoolDown = 20*5;
        this.isSelfCooldown = true;
    }

    @Override
    public void use(Player player) {

        if (isInCooldown(player))return;
        {
            CareerSkill skill1 = SkillHandle.getSkill(SpellRegistry.
                    ASCENSION_SPELL.get().getSpellName());
            Integer skillV = SkillHandle.getSkillV(player, skill1.LocalName);
            SkillHandle.ChangeSkillV(player, skill1.LocalName, Math.max(skillV - 40, 0));
        }
        if (player.hasEffect(MobEffectRegistry.ASCENSION.get())) {
            Level level = player.level();
            Vec3 pos = player.position();

            level.getEntities(player, AABB.ofSize(pos, (double) (6 * 2.0F), (double) (10 * 2.0F), (double) (6 * 2.0F)), (target) -> canHit(player, target)).forEach((target) -> {
                        if ((target instanceof LivingEntity a)) {
                            a.invulnerableTime = 0;
                            DamageSources.applyDamage(target, 4, new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player));
                            SkillHandle.vmove2(a, 1, 0);
                            LightningBolt lightningBolt = (LightningBolt) EntityType.LIGHTNING_BOLT.create(level);
                            lightningBolt.setVisualOnly(true);
                            lightningBolt.setDamage(0.0F);
                            lightningBolt.setPos(a.position());
                            level.addFreshEntity(lightningBolt);
                            a.addEffect(new MobEffectInstance(CareerWarModMobEffects.VULNERABILITY.get(), 60, 0, false, false, false));
                            a.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, false, false, false));

                        }
        });
        }

        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 2, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 0, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffectRegistry.OAKSKIN.get(), 80, 1, false, false, false));
        super.use(player);
    }
}
