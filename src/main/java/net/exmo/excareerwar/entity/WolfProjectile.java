
package net.exmo.excareerwar.entity;


import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModEntities;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class WolfProjectile extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = ItemStack.EMPTY;

	public WolfProjectile(PlayMessages.SpawnEntity packet, Level world) {
		super(CareerWarModEntities.WolfProjectile.get(), world);
	}

	public WolfProjectile(EntityType<? extends WolfProjectile> type, Level world) {
		super(type, world);
	}

	public WolfProjectile(EntityType<? extends WolfProjectile> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
	}

	public WolfProjectile(EntityType<? extends WolfProjectile> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	@Override
	protected void onHitEntity(EntityHitResult p_36757_) {
		super.onHitEntity(p_36757_);
		Entity owner = getOwner();
		Entity entity = p_36757_.getEntity();
		if(entity != owner){
			if (owner !=null){
				if (entity instanceof LivingEntity entity1) {
					if (owner instanceof Player player) {
						if (entity1.isDeadOrDying()){
							SkillHandle.ChangeSkillV(player,"KaiSkill",0);
						}else {
							CareerWarModVariables.PlayerVariables playerVariables = player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElseGet(null);
							playerVariables.HeavenlyEyeUUID = entity1.getStringUUID();
							playerVariables.syncPlayerVariables(player);
							player.addEffect(new MobEffectInstance(CareerWarModMobEffects.HeavenlyEye.get(), 20 * 7, 2, false, false, false));
							player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 15 * 4, 1, false, false, false));
						}
					}
				}
			}
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		return PROJECTILE_ITEM;
	}

	@Override
	protected ItemStack getPickupItem() {
		return PROJECTILE_ITEM;
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.inGround)
			this.discard();
	}

	public static WolfProjectile shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 1f, 5, 1);
	}

	public static WolfProjectile shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		WolfProjectile entityarrow = new WolfProjectile(CareerWarModEntities.WolfProjectile.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.crossbow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static WolfProjectile shoot(LivingEntity entity, LivingEntity target) {
		WolfProjectile entityarrow = new WolfProjectile(CareerWarModEntities.WolfProjectile.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(5);
		entityarrow.setKnockback(5);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.crossbow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
