
package net.exmo.excareerwar.content.skills.Yi;

import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.content.events.SwingEvent;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;
@AutoInit
@Mod.EventBusSubscriber
public class AccumulatePowerChop extends CareerSkill {

	public AccumulatePowerChop() {
		super("AccumulatePowerChop");
		this.Icon = Items.QUARTZ;
		this.CoolDown = 140;
		this.LocalDescription = "AccumulatePowerChop_d";
	}
	@SubscribeEvent
	public static void atSwing (SwingEvent e){
		if (e.getEntity() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) e.getEntity();
			player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					if (capability.playercooldown.containsKey("XLZ")&& Integer.valueOf(capability.playercooldown.get("XLZ"))>0) {
						Level level = player.level();

						final Vec3 _center = player.position();
						List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10 / 2d), a -> true)
								.stream()
								.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
								.toList();

						boolean found = false; // 添加标志变量

						for (Entity entityiterator : _entfound) {
							if (isSameTeam(entityiterator, player))continue;

							if (entityiterator != player && entityiterator instanceof LivingEntity entity) {
								if (entity.getHealth()<=0)return;
								capability.playercooldown.replace("XLZ", 0) ;
								found = true; // 标记为已找到
								Vec3 position = entity.position();
								Vec3 de = position.subtract(player.position()).normalize();
								Vec3 vmove = de.scale(2);
								player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 2));
								player.move(MoverType.SELF, vmove);
								player.connection
										.send(new ClientboundSetEntityMotionPacket(player.getId(),vmove));
								player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 0));
								entityiterator.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC),player), 6f);
							if 	(Integer.valueOf(SkillHandle.getSkillV(player,"Omnipotent_Explosion1"))>0){
								((LivingEntity) entityiterator).invulnerableTime =0;
								player.attack(entityiterator);
							}
								entityiterator.invulnerableTime = 0;
								break; // 找到最近的一个符合条件的实体后退出循环
							}
						}
					}
				capability.syncPlayerVariables(player);
			});
		}
	}
	@SubscribeEvent
	public static void atTick(TickEvent.PlayerTickEvent e){
			Player player = e.player;
			player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				if (capability.playercooldown.containsKey("XLZ")&& Integer.valueOf(capability.playercooldown.get("XLZ"))>0) {
					if (player.level() instanceof ServerLevel level){
						level.sendParticles(ParticleTypes.CRIT, player.getX(), player.getY()+1, player.getZ(), 10, 0.1, 0.1, 0.1, 0.1);
					}
				}
			});

	}

	public void use(Player player) {
		if (!this.setCoolDown(player))return;
		player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
			SkillHandle.ChangeSkillV(player,"XLZ",100);
			capability.syncPlayerVariables(player);
		});
	}
}
