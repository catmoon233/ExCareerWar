package net.exmo.excareerwar.content.skills.Puppeteer;

import com.mojang.util.UUIDTypeAdapter;
import net.exmo.excareerwar.content.CareerSkill;
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
public class SoulShift extends CareerSkill {
	public SoulShift(){
		super("Soul_Shift");
		this.CoolDown = (int) (8*20);
		this.Icon = Items.FEATHER;
		this.LocalDescription = "Soul_Shift_d";
	}
	public void use(Player player) {
		if (!this.setCoolDown(player))return;
		Level world = player.level();
		if(!player.getPersistentData().getString("Puppets").equals("")) {

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
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 35, 0));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 50, 2));
				player.heal(3);
				Vec3 pv = player.blockPosition().getCenter();
				Vec3  kv = _entity.blockPosition().getCenter();
				player.teleportTo(kv.x(), kv.y(), kv.z());
				_entity.teleportTo(pv.x(), pv.y(), pv.z());
				List<Vec3> vl = PathGenerator.generatePath(Vec3.atCenterOf(player.blockPosition()), Vec3.atCenterOf(_entity.blockPosition()));
				for (Vec3 v : vl){

					if (player.level() instanceof ServerLevel serverLevel){
						serverLevel.sendParticles(ParticleTypes.CLOUD, v.x(), v.y(), v.z(), 4, 0.3, 0.3, 0.3, 0.1);
						{
							final Vec3 _center = v;
							List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (isSameTeam(entityiterator, player))continue;

								if (entityiterator != player) {
									if (entityiterator!=_entity) {
										if (entityiterator instanceof LivingEntity) {
											player.heal(1);
											entityiterator.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 5);
											entityiterator.setDeltaMovement(new Vec3(0, 1, 0));
										}
									}
								}
							}
						}
					}
				}
			}
		}else {
			if ( !player.level().isClientSide())
				player.displayClientMessage(Component.literal("傀儡不存在"), true);
			player.getCapability(net.exmo.excareerwar.network.CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

				Map<String, Integer> map = capability.playercooldown;
				if (map.containsKey(this.LocalName))map.remove(this.LocalName);
				map.put(this.LocalName, (10));
				capability.playercooldown = map;
				capability.syncPlayerVariables(player);
			});
		}


	}
}
