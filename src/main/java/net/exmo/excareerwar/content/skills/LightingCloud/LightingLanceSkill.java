package net.exmo.excareerwar.content.skills.LightingCloud;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

@AutoInit
public class LightingLanceSkill extends CareerSkill {
    public LightingLanceSkill() {
        super(SpellRegistry.LIGHTNING_LANCE_SPELL.get(), 4);
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
        player.heal(4);
        player.addEffect(new MobEffectInstance(MobEffectRegistry.OAKSKIN.get(), 80, 1, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 2, false, false, false));
        super.use(player);
    }
}
