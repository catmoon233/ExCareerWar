package net.exmo.excareerwar.content.skills.LightingCloud;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.ball_lightning.BallLightning;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.content.events.SwingEvent;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.world.item.enchantment.ThornsEnchantment.getDamage;

@Mod.EventBusSubscriber
@AutoInit
public class LightingA extends CareerSkill {
    public LightingA() {
        super(SpellRegistry.LIGHTNING_BOLT_SPELL.get(), 1);
        this.CoolDown=5*20;
    }
    @SubscribeEvent
    public static void atTick(SwingEvent e){
        LivingEntity living = e.getEntity();
        if (!(living instanceof Player player)) return;
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (capability.career.equals("ThunderCloud")) {
                AbstractSpell abstractSpell = SpellRegistry.LIGHTNING_BOLT_SPELL.get();
                CareerSkill skill = SkillHandle.getSkill(abstractSpell.getSpellName());
                Level level = player.level();
                if (player.isSpectator())return;
                if (level.isClientSide) return;
                if (!skill.setCoolDown((Player) player)) return;

                player.swing(player.getUsedItemHand());
                HitResult result = Utils.raycastForEntity(level, player, 15F, true, 1.0F);
                Vec3 pos = result.getLocation();
                if (result.getType() == HitResult.Type.ENTITY) {
                    pos = ((EntityHitResult) result).getEntity().position();
                } else {
                    pos = Utils.moveToRelativeGroundLevel(level, pos, 10);
                }
                if (player.hasEffect(MobEffectRegistry.ASCENSION.get())) {
                    capability.playercooldown.put(skill.LocalName, (int) (skill.CoolDown - 3.5 * 20));
//                    SkillHandle.sendDashMessage(player, -0.2, 0.5);
//                    BallLightning balllightning = new BallLightning(level, player);
//                    balllightning.setPos(player.position().add(0, player.getEyeHeight() - balllightning.getBoundingBox().getYsize() * .5f, 0));
//                    balllightning.shoot(player.getLookAngle());
//                    balllightning.setDamage(2.5f);
//                    level.addFreshEntity(balllightning);
                }else {
                    {
                        CareerSkill skill1 = SkillHandle.getSkill(SpellRegistry.
                                ASCENSION_SPELL.get().getSpellName());
                        Integer skillV = SkillHandle.getSkillV(player, skill1.LocalName);
                        SkillHandle.ChangeSkillV(player, skill1.LocalName, Math.max(skillV - 40, 0));
                    }
                }
                {
                CareerSkill skill1 = SkillHandle.getSkill(SpellRegistry.
                        LIGHTNING_LANCE_SPELL.get().getSpellName());
                Integer skillV = SkillHandle.getSkillV(player, skill1.LocalName);
                SkillHandle.ChangeSkillV(player, skill1.LocalName, Math.max(skillV - 20, 0));
                }

                LightningBolt lightningBolt = (LightningBolt) EntityType.LIGHTNING_BOLT.create(level);
                lightningBolt.setVisualOnly(true);
                lightningBolt.setDamage(0.0F);
                lightningBolt.setPos(pos);
                level.addFreshEntity(lightningBolt);
                float radius = 4.0F;
                float damage = 2.5F;
                Vec3 finalPos = pos;
                level.getEntities(player, AABB.ofSize(pos, (double)(radius * 2.0F), (double)(radius * 2.0F), (double)(radius * 2.0F)), (target) -> canHit(player, target)).forEach((target) -> {
                    double distance = target.distanceToSqr(finalPos);
                    if (distance < (double)(radius * radius) && Utils.hasLineOfSight(level, finalPos.add((double)0.0F, (double)2.0F, (double)0.0F), target.getBoundingBox().getCenter(), true)) {
                        float finalDamage = (float)((double)damage * ((double)1.0F - distance / (double)(radius * radius)));
                        DamageSources.applyDamage(target, finalDamage, abstractSpell.getDamageSource(lightningBolt, player));
                        if (target instanceof Creeper) {
                            Creeper creeper = (Creeper)target;
                            creeper.thunderHit((ServerLevel)level, lightningBolt);
                        }
                    }

                });
            }
        });
    }
    @Override
    public void use(Player player) {

    }
    public static boolean canHit(Entity owner, Entity target) {
        return target != owner && target.isAlive() && target.isPickable() && !target.isSpectator();
    }
}
