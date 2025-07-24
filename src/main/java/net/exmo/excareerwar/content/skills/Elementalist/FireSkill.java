
package net.exmo.excareerwar.content.skills.Elementalist;


import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.content.careers.Elementalist;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

@AutoInit
public class FireSkill extends CareerSkill {
	public FireSkill() {
		super("fire");
		this.CoolDown = 40*4;
		this.Icon = Items.LAVA_BUCKET;
		this.LocalDescription = "fire_d";
	}


	public void use(Player player) {
		if (!this.setCoolDown(player)) return;
		//Elementalist.skillCooldownin(player);
		player.getCooldowns().addCooldown(CareerWarModItems.WOOD_STAFF.get(), 20);
		player.swing(player.getUsedItemHand());
		if (player.hasEffect(CareerWarModMobEffects.SPELL_BURST_E.get())){
			SkillHandle.ChangeSkillV(player,"fire",30);
		}
		Elementalist.magicSet(player,"fire");
		if (player instanceof ServerPlayer player1)
			SkillHandle.generateSemicircleParticles(player1, 3.0f, ParticleTypes.FLAME);
		{

				final Vec3 _center = player.level().clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(2.5)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos().getCenter();
				if (!(player.level() instanceof ServerLevel l))return;
				List<Entity> _entfound = l.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4.5 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
				for (Entity entityiterator : _entfound) {
					if (entityiterator != player) {
						if (entityiterator instanceof LivingEntity) {
							if (isSameTeam(entityiterator, player))continue;

							((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 4, (false), (false)));
							entityiterator.setSecondsOnFire(3);
							entityiterator.invulnerableTime = 0;
							entityiterator.hurt(new DamageSource(l.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 6f);
							int dashDistance = 1;
							float yaw = player.getYRot();
							double dx = -Math.sin(Math.toRadians(yaw)) * dashDistance;
							double dz = Math.cos(Math.toRadians(yaw)) * dashDistance;
							entityiterator.setDeltaMovement(new Vec3(dx, 0.75, dz));

						}
					}
				}

		}
	}
}
