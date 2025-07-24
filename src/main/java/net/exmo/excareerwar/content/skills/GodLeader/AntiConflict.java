
package net.exmo.excareerwar.content.skills.GodLeader;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;

import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.excareerwar.util.EntityPointer;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.Map;

@AutoInit
public class AntiConflict extends CareerSkill {
	public AntiConflict() {
		super(SpellRegistry.POISON_ARROW_SPELL.get(),6);
		this.LocalName= "AntiConflict";
		this.LocalDescription = "AntiConflict_d";
		this.CoolDown = 14*40;
		this.Icon = Items.ARROW;
	}
	public void use(Player player) {

		if (isInCooldown(player))return;
		if (player.level() instanceof ServerLevel l) {
			var target = EntityPointer.findTargetedEntity(player, 15);
			if (target.isPresent()){
				var target1 = target.get();
				if (isSameTeam(target1, player))return;

				player.teleportTo(target1.getX(), target1.getY(), target1.getZ());
				target1.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 15, 255, false, false, false));
				player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 2, false, false, false));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 15, 255, false, false, false));
				target1.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 15, 255, false, false, false));
				Excareerwar.queueServerWork(3,()->{
					SkillHandle.sendDashMessage(player, 0.4,-2.8);
					target1.hurt(new DamageSource(l.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), (float) (getAttackDamage(player)*1.6+3));

				});

			}else {
				player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

					Map<String, Integer> map = capability.playercooldown;
					if (map.containsKey(this.LocalName))map.remove(this.LocalName);
					map.put(this.LocalName, (0));
					capability.playercooldown = map;
					capability.syncPlayerVariables(player);
				});
			}
		}
		super.use(player);
	}

}
