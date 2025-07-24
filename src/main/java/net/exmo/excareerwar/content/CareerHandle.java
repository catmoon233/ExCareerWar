package net.exmo.excareerwar.content;

import com.mojang.util.UUIDTypeAdapter;

import io.netty.buffer.Unpooled;
import io.redspace.ironsspellbooks.entity.mobs.SummonedHorse;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.entity.PuppetmasterontologyEntity;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.inventory.SelectedCareerMenu;
import net.exmo.excareerwar.network.CareerWarModVariables;

import net.exmo.excareerwar.network.SelectedCareerButtonMessage;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.events.LivingSwingEvent;
import net.exmo.exmodifier.util.EntityAttrUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSpawnPhantomsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.objectweb.asm.Type;


import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static net.exmo.excareerwar.content.CareerSkill.isSameTeam;
import static net.exmo.excareerwar.content.skills.LightingCloud.LightingA.canHit;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CareerHandle {
	public static List<Career> registeredCareers = new ArrayList<>();
	static AttributeModifier modifier = new AttributeModifier(UUID.fromString("7f5c3f7d-c0a7-4f0b-b5c5-c2c4c5c5c5c5"), "health", 10, AttributeModifier.Operation.ADDITION);


	public static void ChangeCareer(Player player,String career){
		if (player instanceof ServerPlayer serverPlayer)
		SelectedCareerButtonMessage.ChangeCareer(serverPlayer,career);
	}
	public static void registerCareer(Career career) {
		registeredCareers.add(career);
		Excareerwar.LOGGER.debug("Registered career: " + career.LocalName + "   "+career.toString());
	}
	public static Career getCareer(String name){
		return registeredCareers.stream().filter(c -> c.LocalName.equals(name)).findFirst().orElse(null);
	}
	private static int spellCount = 0;
	public final static Random random = new Random();
	public static void init() throws Exception {
//
//	new FireLord();
//	new GodLeader();
//	new Elementalist();
//	new Puppeteer();
//	new Yi();
//	new WindRanger();
//		IForgeRegistry<AbstractSpell> abstractSpells = io.redspace.ironsspellbooks.api.registry.SpellRegistry.REGISTRY.get();
//		spellCount = abstractSpells.getValues().size();
//		CareerIntanse.itemCount = ForgeRegistries.ITEMS.getValues().size();
//		List<Item> armorList = ForgeRegistries.ITEMS.getValues().stream().filter(i -> i instanceof ArmorItem).toList();
//		List<Item> SwordList = ForgeRegistries.ITEMS.getValues().stream().filter(i -> i instanceof SwordItem).toList();
//	for (String c: CareerIntanse.careerName) {
//		var al = new ArrayList<CareerSkill>();
//		for (int i = 0; i < random.nextInt(4)+1; i++) {
//			var RandomSpell = abstractSpells.getValues().stream().skip(random.nextInt(spellCount)).findFirst().orElse(null);
//			if (RandomSpell!=null) {
//				CareerSkill e = new CareerSkill(RandomSpell, random.nextInt(5));
//				al.add(e);
//			}
//		}
//
//		new CareerIntanse(c, al)
//				.addArmorItemKit((ArmorItem) armorList.get(random.nextInt(armorList.size())))
//				.addArmorItemKit((ArmorItem) armorList.get(random.nextInt(armorList.size())))
//				.addArmorItemKit((ArmorItem) armorList.get(random.nextInt(armorList.size())))
//				.addArmorItemKit((ArmorItem) armorList.get(random.nextInt(armorList.size())))
//				.addArmorItemKit((ArmorItem) armorList.get(random.nextInt(armorList.size())))
//				.addArmorItemKit((ArmorItem) armorList.get(random.nextInt(armorList.size())))
//				.addSwordItemKit((SwordItem) SwordList.get(random.nextInt(SwordList.size())))
//				.addSwordItemKit((SwordItem) SwordList.get(random.nextInt(SwordList.size())));
//	}
	}

	public static <T> Collection<T> load(Class<T> pluginInterface) {
		List<T> plugins = new ArrayList<>();

		for (ModFileScanData scanData : ModList.get().getAllScanData()) {
			Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
			for (ModFileScanData.AnnotationData a : annotations) {
				if (Objects.equals(a.annotationType(), Type.getType(AutoInit.class))) {
					String memberName = a.memberName();
					T plugin = getPlugin(memberName, pluginInterface);
					if (plugin != null) plugins.add(plugin);
				}
			}
		}
		return plugins;
	}

	@SuppressWarnings("unchecked")
	private static <T> T getPlugin(String className, Class<T> type) {
		try {
			Class<?> asmClass = Class.forName(className);
			if (!type.isAssignableFrom(asmClass)) return null;
			Class<? extends T> asmInstanceClass = asmClass.asSubclass(type);
			Constructor<? extends T> constructor = asmInstanceClass.getDeclaredConstructor();
			T instance = constructor.newInstance();
			return instance;
		} catch (ReflectiveOperationException | LinkageError e) {
			System.err.println("Failed to load plugin: " + className);
			e.printStackTrace();
		}
		return null;
	}
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) throws Exception {
		registeredCareers = new ArrayList<>();
		SkillHandle.registeredSkills = new ArrayList<>();

		load(CareerSkill.class);
		load(Career.class);
//		try {


//			// 创建 Reflections 对象数组，用于扫描指定包内的所有类
//			Reflections[] reflections = new Reflections[2];
//			reflections[0] = new Reflections(new ConfigurationBuilder()
//							.forPackages("net.exmo.excareerwar.content.skills")//指定扫描路径
//							.setScanners(new MethodParameterScanner())// 添加方法参数扫描工具，可以根据需要向该方法传入多个扫描工具
//					);
//			reflections[1] = new Reflections(new ConfigurationBuilder()
//					.forPackages("net.exmo.excareerwar.content.careers")//指定扫描路径
//					.setScanners(new MethodParameterScanner())// 添加方法参数扫描工具，可以根据需要向该方法传入多个扫描工具
//			);
//
//			for (Reflections reflection : reflections) {
//				// 查找所有带 @AutoInit 注解的类
//				Set<Class<?>> annotated = reflection.getTypesAnnotatedWith(AutoInit.class);
//				Exmodifier.LOGGER.debug("Found annotated classes:" + annotated);
//				for (Class<?> object : annotated) {
//					if (CareerSkill.class.isAssignableFrom(object)) {
//						Constructor<?> constructor = object.getConstructor();
//						CareerSkill skillInstance = (CareerSkill) constructor.newInstance();
//						SkillHandle.registeredSkills.add(skillInstance);
//					}
//					if (Career.class.isAssignableFrom(object)) {
//						Constructor<?> constructor = object.getConstructor();
//						Career CareerInstance = (Career) constructor.newInstance();
//						registerCareer(CareerInstance);
//					}
//				}
//			}
//		}catch (Exception e){
//			Exmodifier.LOGGER.error("Error initializing EXCARRERWAR: " , e);
//		}
//		new FlameCharge();
//		new FlameField();
//		new ArrowLead();
//		new Windrun();
//		new FireSkill();
//		new WaterSkill();
//		new WoodSkill();
//		new OmnipotentExplosion();
//		new AccumulatePowerChop();
//		new SensitiveWaterWhirl();
//		new SoulFlash();
//		new SoulShift();
//		new SpellBurst();
//		new SummonPuppets();
//		new Retreat();
		CareerHandle.init();
	}

	@Mod.EventBusSubscriber
	public static class ForgeBusEvents {
		@SubscribeEvent
		public static void Spawn(PlayerEvent.PlayerRespawnEvent event) {
			EntityAttrUtil.entityAddAttrTF(Attributes.MAX_HEALTH, modifier, event.getEntity(), EntityAttrUtil.WearOrTake.WEAR);
		}
//		@SubscribeEvent
//		public static void CantPickUpTippedArrow(Arrow event){
//			if (event.getStack().getItem() instanceof TippedArrowItem)event.setCanceled(true);
//		}
		@SubscribeEvent
		public static void onQ(LivingDropsEvent event){
			if (event.getEntity() instanceof  Player player){
				event.getDrops().forEach(item->{
					ItemStack item1 = item.getItem();
					if (item1.getTag() !=null&& item1.getTag().contains("Bing")){
						player.addItem(item1);
						item.remove(Entity.RemovalReason.DISCARDED);
					}
				});
			}
		}
		@SubscribeEvent
		public static void onM(ItemStackedOnOtherEvent event){
				if (event.getCarriedItem().getTag() !=null&& event.getCarriedItem().getTag().contains("Bind"))event.setCanceled(true);
				if (event.getSlot().getItem().getTag() !=null&& event.getSlot().getItem().getTag().contains("Bind"))event.setCanceled(true);
			//	if (event.getCarriedItem().getTag() !=null&& event.getSlot().getItem().getTag().contains("Bind"))event.setCanceled(true);
		}

		@SubscribeEvent
		public static void serverTick(TickEvent.ServerTickEvent event) {
			ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);
			if (level!=null){
				ServerScoreboard scoreboard = level.getScoreboard();
				int modCommand = getScore(scoreboard, "modCommand", "#system");
				if (modCommand == 1) {
					level.players().forEach(e->{
						NetworkHooks.openScreen((ServerPlayer) e, new MenuProvider() {
							@Override
							public Component getDisplayName() {
								return Component.literal("SelectedCareer");
							}

							@Override
							public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
								return new SelectedCareerMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(e.blockPosition()));
							}
						}, e.blockPosition());
					});
					setScore(scoreboard, "modCommand", "#system", 0);
				}
				if (modCommand == 2) {
					level.players().forEach(e->{
						e.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(
								c->{
									c.career = "";
									c.magicGether = "";
									c.syncPlayerVariables( e);
								}
						);
						AtomicReference<IItemHandler> _iitemhandlerref = new AtomicReference<>();
						e.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(_iitemhandlerref::set);
						if (_iitemhandlerref.get() != null) {
							for (int _idx = 0; _idx < _iitemhandlerref.get().getSlots(); _idx++) {
								ItemStack itemstackiterator = _iitemhandlerref.get().getStackInSlot(_idx).copy();
								if (!itemstackiterator.isEmpty()) {

									if (itemstackiterator.hasTag() && itemstackiterator.getTag().getBoolean("CareerItems")) {
										e.getInventory().setItem(_idx, ItemStack.EMPTY);
									}



								}
							}
						}
					});
					setScore(scoreboard, "modCommand", "#system", 0);
				}
			}
		}
		public static int getScore(Scoreboard scoreboard, String objectiveName, String player) {
			if (!scoreboard.hasObjective(objectiveName)) return -1;
			return scoreboard.getOrCreatePlayerScore(
					player,
					Objects.requireNonNull(scoreboard.getObjective(objectiveName))
			).getScore();
		}

		public static void setScore(Scoreboard scoreboard, String objectiveName, String player, int value) {
			scoreboard.getOrCreatePlayerScore(
					player,
					Objects.requireNonNull(scoreboard.getObjective(objectiveName))
			).setScore(value);
		}
		@SubscribeEvent
		public static void playerRespawn(PlayerEvent.PlayerRespawnEvent e) {


			Player player = e.getEntity();
			if (player instanceof ServerPlayer player1){
				player1.setGameMode(GameType.SPECTATOR);
			}
			player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.magicGether = "";
				capability	.syncPlayerVariables(player);
			});
			player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 40, (false), (false)));
			player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				if (CareerHandle.getCareer(capability.career)!=null){
					if(CareerHandle.getCareer(capability.career).Effects.isEmpty())return;
					for (MobEffectInstance effect : CareerHandle.getCareer(capability.career).Effects){
						player.addEffect(effect);
					}
				}
			});


		}

		@SubscribeEvent
		public static void playerTick(TickEvent.PlayerTickEvent e) {
			Player player = e.player;
		if (player.level().isClientSide) return;
		player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

				Map<String, Integer> s = capability.playercooldown;

            for (Map.Entry<String, Integer> entry : s.entrySet()) {

                String key = entry.getKey();
                Integer value = entry.getValue();
                if ((value) > 0) {
                    CareerSkill skill = SkillHandle.getSkill(key);
                    if (skill != null) {
                        if (skill.isSelfCooldown)continue;
                    }
                    s.put(key, (value - 1));
					if (value-1==0){
						if (player instanceof ServerPlayer serverPlayer) {
							Career career = CareerHandle.getCareer(capability.career);
							if (career != null) {
								if (career.Skills.contains(skill)) {
									int i = career.Skills.indexOf(skill) + 1;

									String a = "";
									if (i == 1) {
										a = "一";
									}
									if (i == 2) {
										a = "二";
									}
									if (i == 3) {
										a = "三";
									}
									if (i == 4) {
										a = "四";
									}
									if (i == 5) {
										a = "五";
									}
									if (i == 6) {
										a = "六";
									}
									if (i == 7) {
										a = "七";
									}
									if (i == 8) {
										a = "八";
									}
									if (a.isEmpty()) a = i + "";
									serverPlayer.playNotifySound(SoundEvents.NOTE_BLOCK_PLING.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
									serverPlayer.sendSystemMessage(Component.translatable("message.excareerwar.cooldown_end", a).withStyle(ChatFormatting.GREEN), true);
								}
							}
						}
					}
                }
            }
            capability.playercooldown = s;
				capability.syncPlayerVariables(player);
			});
		}
		public static void dispp(Player player){
			if (!player.getPersistentData().getString("Puppets").isEmpty()) {
				Level world = player.level();

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
					_entity.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)), 1000f);
				}
			}
			Vec3 pos = player.position();
			Level level = player.level();
			level.getEntities(player, AABB.ofSize(pos, (double) (30 * 2.0F), (double) (30 * 2.0F), (double) (30 * 2.0F)), (target) -> canHit(player, target)).forEach((target) -> {
				if ((target instanceof SummonedHorse a)) {
					if (a.getOwner()!=null) if(a.getOwner().equals(player)) a.remove(Entity.RemovalReason.KILLED);

				}
			});
		}
		@SubscribeEvent
		public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {

//			Player player = event.getEntity();
//			if (!(player instanceof ServerPlayer player1))return;
//			BlockPos _bpos = player.getOnPos();
//			player1.setGameMode(GameType.SPECTATOR);
//			NetworkHooks.openScreen(player1, new MenuProvider() {
//				@Override
//				public Component getDisplayName() {
//					return Component.literal("SelectedCareer");
//				}
//
//				@Override
//				public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
//					return new net.exmo.excareerwar.inventory.SelectedCareerMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
//				}
//			}, _bpos);
		}
		@SubscribeEvent
		public static void OnPlayerRightClick(PlayerInteractEvent.RightClickBlock event){
			Player player = event.getEntity();
			BlockState blockState = player.level().getBlockState(event.getHitVec().getBlockPos());
			if (blockState.getBlock() instanceof EnderChestBlock){
				if (!(player instanceof ServerPlayer player1))return;
				player1.closeContainer();
				player1.setGameMode(GameType.SPECTATOR);
				BlockPos _bpos = player.getOnPos();
				NetworkHooks.openScreen(player1, new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("SelectedCareer");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
						return new SelectedCareerMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
					}
				}, _bpos);
			}
		}
			@SubscribeEvent
		public static void atdeath(LivingDeathEvent event){


			if (!(event.getEntity() instanceof Player player)) return;
			dispp(player);
		}
		@SubscribeEvent
		public static void atHurt2(LivingHurtEvent event){
			LivingEntity entity = event.getEntity();
			if (entity.level().isClientSide) return;
			if (event.getSource().getEntity() instanceof Player player) {
				CareerWarModVariables.PlayerVariables v =player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CareerWarModVariables.PlayerVariables());
				if (getCareer(v.career) != null) {
					if (getCareer(v.career).LocalName.equals("fire_lord")) {
						if (player.getMainHandItem().getItem() instanceof SwordItem) {
							if (Math.random() <= 0.1) {
									//player.level().explode(player, entity.getX(), entity.getY(), entity.getZ(), 1, Level.ExplosionInteraction.NONE);
								ServerLevel level = (ServerLevel) player.level();
								level.sendParticles(ParticleTypes.EXPLOSION, entity.getX(), entity.getY(), entity.getZ(), 6, 0.2, 0.2, 0.2, 0.3);
								level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1, 1, false);
									entity.invulnerableTime=0;
								List<Entity> entities = level.getEntities(entity, AABB.ofSize(player.position(), 2 * 2, 2, 2 * 2));
								for (Entity targetEntity : entities) {
									if (targetEntity instanceof LivingEntity livingEntity && !isSameTeam(targetEntity, entity) && livingEntity.isAlive()) {
										livingEntity.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), player), 5);
										livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(0, 2.5, 0));
									}
								}

									entity.invulnerableTime=0;

							}
						}
					}
				}
			}
			if (entity instanceof PuppetmasterontologyEntity e){
				if (event.getSource().getEntity() instanceof Player player){
					if (player.getPersistentData().getString("Puppets").equals(e.getUUID().toString())){
						e.heal(event.getAmount());
						event.setAmount(0);

					}
				}
			}
		}
		@SubscribeEvent
		public static void atHurt3(LivingHurtEvent event){
			if (event.getEntity().hasEffect(CareerWarModMobEffects.VULNERABILITY.get())){
				event.setAmount((float) (event.getAmount()*(1+0.2*(event.getEntity().getEffect(CareerWarModMobEffects.VULNERABILITY.get()).getAmplifier()+1))));
			}
			if (event.getEntity().hasEffect(CareerWarModMobEffects.SPELL_BURST_E.get())){
				event.setAmount((float) (event.getAmount()*(1+0.3*(event.getEntity().getEffect(CareerWarModMobEffects.SPELL_BURST_E.get()).getAmplifier()+1))));
			}
		}


		@SubscribeEvent
		public static void playertick1(TickEvent.PlayerTickEvent e){

			Player player = e.player;
			//风之使者
			{
				if (player.getPersistentData().getInt("careerwar:fx") > 0) {
					player.getPersistentData().putInt("careerwar:fx", player.getPersistentData().getInt("careerwar:fx") - 1);
					if (player.level() instanceof ServerLevel l) {
						l.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY() + 1, player.getZ(), 8, 0.3, 0.3, 0.3, 0.1);
						{
							final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
							List<Entity> _entfound = l.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (entityiterator != player) {
									if (isSameTeam(entityiterator, player))continue;

									if (entityiterator instanceof LivingEntity) {
										((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1, (false), (false)));
										entityiterator.hurt(new DamageSource(l.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), player), (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue()+1.5f);
									}
								}
							}
						}

					}
				}
				player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					if (!capability.playercooldown.containsKey("fx")) return;

					if (Integer.valueOf(capability.playercooldown.get("fx")) >0) {
						int dashDistance = 3;
						player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

						SkillHandle.sendDashMessage(player,0.3, 2);


					}
				});
			}
			//火之领域
			{
				if (player.getPersistentData().getInt("flame_field") > 0) {
					if (player.level() instanceof ServerLevel l) {

						{
							SkillHandle.sendParticleCircle(l, player, ParticleTypes.FLAME, 4, 2);
							final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
							player.getPersistentData().putInt("flame_field", player.getPersistentData().getInt("flame_field") - 1);
							List<Entity> _entfound = l.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
							for (Entity entityiterator : _entfound) {
								if (entityiterator != player) {
									if (entityiterator instanceof LivingEntity) {
										if (isSameTeam(entityiterator, player))continue;
										entityiterator.setSecondsOnFire(4);
										if (!((LivingEntity) entityiterator).hasEffect(MobEffects.WEAKNESS)) ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, (false), (false)));
										player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 2, (false), (false)));
										if (!((LivingEntity) entityiterator).hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2, (false), (false)));
										if (!((LivingEntity) entityiterator).hasEffect(MobEffects.WITHER)) ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0, (false), (false)));
										//entityiterator.hurt(new DamageSource(l.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.IN_FIRE), player),0.5f );
									}
								}
							}
						}
					}
				}
			}
			//火焰冲锋
			{
				player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {

					if (capability.playercooldown.containsKey("Flame_Charge1")&&Integer.valueOf(capability.playercooldown.get("Flame_Charge1")) > 0) {
						SkillHandle.sendDashMessage(player,-0.2,0.2);
						if (player.level() instanceof ServerLevel l) {
							l.sendParticles(ParticleTypes.FLAME, player.getX(), player.getY() + 1, player.getZ(), 4, 0.3, 0.3, 0.3, 0.1);


							{
								final Vec3 _center = player.level().clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(1)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos().getCenter();
								List<Entity> _entfound = l.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
								for (Entity entityiterator : _entfound) {
									if (entityiterator != player) {
										if (entityiterator instanceof LivingEntity) {
											if (isSameTeam(entityiterator, player))continue;

											Map<String, Integer> a =capability.playercooldown;
											a.put("Flame_Charge1",0);
											if (a.containsKey("Flame_Charge")) a.put("Flame_Charge", Math.max(((capability.playercooldown.get("Flame_Charge")) - 70), 0));
											capability.playercooldown = a;
											capability.syncPlayerVariables(player);
											((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 4, (false), (false)));
											entityiterator.setSecondsOnFire(5);
											entityiterator.invulnerableTime = 0;
											entityiterator.hurt(new DamageSource(l.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 2f);
											entityiterator.setDeltaMovement(new Vec3(0, 1, 0));

										}
									}
								}
							}
						}
					}
				});
			}

		}
	}

		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void clientLoad(FMLClientSetupEvent event) {
		}
	}

