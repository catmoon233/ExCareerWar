
package net.exmo.excareerwar.content.skills.Yi;

import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
@AutoInit
@Mod.EventBusSubscriber
public class SensitiveWaterWhirl extends CareerSkill {
	public SensitiveWaterWhirl() {
		super("SensitiveWaterWhirl");
		this.CoolDown = 200;
		this.Icon = Items.DIAMOND_SWORD;
		this.LocalDescription = "SensitiveWaterWhirl_d";
	}
@SubscribeEvent
public static void atLiving (LivingEvent.LivingTickEvent e){
		if (e.getEntity() instanceof ArmorStand entity){
			if (entity.getPersistentData().getInt("KilledTime1")>0){
				if (entity.getPersistentData().getInt("KilledTime1")==1)entity.remove(Entity.RemovalReason.DISCARDED);
				entity.getPersistentData().putInt("KilledTime1", entity.getPersistentData().getInt("KilledTime1")-1);
				if (entity.getPersistentData().getString("Skill").equals("SensitiveWaterWhirl")){
				SkillHandle.vmove(entity,-0.1,1.2);

				if (entity.level() instanceof ServerLevel level) {
						Player player = null;

						for (Player p : new ArrayList<>(level.players())) {
							if (p.getStringUUID().equals(entity.getPersistentData().getString("Owner"))) {
								player = p;
								break;
							}
						}
						level.sendParticles(ParticleTypes.SQUID_INK, entity.getX(), entity.getY(), entity.getZ(), 10, 0.3, 0.3, 0.3, 0.3);
						final Vec3 _center = entity.position();

						List<Entity> _entfound = entity.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
						for (Entity entityiterator : _entfound) {
							if (entityiterator != entity) {
								if (entityiterator != player) {
									if (isSameTeam(entityiterator, player))continue;

									if (entityiterator instanceof LivingEntity living) {
										Random random = new Random();
										if (player!=null) {
											player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, 3, false, false));
											entityiterator.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), random.nextInt(5) + 1.5F);
										}
										if (!entity.getPersistentData().getBoolean("isHit")){
											entity.setXRot(entity.getXRot()*-1);
											entity.setYRot(entity.getYRot()*-1);
											entity.getPersistentData().putBoolean("isHit", true);
										}
									}
								}
							}
						}



				}
				}

			}else {
			}
		}

}
	public void use(Player player) {
		if (!this.setCoolDown(player))return;
		if (player.level() instanceof ServerLevel level) {
			//SkillHandle.generateSphereParticles(level, ParticleTypes.FLAME,);
			if (player instanceof ServerPlayer serverPlayer) {
				BlockPos pos1 = serverPlayer.blockPosition();
				BlockPos pos2 = new BlockPos(pos1.getX(), pos1.getY() + 1, pos1.getZ());

				Level world = serverPlayer.level();
				if (world instanceof ServerLevel _level) {
					LivingEntity entityToSpawn = EntityType.ARMOR_STAND.spawn(_level, pos2, MobSpawnType.MOB_SUMMONED);
					if (entityToSpawn != null) {
						entityToSpawn.getPersistentData().putInt("KilledTime1", 80);
						entityToSpawn.getPersistentData().putString("Owner", player.getStringUUID());
						entityToSpawn.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 99999999, 1, false, false));
						entityToSpawn.getPersistentData().putString("Skill", "SensitiveWaterWhirl");
						entityToSpawn.setInvisible(true);
						LivingEntity _ent = (LivingEntity) entityToSpawn;
						_ent.setYRot(player.getYRot());
						_ent.setXRot(player.getXRot());
						_ent.setYBodyRot(_ent.getYRot());
						_ent.setYHeadRot(_ent.getYRot());
						_ent.yRotO = _ent.getYRot();
						_ent.xRotO = _ent.getXRot();
						_ent.yBodyRotO = _ent.getYRot();
						_ent.yHeadRotO = _ent.getYRot();
						SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.TOTEM_OF_UNDYING, 1, 10);

					}

				}
			}
		}
	}
}
