
package net.exmo.excareerwar.content.skills.WindRanger;

import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

@AutoInit
public class ArrowLead extends CareerSkill {
	public ArrowLead() {
		super("arrow_lead");
		this.LocalDescription = "arrow_lead_d";
		this.CoolDown = 40*4;
		this.Icon = Items.ARROW;
	}
	public void use(Player player) {
		if (!this.setCoolDown(player))return;
		if (player.level() instanceof ServerLevel l) {
			{
				final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
				List<Entity> _entfound = l.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
				for (Entity entityiterator : _entfound) {
					if (entityiterator instanceof LivingEntity le) {
						le.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, 0));
						le.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3));
					if (entityiterator != player) {
							player.heal(1);
						le.invulnerableTime =0;
							entityiterator.hurt(new DamageSource(l.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 4);
						}
					}
				}
			}
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);

			SkillHandle.sendParticleCircle(l, player, ParticleTypes.END_ROD, 3, 30);
			player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 1));
			{
				Entity _ent = player;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "clear @s arrow");
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "give @s minecraft:tipped_arrow{Bind:true,CustomPotionEffects:[{Id:2,Amplifier:1,Duration:100}]} 1");
				}
			}
		}
	}

}
