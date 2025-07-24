package net.exmo.excareerwar.content;


import net.exmo.excareerwar.entity.VerticalStringEntity;
import net.exmo.excareerwar.init.CareerWarModEntities;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class ShootString {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (entity instanceof Player _player)
			_player.getCooldowns().addCooldown(itemstack.getItem(), 35);
		if (entity instanceof LivingEntity _entity)
			_entity.swing(InteractionHand.MAIN_HAND, true);
		Entity _ent = entity;
		Level _level = entity.level();
		if (!_level.isClientSide()) {
			_level.playSound(null, _ent.blockPosition(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.bamboo_wood_door.open")), SoundSource.NEUTRAL, 1, 1);
		} else {
			_level.playLocalSound(_ent.blockPosition(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.bamboo_wood_door.open")), SoundSource.NEUTRAL, 1, 1, false);
		}

		if (itemstack.getOrCreateTag().getDouble("times") == 0) {
			{
				Entity _shootFrom = entity;
				Level projectileLevel = _shootFrom.level();
				if (!projectileLevel.isClientSide()) {
					Projectile _entityToSpawn = new Object() {
						public Projectile getArrow(Level level, Entity shooter, float damage, int knockback, byte piercing) {
							AbstractArrow entityToSpawn = new VerticalStringEntity(CareerWarModEntities.VERTICAL_STRING.get(), level);
							entityToSpawn.setOwner(shooter);
							entityToSpawn.setBaseDamage(damage);
							entityToSpawn.setKnockback(knockback);
							entityToSpawn.setSilent(true);
							entityToSpawn.setPierceLevel(piercing);
							entityToSpawn.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
							return entityToSpawn;
						}
					}.getArrow(projectileLevel, entity, 1.2f, 1, (byte) 1);
					_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
					_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 5, 0);
					projectileLevel.addFreshEntity(_entityToSpawn);
				}
			}
			itemstack.getOrCreateTag().putDouble("times", 1);
		} else {
			if (itemstack.getOrCreateTag().getDouble("times") == 1) {
				Player player = (Player) entity;
				for (int index = -1; index < 2; index++) {
					double x = player.getX() + (Math.cos(Math.toRadians(player.getYRot())) * 2) * index*0.5;
					double z = player.getZ() + (Math.sin(Math.toRadians(player.getYRot())) * 2) * index*0.5;

					player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 20);
					Entity _shootFrom = player;
					Level projectileLevel = _shootFrom.level();
					for (int i =0 ;i < 2; i++) {
						if (!projectileLevel.isClientSide()) {
							Projectile _entityToSpawn = new Object() {
								public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
									AbstractArrow entityToSpawn = new VerticalStringEntity(CareerWarModEntities.VERTICAL_STRING.get(), level);
									entityToSpawn.setOwner(shooter);
									entityToSpawn.setBaseDamage(damage);
									entityToSpawn.setKnockback(knockback);
									return entityToSpawn;
								}
							}.getArrow(projectileLevel, player, 0.32f, 1);
							_entityToSpawn.setPos(x, _shootFrom.getEyeY() - 0.1, z);
							_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 5, 4);
							projectileLevel.addFreshEntity(_entityToSpawn);
						}
					}
				}
				itemstack.getOrCreateTag().putDouble("times", 2);
			} else {
				if (itemstack.getOrCreateTag().getDouble("times") == 2) {
					{
						Entity _shootFrom = entity;
						Level projectileLevel = _shootFrom.level();
						if (!projectileLevel.isClientSide()) {
							Projectile _entityToSpawn = new Object() {
								public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
									AbstractArrow entityToSpawn = new VerticalStringEntity(CareerWarModEntities.VERTICAL_STRING.get(), level);
									entityToSpawn.setOwner(shooter);
									entityToSpawn.setBaseDamage(damage);
									entityToSpawn.setKnockback(knockback);
									entityToSpawn.setSilent(true);
									entityToSpawn.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
									return entityToSpawn;
								}
							}.getArrow(projectileLevel, entity, 1.2f, 1);
							_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
							_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 5, 2);
							projectileLevel.addFreshEntity(_entityToSpawn);
						}
					}
					{
						Entity _shootFrom = entity;
						Level projectileLevel = _shootFrom.level();
						if (!projectileLevel.isClientSide()) {
							Projectile _entityToSpawn = new Object() {
								public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
									AbstractArrow entityToSpawn = new VerticalStringEntity(CareerWarModEntities.VERTICAL_STRING.get(), level);
									entityToSpawn.setOwner(shooter);
									entityToSpawn.setBaseDamage(damage);
									entityToSpawn.setKnockback(knockback);
									entityToSpawn.setSilent(true);
									entityToSpawn.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
									return entityToSpawn;
								}
							}.getArrow(projectileLevel, entity, 1.35f, 1);
							_entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
							_entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 5, 2);
							projectileLevel.addFreshEntity(_entityToSpawn);
						}
					}
					itemstack.getOrCreateTag().putDouble("times", 0);
				}
			}
		}
		if (!_ent.level().isClientSide() && _ent.getServer() != null) {
			_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
					_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "tp @s ~ ~ ~ ~ ~--15");

		}
	}
}
