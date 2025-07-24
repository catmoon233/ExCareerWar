
package net.exmo.excareerwar.content.skills.WindRanger;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
@AutoInit
public class Windrun extends CareerSkill {
	public Windrun() {
		super("wind_run");
		this.Icon = Items.QUARTZ;
		this.CoolDown = (int) (40 *17);
		this.LocalDescription = "wind_run_d";
	}
	public void use(Player player) {
		if (this.setCoolDown(player)){
			player.swing(player.getUsedItemHand());
			SkillHandle.ChangeSkillV(player,"fx",2);
			player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 4));
		}
	}

}
