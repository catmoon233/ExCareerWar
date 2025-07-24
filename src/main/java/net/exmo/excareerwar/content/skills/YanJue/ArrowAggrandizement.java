package net.exmo.excareerwar.content.skills.YanJue;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
@AutoInit
public class ArrowAggrandizement extends CareerSkill {
    public ArrowAggrandizement() {
        super("ArrowAggrandizement");
        this.IconTexture = new ResourceLocation("textures/mob_effect/jump_boost.png");
        this.CoolDown=3;
        this.isSelfCooldown=true;
        this.isPassive=true;
    }

    @Override
    public void use(Player player) {

    }

}
