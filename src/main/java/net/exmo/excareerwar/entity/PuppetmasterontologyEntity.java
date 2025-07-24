
package net.exmo.excareerwar.entity;

import net.exmo.excareerwar.init.CareerWarModEntities;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PuppetmasterontologyEntity extends TamableAnimal implements RangedAttackMob {
	public static final EntityDataAccessor<String> DATA_skin = SynchedEntityData.defineId(PuppetmasterontologyEntity.class, EntityDataSerializers.STRING);

	public PuppetmasterontologyEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(CareerWarModEntities.PUPPETMASTERONTOLOGY.get(), world);
	}

	public PuppetmasterontologyEntity(EntityType<PuppetmasterontologyEntity> type, Level world) {
		super(type, world);
		setMaxUpStep(0.6f);
		xpReward = 0;
		setNoAi(false);
		setPersistenceRequired();
	}

	@Override
	public void remove(RemovalReason p_276115_) {
			if (this.level() instanceof  ServerLevel sl){
				for ( ServerBossEvent bossBar : bossBars){
					bossBar.removeAllPlayers();
				}
				bossBars.clear();
				for (Player p : new ArrayList<>(sl.players())) {
					if (getOwnerUUID().equals(p.getUUID())){
						if (p.getPersistentData().getBoolean("Puppetsdie")){
							p.getPersistentData().putBoolean("Puppetsdie",false);
						}else{
							p.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

								Map<String, Integer> map = capability.playercooldown;
								if (map.containsKey("Summon_Puppets") )map.remove("Summon_Puppets");
								map.put("Summon_Puppets", (20*40));
								capability.playercooldown = map;
								capability.syncPlayerVariables(p);
							});
							p.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 3, (false), (false)));
							p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 30, (false), (false)));
						}
						p.getPersistentData().putString("Puppets","");
					}
				}

		}
			super.remove(p_276115_);
	}
	private static List<ServerBossEvent> bossBars = new ArrayList<>();
	@Override
	public void tick() {
		super.tick();


				final Vec3 _center = this.position();
				List<Entity> _entfound = this.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20 / 2d), a -> true)
						.stream()
						.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
						.toList();

				boolean found = false; // 添加标志变量

				for (Entity entityiterator : _entfound) {
					if (entityiterator instanceof Player player) {
						player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							if (capability.career == null) return;
							if (capability.career.equals("Puppeteer")) {
								if (player.getUUID().equals(getOwnerUUID())) {
									if (player instanceof ServerPlayer serverPlayer){
										boolean bossBarVisible = false;
										for (ServerBossEvent bossBar : bossBars) {
											if (bossBar.getPlayers().contains(serverPlayer)) {
												bossBarVisible = true;
												float hp = this.getHealth() / this.getMaxHealth();

													bossBar.setProgress(hp); // 这里只是一个例子，你可以根据需要改变这个值
													bossBar.setVisible(true);



											}
										}
										if (!bossBarVisible) {
											ServerBossEvent e = new ServerBossEvent(Component.literal("\u00a74傀儡生命值"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
											e.addPlayer(serverPlayer);
											bossBars.add(e);

										}
									}
									//	serverPlayer.sendSystemMessage(Component.literal("傀儡血量:" + (int) ((LivingEntity) entityiterator).getHealth() + "/" + (int) ((LivingEntity) entityiterator).getMaxHealth()), true);
									}

						}
					});
				}
			}
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_skin, "Steve");
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new FollowOwnerGoal(this, 1, (float) 20, (float) 30, false));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25, 10, 10f) {
			@Override
			public boolean canContinueToUse() {
				return this.canUse();
			}
		});
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public double getMyRidingOffset() {
		return -0.35D;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("Dataskin", this.entityData.get(DATA_skin));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("Dataskin"))
			this.entityData.set(DATA_skin, compound.getString("Dataskin"));
	}

	@Override
	public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
		ItemStack itemstack = sourceentity.getItemInHand(hand);
		InteractionResult retval = InteractionResult.sidedSuccess(this.level().isClientSide());
		Item item = itemstack.getItem();
		if (itemstack.getItem() instanceof SpawnEggItem) {
			retval = super.mobInteract(sourceentity, hand);
		} else if (this.level().isClientSide()) {
			retval = (this.isTame() && this.isOwnedBy(sourceentity) || this.isFood(itemstack)) ? InteractionResult.sidedSuccess(this.level().isClientSide()) : InteractionResult.PASS;
		} else {
			if (this.isTame()) {
				if (this.isOwnedBy(sourceentity)) {
					if (item.isEdible() && this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
						this.usePlayerItem(sourceentity, hand, itemstack);
						this.heal((float) item.getFoodProperties().getNutrition());
						retval = InteractionResult.sidedSuccess(this.level().isClientSide());
					} else if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
						this.usePlayerItem(sourceentity, hand, itemstack);
						this.heal(4);
						retval = InteractionResult.sidedSuccess(this.level().isClientSide());
					} else {
						retval = super.mobInteract(sourceentity, hand);
					}
				}
			} else if (this.isFood(itemstack)) {
				this.usePlayerItem(sourceentity, hand, itemstack);
				if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, sourceentity)) {
					this.tame(sourceentity);
					this.level().broadcastEntityEvent(this, (byte) 7);
				} else {
					this.level().broadcastEntityEvent(this, (byte) 6);
				}
				this.setPersistenceRequired();
				retval = InteractionResult.sidedSuccess(this.level().isClientSide());
			} else {
				retval = super.mobInteract(sourceentity, hand);
				if (retval == InteractionResult.SUCCESS || retval == InteractionResult.CONSUME)
					this.setPersistenceRequired();
			}
		}
		return retval;
	}

	@Override
	public void performRangedAttack(LivingEntity target, float flval) {
		Arrow entityarrow = new Arrow(this.level(), this);
		double d0 = target.getY() + target.getEyeHeight() - 1.1;
		double d1 = target.getX() - this.getX();
		double d3 = target.getZ() - this.getZ();
		entityarrow.shoot(d1, d0 - entityarrow.getY() + Math.sqrt(d1 * d1 + d3 * d3) * 0.2F, d3, 1.6F, 12.0F);
		this.level().addFreshEntity(entityarrow);
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageable) {
		PuppetmasterontologyEntity retval = CareerWarModEntities.PUPPETMASTERONTOLOGY.get().create(serverWorld);
		retval.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(retval.blockPosition()), MobSpawnType.BREEDING, null, null);
		return retval;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return Ingredient.of().test(stack);
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 40);
		builder = builder.add(Attributes.ARMOR, 2);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 2);
		builder = builder.add(Attributes.FOLLOW_RANGE, 32);
		return builder;
	}
}
