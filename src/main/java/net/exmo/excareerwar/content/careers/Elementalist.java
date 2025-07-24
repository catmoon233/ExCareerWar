
package net.exmo.excareerwar.content.careers;


import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.entity.IceBallEntity;
import net.exmo.excareerwar.init.CareerWarModEntities;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.item.WoodStaffItem;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.excareerwar.util.PathGenerator;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
@AutoInit
@Mod.EventBusSubscriber
public class Elementalist extends Career {
    public Elementalist() throws Exception{
        super("Elementalist");
        this.bingItems = true;
        this.Skills.add(SkillHandle.getSkill("water"));
        this.Skills.add(SkillHandle.getSkill("fire"));
        this.Skills.add(SkillHandle.getSkill("Wood"));
        this.Skills.add(SkillHandle.getSkill("SpellBurst"));
        this.LocalDescription = "Elementalist_d";
        this.IsAlwaysHas = true;
        this.ItemKits.add(new ItemKit(EquipmentSlot.HEAD,"minecraft:diamond_helmet{Unbreakable:true,Enchantments:[{id:\"protection\",lvl:3s},{id:\"fire_protection\",lvl:2s},{id:\"blast_protection\",lvl:2s},{id:\"binding_curse\",lvl:1s},{id:\"projectile_protection\",lvl:3s},{id:\"feather_falling\",lvl:5s}],AttributeModifiers:[{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"head\",Operation:0,Amount:5.0d,UUID:[I;-1721411743,-2122232313,-1842708953,1554740280]},{AttributeName:\"generic.armor_toughness\",Name:\"generic.armor_toughness\",Slot:\"head\",Operation:0,Amount:2.0d,UUID:[I;-1105139654,2113749391,-1519190009,-1137572158]}],display:{Name:'{\"text\":\"\\\\u00a7l\\\\u00a7a元素之帽\"}'}}",1));
        this.ItemKits.add(new ItemKit(0,"excareerwar:wood_staff{}",1));
        this.itemIcon = net.exmo.excareerwar.init.CareerWarModItems.WOOD_STAFF.get();
        this.Effects.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 999999999, 0, false, false, false));
    }
    @SubscribeEvent
    public static void skillTick(LivingEvent.LivingTickEvent event){
        if (event.getEntity() instanceof ArmorStand entity){
            if (entity.getPersistentData().getInt("KilledTime")>0){
                entity.getPersistentData().putInt("KilledTime",entity.getPersistentData().getInt("KilledTime")-1);

            }
            if (entity.getPersistentData().getInt("KilledTime")==2){
                entity.kill();
            }
            if (entity.getPersistentData().getString("Skill").equals("FireBall")){

                if (entity.level() instanceof ServerLevel level) {
                    SkillHandle.generateSphereParticles(level, ParticleTypes.FLAME, entity.position(), 4, 25,0,0);
                    if (!level.getBlockState(entity.blockPosition().below(1)).isAir()){
                        Player player = null;

                        for (Player p : new ArrayList<>(level.players())) {
                            if (p.getStringUUID().equals(entity.getPersistentData().getString("Owner"))) {
                                player = p;
                                break;
                            }
                        }
                        level.sendParticles(ParticleTypes.EXPLOSION, entity.getX(),entity.getY(),entity.getZ(), 20, 1.5, 0.75, 1.5, 0.5);
                        final Vec3 _center = entity.position();

                        List<Entity> _entfound = entity.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                        for (Entity entityiterator : _entfound) {
                            if (entityiterator != entity) {
                                if (CareerSkill.isSameTeam(entityiterator, player))continue;
                                if (entityiterator != player) {
                                    if (entityiterator instanceof LivingEntity) {
                                        entityiterator.setSecondsOnFire(5);
                                        Random random = new Random();
                                        entityiterator.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 7f+random.nextInt(8));
                                        level.explode(player, entityiterator.getX(),entityiterator.getY(),entityiterator.getZ(), 1, Level.ExplosionInteraction.NONE);
                                        entityiterator.setDeltaMovement(0,1.5,0);
                                    }
                                }
                            }
                        }
                        entity.kill();

                    }

                }
            }
            if (entity.getPersistentData().getString("Skill").equals("FireField")){

                if (entity.level() instanceof ServerLevel level) {
                    SkillHandle.sendParticleCircle(level, entity, ParticleTypes.FLAME, 4, 9);
                    SkillHandle.sendParticleCircle(level, entity, ParticleTypes.FLAME, 3, 7);
                    SkillHandle.sendParticleCircle(level, entity, ParticleTypes.FLAME, 2, 5);
                    SkillHandle.sendParticleCircle(level, entity, ParticleTypes.FLAME, 1, 3);
                    if (!level.getBlockState(entity.blockPosition().below(1)).isAir()){
                        Player player = null;

                        for (Player p : new ArrayList<>(level.players())) {
                            if (p.getStringUUID().equals(entity.getPersistentData().getString("Owner"))) {
                                player = p;
                                break;
                            }
                        }
                        final Vec3 _center = entity.position();

                        List<Entity> _entfound = entity.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                        for (Entity entityiterator : _entfound) {
                            if (entityiterator != entity) {
                                if (entityiterator != player) {
                                    if (CareerSkill.isSameTeam(entityiterator, player))continue;

                                    if (entityiterator instanceof LivingEntity) {
                                        entityiterator.setSecondsOnFire(5);
                                        ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1, (false), (false)));
                                        ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2, (false), (false)));
                                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1, (false), (false)));
                                        entityiterator.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 2f);
                                        if (entityiterator.getY()>=entity.getY()-1.5&&entityiterator.getY()<=entity.getY()+1.5) {
                                            if (entityiterator.getPersistentData().getInt("KilledTime") % 15 == 0) {
                                                entityiterator.setDeltaMovement(0, 0.25, 0);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
            }
            if (entity.getPersistentData().getString("Skill").equals("tornado")) {
                if (entity.level() instanceof ServerLevel level) {
                    Vec3 pos = entity.position();

                    SkillHandle.sendParticleCircleN(level, new Vec3(pos.x,pos.y,pos.z), ParticleTypes.CLOUD, 3, 8);
                    SkillHandle.sendParticleCircleN(level, new Vec3(pos.x,pos.y+1,pos.z), ParticleTypes.CLOUD, 3f, 8);
                    SkillHandle.sendParticleCircleN(level, new Vec3(pos.x,pos.y+2,pos.z), ParticleTypes.CLOUD, 3f, 8);
                    SkillHandle.sendParticleCircleN(level, new Vec3(pos.x,pos.y+3,pos.z), ParticleTypes.CLOUD, 3f, 8);

                    SkillHandle.vmove(entity,-0.5,0.5);
                    Player player = null;

                    for (Player p : new ArrayList<>(level.players())) {
                        if (p.getStringUUID().equals(entity.getPersistentData().getString("Owner"))) {
                            player = p;
                            break;
                        }
                    }
                    final Vec3 _center = entity.position();

                    List<Entity> _entfound = entity.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                    for (Entity entityiterator : _entfound) {
                        if (entityiterator != entity) {
                            if (entityiterator != player) {
                                if (CareerSkill.isSameTeam(entityiterator, player))continue;

                                if (entityiterator instanceof LivingEntity) {
                                    ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1, (false), (false)));
                                    ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.WITHER, 20, 1, (false), (false)));
                                    ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2, (false), (false)));
                                    ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 2, (false), (false)));
                                    entityiterator.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 2f);
                                    SkillHandle.orbitAroundEntity(entityiterator, entity, 3, 0.5f);
                                }
                            }
                        }
                    }
                }
            }
            if (entity.getPersistentData().getString("Skill").equals("earthquake")) {
                if (entity.level() instanceof ServerLevel level) {
                    Vec3 pos = entity.position();
                    SkillHandle.vmove(entity,-0.5,0.5);

                  //  level.sendParticles(ParticleTypes.CLOUD, pos.x, pos.y, pos.z, 2, 0, 0, 0, 0);

                    Player player = null;

                    for (Player p : new ArrayList<>(level.players())) {
                        if (p.getStringUUID().equals(entity.getPersistentData().getString("Owner"))) {
                            player = p;
                            break;
                        }
                    }
                    if (entity.getPersistentData().getInt("KilledTime")%15==0) {
                        final Vec3 _center = entity.position();


                        level.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 45, 0.75, 1, 0.75, 0.3);
                        List<Entity> _entfound = entity.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                        for (Entity entityiterator : _entfound) {
                            if (entityiterator != entity) {
                                if (entityiterator != player) {
                                    if (CareerSkill.isSameTeam(entityiterator, player))continue;

                                    if (entityiterator instanceof LivingEntity) {
                                        ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(CareerWarModMobEffects.VULNERABILITY.get(), 160, 1, (false), (false)));
                                        level.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0.3);
                                        ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1, (false), (false)));
                                        ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.WITHER, 20, 1, (false), (false)));
                                        ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 2, (false), (false)));
                                        entityiterator.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 2f);
                                        entityiterator.setDeltaMovement(0,2.5,0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (entity.getPersistentData().getString("Skill").equals("BlackHole")) {
                if (entity.level() instanceof ServerLevel level) {
                    Vec3 pos = entity.position();

                    SkillHandle.generateSphereParticles(level, ParticleTypes.SQUID_INK, pos, 4, 45,0,0);
                    Player player = null;

                    for (Player p : new ArrayList<>(level.players())) {
                        if (p.getStringUUID().equals(entity.getPersistentData().getString("Owner"))) {
                            player = p;
                            break;
                        }
                    }
                        final Vec3 _center = entity.position();

                     List<Entity> _entfound = entity.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                        for (Entity entityiterator : _entfound) {
                            if (entityiterator != entity) {
                                if (entityiterator != player) {
                                    if (CareerSkill.isSameTeam(entityiterator, player))continue;

                                    if (entityiterator instanceof LivingEntity) {
                                        ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(CareerWarModMobEffects.VULNERABILITY.get(), 20, 1, (false), (false)));
                                        ((LivingEntity) player).addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30, 2, (false), (false)));
                                        entityiterator.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 1.5f);
                                        SkillHandle.moveTowardsBlockPos(entityiterator, BlockPos.containing(pos), 1.25, 0.5);


                                    }
                                }
                            }

                    }
                }
            }
        }
    }
    public static void teleporting(Player player){
        BlockPos pos = SkillHandle.findFarthestNonAirBlock(player,20);
        List<Vec3> vl = PathGenerator.generatePath(Vec3.atCenterOf(player.blockPosition()), Vec3.atCenterOf(pos));
        for (Vec3 v : vl){
            if (player.level() instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(ParticleTypes.WITCH, v.x(), v.y(), v.z(), 4, 0.3, 0.3, 0.3, 0.1);
                {
                    final Vec3 _center = v;
                    List<Entity> _entfound = serverLevel.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                    for (Entity entityiterator : _entfound) {
                        if (CareerSkill.isSameTeam(entityiterator, player))continue;

                        if (entityiterator != player) {
                            if (entityiterator instanceof LivingEntity) {
                                player.heal(0.5f);
                                entityiterator.hurt(new DamageSource(serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 5);
                                entityiterator.setDeltaMovement(new Vec3(0,1,0));
                            }
                        }
                    }
                }
            }
        }
        player.teleportTo(pos.getX(),pos.getY()+1,pos.getZ());

    }
    public static void BlackHole(Player player){
        if (player.level() instanceof ServerLevel level) {
            BlockPos pos = SkillHandle.findFarthestNonAirBlock(player,25);
            BlockPos pos1 = new BlockPos(pos.getX(),pos.getY()+5,pos.getZ());
            if (player.level() instanceof ServerLevel _level) {
                LivingEntity entityToSpawn = EntityType.ARMOR_STAND.spawn(_level, pos1, MobSpawnType.MOB_SUMMONED);
                if (entityToSpawn != null) {
                    entityToSpawn.getPersistentData().putInt("KilledTime", 120);
                                           entityToSpawn.getPersistentData().putString("Owner", player.getStringUUID());
                        entityToSpawn.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 99999999, 1,false,false));
                    entityToSpawn.getPersistentData().putString("Skill", "BlackHole");
                    entityToSpawn.setInvisible(true);
                    entityToSpawn.setNoGravity(true);
                    SkillHandle.sendParticleCircle((ServerLevel) player.level(), player, ParticleTypes.COMPOSTER, 1, 10);
                    SkillHandle.sendParticleCircle((ServerLevel) player.level(), player, ParticleTypes.COMPOSTER, 3, 10);
                    SkillHandle.sendParticleCircle((ServerLevel) player.level(), player, ParticleTypes.DRIPPING_DRIPSTONE_WATER, 4, 10);
                }

            }
        }
    }
    public static void earthquake(Player player){
        if (player.level() instanceof ServerLevel level) {
            //SkillHandle.generateSphereParticles(level, ParticleTypes.FLAME,);
            if (player instanceof ServerPlayer serverPlayer) {
                BlockPos pos1 = serverPlayer.blockPosition();

                Level world = serverPlayer.level();
                if (world instanceof ServerLevel _level) {
                    LivingEntity entityToSpawn = EntityType.ARMOR_STAND.spawn(_level, pos1, MobSpawnType.MOB_SUMMONED);
                    if (entityToSpawn != null) {
                        entityToSpawn.getPersistentData().putInt("KilledTime", 80);
                                               entityToSpawn.getPersistentData().putString("Owner", player.getStringUUID());
                        entityToSpawn.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 99999999, 1,false,false));
                        entityToSpawn.getPersistentData().putString("Skill", "earthquake");
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
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.FLAME, 1, 10);
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.COMPOSTER, 3, 10);
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.COMPOSTER, 4, 10);
                    }

                }
            }
        }
    }
    public static void tornado(Player player){
        if (player.level() instanceof ServerLevel level) {
            //SkillHandle.generateSphereParticles(level, ParticleTypes.FLAME,);
            if (player instanceof ServerPlayer serverPlayer) {
                BlockPos pos1 = serverPlayer.blockPosition();

                Level world = serverPlayer.level();
                if (world instanceof ServerLevel _level) {
                    LivingEntity entityToSpawn = EntityType.ARMOR_STAND.spawn(_level, pos1, MobSpawnType.MOB_SUMMONED);
                    if (entityToSpawn != null) {
                        entityToSpawn.getPersistentData().putInt("KilledTime", 120);
                         entityToSpawn.getPersistentData().putString("Owner", player.getStringUUID());
                        entityToSpawn.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 99999999, 1,false,false));
                        entityToSpawn.getPersistentData().putString("Skill", "tornado");
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

                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.FLAME, 1, 10);
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.COMPOSTER, 3, 10);
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.COMPOSTER, 4, 10);
                    }

                }
            }
        }
    }
    public static void iceBall(Player player){
        player.getPersistentData().putInt("IceBall", 100);
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 3, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 100, 2, false, false, false));
    }
    public static void NatureShield(Player player) {
        player.getPersistentData().putInt("NatureShield", 120);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, false, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2, false, false, false));
       if (player.getAbsorptionAmount()+8 <8)
        player.setAbsorptionAmount(player.getAbsorptionAmount() + 8);
    }
    public static void LightingHit(Player player){
        if (player instanceof ServerPlayer serverPlayer) {
            BlockPos pos = SkillHandle.findFarthestNonAirBlock(serverPlayer, 30);
            if (player.level() instanceof  ServerLevel level)
            if (player.level() instanceof ServerLevel _level) {
                LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(_level);
                entityToSpawn.moveTo(Vec3.atBottomCenterOf(pos));;
                RandomSource random = RandomSource.create(); // 创建随机数生成器

                for (int i = 0; i < 25; i++) {
                    // 计算角度
                    double angle = i * (2 * Math.PI / 25);

                    // 在圆周上计算粒子位置
                    double offsetX = 3 * Math.cos(angle);
                    double offsetZ = 3 * Math.sin(angle);

                    // 添加一点随机偏移，使粒子分散
                    offsetX += random.nextDouble() * 0.1 - 0.05;
                    offsetZ += random.nextDouble() * 0.1 - 0.05;

                    // 发送粒子
                    level.sendParticles(ParticleTypes.END_ROD, pos.getX() + offsetX, pos.getY()+1.25, pos.getZ() + offsetZ, 1, 0.0, 0.0, 0.0, 0.05);
                }
                entityToSpawn.setVisualOnly(true);
                _level.addFreshEntity(entityToSpawn);
                final Vec3 _center = entityToSpawn.position();

                List<Entity> _entfound = _level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (entityiterator != player) {
                        if (entityiterator instanceof LivingEntity) {
                            entityiterator.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.LIGHTNING_BOLT),player), 10f);
                        }
                    }

                }
            }

        }
    }
    public static void FireBall(Player player) {
        if (player.level() instanceof ServerLevel level) {
            //SkillHandle.generateSphereParticles(level, ParticleTypes.FLAME,);
            if (player instanceof ServerPlayer serverPlayer) {
                BlockPos pos = SkillHandle.findFarthestNonAirBlock(serverPlayer, 30);
                BlockPos pos1 = new BlockPos(pos.getX(), pos.getY() + 15, pos.getZ());
                Level world = serverPlayer.level();
                if (world instanceof ServerLevel _level) {
                    LivingEntity entityToSpawn = EntityType.ARMOR_STAND.spawn(_level, pos1, MobSpawnType.MOB_SUMMONED);
                    if (entityToSpawn != null) {
                        entityToSpawn.getPersistentData().putInt("KilledTime", 120);
                                               entityToSpawn.getPersistentData().putString("Owner", player.getStringUUID());
                        entityToSpawn.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 99999999, 1,false,false));
                        entityToSpawn.getPersistentData().putString("Skill", "FireBall");
                        entityToSpawn.setInvisible(true);
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.FLAME, 1, 10);
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.FLAME, 3, 10);
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.FLAME, 4, 10);
                    }

                }
            }
        }
    }
    public static void FireField(Player player) {
        if (player.level() instanceof ServerLevel level) {
            //SkillHandle.generateSphereParticles(level, ParticleTypes.FLAME,);
            if (player instanceof ServerPlayer serverPlayer) {
                BlockPos pos1 = player.blockPosition();
                Level world = serverPlayer.level();
                if (world instanceof ServerLevel _level) {
                    LivingEntity entityToSpawn = EntityType.ARMOR_STAND.spawn(_level, pos1, MobSpawnType.MOB_SUMMONED);
                    if (entityToSpawn != null) {
                        entityToSpawn.getPersistentData().putInt("KilledTime", 160);
                                               entityToSpawn.getPersistentData().putString("Owner", player.getStringUUID());
                        entityToSpawn.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 99999999, 1,false,false));
                        entityToSpawn.getPersistentData().putString("Skill", "FireField");
                        entityToSpawn.setInvisible(true);
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.FLAME, 1, 10);
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.FLAME, 3, 10);
                        SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.COMPOSTER, 4, 10);
                    }

                }
            }
        }
    }
    public static void steam(Player player){
        Level level = player.level();
            if (player instanceof ServerPlayer serverPlayer) {
                SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.FLAME, 1, 10);
                SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.FLAME, 3, 10);
                SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.DRIPPING_DRIPSTONE_WATER, 4, 10);

            }
            if (level instanceof ServerLevel _level){
                _level.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY()+1.5, player.getZ(), 45, 0.75, 0.5, 0.75, 0.2);
            }
                final Vec3 _center = player.position();
                List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (CareerSkill.isSameTeam(entityiterator, player))continue;

                    if (entityiterator != player) {
                            if (entityiterator instanceof LivingEntity) {
                                ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(CareerWarModMobEffects.VULNERABILITY.get(), 100, 0, (false), (false)));
                                ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1, (false), (false)));
                                entityiterator.setSecondsOnFire(5);
                                ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2, (false), (false)));
                                entityiterator.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC),player), 6f);
                                entityiterator.setDeltaMovement(0,1,0);

                            }
                        }

                }
            }


    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.getPersistentData().getInt("IceBall")>0) {
            player.getPersistentData().putInt("IceBall", player.getPersistentData().getInt("IceBall") - 1);
            if (player.getPersistentData().getInt("IceBall") % 5 == 0) {
                Entity _shootFrom = player;
                Entity _ent = _shootFrom;
                if (!_ent.level().isClientSide() && _ent.getServer() != null) {
                    _ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
                            _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "tp @s ~ ~ ~ ~ ~-2");

                }
                if (player.level() instanceof  ServerLevel level){
                    level.sendParticles(ParticleTypes.SNOWFLAKE, player.getX(), player.getY()+1.25, player.getZ(), 10, 0.1, 0.1, 0.1, 0.1);
                }
                Level projectileLevel = _shootFrom.level();
                if (!projectileLevel.isClientSide()) {
                    Projectile _entityToSpawn = new Object() {
                        public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
                            AbstractArrow entityToSpawn = new IceBallEntity(CareerWarModEntities.ICE_BALL.get(), level);
                            entityToSpawn.setOwner(shooter);
                            entityToSpawn.setBaseDamage(damage);
                            entityToSpawn.setKnockback(knockback);


                            entityToSpawn.setSilent(true);
                            entityToSpawn.pickup = AbstractArrow.Pickup.DISALLOWED;
                            return entityToSpawn;
                        }
                    }.getArrow(projectileLevel, player, (float) 0.65, 1);
                    _entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.1, _shootFrom.getZ());
                    _entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, 4, (float) 0.6);
                    projectileLevel.addFreshEntity(_entityToSpawn);
                }
            }
        }

        if (player.getPersistentData().getInt("NatureShield")>0){
                player.getPersistentData().putInt("NatureShield",player.getPersistentData().getInt("NatureShield")-1);
                if (player.level() instanceof ServerLevel level) {
                    SkillHandle.generateSphereParticles(level, ParticleTypes.TOTEM_OF_UNDYING, player.position(), 3, 20, 0, 0);
                }
                Level level = player.level();
            final Vec3 _center = player.position();

            List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (Entity entityiterator : _entfound) {
                if (CareerSkill.isSameTeam(entityiterator, player))continue;

                if (entityiterator != player) {
                    if (entityiterator instanceof LivingEntity) {
                        if (!level.isClientSide) {
                          entityiterator.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), 2.4f);
                        }
                        SkillHandle.vmove((LivingEntity) entityiterator,0.5,-1);

                    }
                }

            }

        }
        if (event.player.getMainHandItem().getItem() instanceof  WoodStaffItem w){
            if (event.player.level().isClientSide()){
                player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    List<String> stringList = List.of(capability.magicGether.split(","));
                    if (!stringList.isEmpty()) {
                        StringBuilder dis = new StringBuilder();
                        for (String s : stringList) {
                            dis.append(" ").append(Component.translatable(s).getString());
                        }
                        if (dis.toString() !="") event.player.displayClientMessage(Component.literal(dis.toString().toString()), true);
                    }
                });

            }}
        }

        public static void skillCooldownin(Player player){
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (capability.playercooldown.containsKey("Wood")) {
                if (capability.playercooldown.get("Wood") < 1)
                    capability.playercooldown.replace("Wood", ((capability.playercooldown.get("Wood")) - 1));
            }
            if (capability.playercooldown.containsKey("water")) {
                if (capability.playercooldown.get("water") < 1)
                    capability.playercooldown.replace("Wood", ((capability.playercooldown.get("water")) - 1));
            }
            if (capability.playercooldown.containsKey("fire")) {
                if (capability.playercooldown.get("fire") < 1)
                    capability.playercooldown.replace("fire", ((capability.playercooldown.get("fire")) - 1));
            }
            capability.syncPlayerVariables(player);
        });
        }
public static void magicSet(Player player ,String s){
    player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
        Excareerwar.LOGGER.info(capability.magicGether);

        if (capability.magicGether!=null){
            List<String> stringList = List.of(capability.magicGether.split(","));
            if (stringList.size()<3){
                if (stringList.size()!=2) capability.magicGether = capability.magicGether+s+",";
                else capability.magicGether = capability.magicGether+s;
            }else {
                StringBuilder fs = new StringBuilder();
                if (stringList.size()==3){
                        fs.append(stringList.get(1)+",");
                        fs.append(stringList.get(2)+",");
                    fs.append(s);
                }

                capability.magicGether = fs.toString();
            }
        }else {
            capability.magicGether = s;
        }
        capability.syncPlayerVariables(player);
        Excareerwar.LOGGER.info(capability.magicGether);

    });

}
}
