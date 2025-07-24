
package net.exmo.excareerwar.entity;


import net.exmo.excareerwar.init.CareerWarModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class VerticalStringEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(Blocks.AIR);

	public VerticalStringEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(net.exmo.excareerwar.init.CareerWarModEntities.VERTICAL_STRING.get(), world);
	}

	public VerticalStringEntity(EntityType<? extends VerticalStringEntity> type, Level world) {
		super(type, world);
	}

	public VerticalStringEntity(EntityType<? extends VerticalStringEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
	}

	public VerticalStringEntity(EntityType<? extends VerticalStringEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
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
	public void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		Entity entity = entityHitResult.getEntity();
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 40, 1));
		if (entity instanceof LivingEntity _entity)
			entity.invulnerableTime = 0;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level() instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.END_ROD, (this.getX()), this.getY(), this.getZ(), 4, 0, 0, 0, 0);
		this.setNoGravity(true);		if (this.inGround)
			this.discard();
	}

	public static VerticalStringEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 2f, 2, 0);
	}

	public static VerticalStringEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		VerticalStringEntity entityarrow = new VerticalStringEntity(CareerWarModEntities.VERTICAL_STRING.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.spider.ambient")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static VerticalStringEntity shoot(LivingEntity entity, LivingEntity target) {
		VerticalStringEntity entityarrow = new VerticalStringEntity(CareerWarModEntities.VERTICAL_STRING.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 2f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(2);
		entityarrow.setKnockback(0);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.spider.ambient")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
