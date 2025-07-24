
package net.exmo.excareerwar.content.skills.Puppeteer;

import com.mojang.util.UUIDTypeAdapter;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.excareerwar.util.PathGenerator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
@AutoInit
public class SoulFlash extends CareerSkill {
	public SoulFlash() {
		super("Soul_Flash");
		this.CoolDown = (int) (9*20);
		this.Icon = Items.LEATHER_BOOTS;
		this.LocalDescription = "Soul_Flash_d";
	}
	public  void use(Player player) {
		if (!this.setCoolDown(player))return;
		Level world = player.level();

		if(!player.getPersistentData().getString("Puppets").equals("")) {
			SkillHandle.sendDashMessage(player,0.1,2.2);

			if (new Object() {
				Entity getEntity(String uuid) {
					Entity _uuidentity = null;
					if (world instanceof ServerLevel _server) {
						try {
							_uuidentity = _server.getEntity(UUIDTypeAdapter.fromString(uuid));
						} catch (IllegalArgumentException e) {
						}
					}
					return _uuidentity;
				}
			}.getEntity(player.getPersistentData().getString("Puppets")) instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
				player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2));

				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 2));
				player.heal(3);
				if (player.level() instanceof  ServerLevel serverLevel) SkillHandle.sendParticleCircle(serverLevel, player, ParticleTypes.FLASH, 4, 25);

				if (player.level() instanceof ServerLevel serverLevel){
					serverLevel.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(), 4, 0.3, 0.3, 0.3, 0.1);
					{
						final Vec3 _center = player.position();
						List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
						for (Entity entityiterator : _entfound) {
							if (isSameTeam(entityiterator, player))continue;

							if (entityiterator != player) {
								if (entityiterator!=_entity) {
									if (entityiterator instanceof LivingEntity) {
										player.heal(1f);
										entityiterator.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 3);
										entityiterator.setDeltaMovement(new Vec3(0, 1, 0));
									}
								}
							}
						}
					}
				}
				if (_entity.level() instanceof ServerLevel serverLevel){
					serverLevel.sendParticles(ParticleTypes.CLOUD, _entity.getX(), _entity.getY(), _entity.getZ(), 4, 0.3, 0.3, 0.3, 0.1);
					{
						final Vec3 _center = player.position();
						List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
						for (Entity entityiterator : _entfound) {
							if (isSameTeam(entityiterator, player))continue;

							if (entityiterator != player) {
								if (entityiterator!=_entity) {
									if (entityiterator instanceof LivingEntity) {
										player.heal(1);
										entityiterator.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 3);
										entityiterator.setDeltaMovement(new Vec3(0, 1, 0));
									}
								}
							}
						}
					}
				}


				List<Vec3> vl = PathGenerator.generatePath(Vec3.atCenterOf(player.blockPosition()), Vec3.atCenterOf(_entity.blockPosition()));
				CareerWarModVariables.PlayerVariables v1 = player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(null);
				int cooln = (v1.playercooldown.get(this.LocalName));
				for (Vec3 v : vl){
					if (player.level() instanceof ServerLevel serverLevel){
						serverLevel.sendParticles(ParticleTypes.CLOUD, v.x(), v.y(), v.z(), 4, 0.3, 0.3, 0.3, 0.1);
						{
							final Vec3 _center = v;
							List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (entityiterator != player) {
									if (entityiterator!=_entity) {
										if (entityiterator instanceof LivingEntity) {
											cooln -=40;
											player.heal(1);
											entityiterator.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 5);
											entityiterator.setDeltaMovement(new Vec3(0, 0.7, 0));
										}
									}
								}
							}
						}
					}
				}
				_entity.teleportTo(player.getX(), player.getY(), player.getZ());
				final  int  c= cooln;
				player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

					Map<String, Integer> map = capability.playercooldown;
					if (map.containsKey(this.LocalName))map.remove(this.LocalName);
					map.put(this.LocalName, (Math.max(c, 0)));
					capability.playercooldown = map;
					capability.syncPlayerVariables(player);
				});
			}
		}else {
			if ( !player.level().isClientSide())
				player.displayClientMessage(Component.literal("傀儡不存在"), true);
			player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

				Map<String, Integer> map = capability.playercooldown;
				if (map.containsKey(this.LocalName))map.remove(this.LocalName);
				map.put(this.LocalName, (10));
				capability.playercooldown = map;
				capability.syncPlayerVariables(player);
			});
		}
	}
}
