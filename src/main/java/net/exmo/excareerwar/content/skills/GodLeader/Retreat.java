
package net.exmo.excareerwar.content.skills.GodLeader;

import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
@AutoInit
public class Retreat  extends CareerSkill {
	public Retreat() {
		super("retreat");
		this.CoolDown = (int) (40 * 12);
		this.Icon = Items.LEATHER_BOOTS;
		this.LocalDescription = "retreat_d";

	}
	public void use(Player player) {
		if(!this.setCoolDown(player))return;

		float yaw = player.getYRot();
		double dx = -Math.sin(Math.toRadians(yaw)) * -1.8;
		double dz = Math.cos(Math.toRadians(yaw)) * -1.8;
		player.setDeltaMovement(dx, 0.75, dz);
		player.heal(8);
		player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 2));
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1));
		player.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 2));
	}

}
