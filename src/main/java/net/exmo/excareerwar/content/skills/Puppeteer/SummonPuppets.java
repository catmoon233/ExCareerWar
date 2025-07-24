
package net.exmo.excareerwar.content.skills.Puppeteer;

import com.mojang.util.UUIDTypeAdapter;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.entity.PuppetmasterontologyEntity;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.excareerwar.util.PathGenerator;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
@AutoInit
public class SummonPuppets extends CareerSkill {
	public SummonPuppets() {
		super("Summon_Puppets");
		this.CoolDown = 300;
		this.Icon = Items.PLAYER_HEAD;
		this.LocalDescription = "Summon_Puppets_d";
	}
	public void use(Player player) {
		if (!this.setCoolDown(player))return;
		Level world = player.level();
		{
			Entity _ent = player;
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
					player.getPersistentData().putBoolean("Puppetsdie", true);
					_entity.remove(Entity.RemovalReason.KILLED);
					player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2));
					List<Vec3> vl = PathGenerator.generatePath(Vec3.atCenterOf(player.blockPosition()), Vec3.atCenterOf(_entity.blockPosition()));
					for (Vec3 v : vl){
						if (player.level() instanceof ServerLevel serverLevel){
							serverLevel.sendParticles(ParticleTypes.CLOUD, v.x(), v.y(), v.z(), 4, 0.3, 0.3, 0.3, 0.1);
							{
								final Vec3 _center = v;
								List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
								for (Entity entityiterator : _entfound) {
									if (isSameTeam(entityiterator, player))continue;

									if (entityiterator != player) {
										if (entityiterator instanceof LivingEntity) {
											player.heal(1);
											entityiterator.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 5);
											entityiterator.setDeltaMovement(new Vec3(0,1,0));
										}
									}
								}
							}
						}
					}
				}
				SkillHandle.sendDashMessage(player,0,1.6);
				player.getPersistentData().remove("Puppets");

			}else {
				if (player.level() instanceof ServerLevel serverLevel) {
					Entity entityToSpawn = net.exmo.excareerwar.init.CareerWarModEntities.PUPPETMASTERONTOLOGY.get().spawn(serverLevel, BlockPos.containing(player.getX(), player.getY(), player.getZ()), MobSpawnType.MOB_SUMMONED);
					if (entityToSpawn != null) {
						if (entityToSpawn instanceof TamableAnimal _entity) {
							_entity.tame(player);
						}
						entityToSpawn.setCustomName(Component.literal(player.getDisplayName().getString() + "的傀儡"));
						player.getPersistentData().putString("Puppets", entityToSpawn.getUUID().toString());
						((PuppetmasterontologyEntity) entityToSpawn).setOwnerUUID(player.getUUID());

					}

					serverLevel.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(), 20, 0.3, 0.3, 0.3, 0.1);
				}
				SkillHandle.sendDashMessage(player, 0.2, 1.8);
			}
		}
	}
}
