package net.exmo.excareerwar.content.skills.LightingCloud;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

@AutoInit
public class LightingSoar extends CareerSkill {
    public LightingSoar() {
        super(SpellRegistry.ASCENSION_SPELL.get(), 4);
        this.CoolDown= 40*40;
        this.firstCooldown = 38*20;
        this.isSelfCooldown = true;
    }

    @Override
    public void use(Player player) {

        if (isInCooldown(player))return;
        {

        CareerSkill skill1 = SkillHandle.getSkill(SpellRegistry.
                LIGHTNING_LANCE_SPELL.get().getSpellName());
            SkillHandle.ChangeSkillV(player, skill1.LocalName, 0);
        }
        {
            CareerSkill skill = SkillHandle.getSkill(SpellRegistry.SHOCKWAVE_SPELL.get().getSpellName());
            SkillHandle.ChangeSkillV(player, skill.LocalName, 0);
        }
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 130, 1, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffectRegistry.CHARGED.get(), 130, 3, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffectRegistry.THUNDERSTORM.get(), 130, 3, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffectRegistry.ASCENSION.get(), 130, 0, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 130, 1, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 130, 1, false, false, false));
        player.addEffect(new MobEffectInstance(CareerWarModMobEffects.FastCastEMobEffect.get(), 130, 6, false, false, false));
        player.addEffect(new MobEffectInstance(CareerWarModMobEffects.SPELL_DAMAGE_BOOST_EFFECT.get(), 130, 1, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 130, 2, false, false, false));
        super.use(player);
   }
}
