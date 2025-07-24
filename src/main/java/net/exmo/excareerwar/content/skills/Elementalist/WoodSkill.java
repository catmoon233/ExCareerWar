package net.exmo.excareerwar.content.skills.Elementalist;


import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.content.careers.Elementalist;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
@AutoInit
@Mod.EventBusSubscriber
public class WoodSkill extends CareerSkill {
    public WoodSkill(){
        super("Wood");
        this.CoolDown = 40*4;
        this.Icon = Items.GRASS;
        this.LocalDescription = "Wood_d";
    }
@SubscribeEvent
public static void SkillContent(LivingEvent.LivingTickEvent event){
        if (event.getEntity() instanceof  ArmorStand armorStand){
            if (armorStand.getPersistentData().getInt("KilledTime")>0){
                armorStand.getPersistentData().putInt("KilledTime", armorStand.getPersistentData().getInt("KilledTime") - 1);
            }
            if (armorStand.getPersistentData().getInt("KilledTime")==1){
                armorStand.remove(Entity.RemovalReason.KILLED);
            }
            if (armorStand.getPersistentData().getString("Skill").equals("Wood")){
                if (armorStand.level() instanceof  ServerLevel level){
                    level.sendParticles(ParticleTypes.COMPOSTER, armorStand.getX(), armorStand.getY()+0.5, armorStand.getZ(), 10, 1.75, 0, 1.75, 0.4);
                    {
                        Player player = null;

                        for (Player p : new ArrayList<>(level.players())) {
                            if (p.getStringUUID().equals(armorStand.getPersistentData().getString("Owner"))) {
                                player = p;
                                break;
                            }
                            }
                        SkillHandle.sendParticleCircle(level, armorStand, ParticleTypes.COMPOSTER, 4, 15);
                        if (player ==null)return;
                        final Vec3 _center = new Vec3(armorStand.getX(), armorStand.getY(), armorStand.getZ());
                        List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                        for (Entity entityiterator : _entfound) {
                            if (entityiterator != armorStand) {
                            if (entityiterator != player) {
                                if (entityiterator instanceof LivingEntity) {
                                    if (isSameTeam(entityiterator, player))continue;


                                    ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.WITHER, 20, 1, (false), (false)));
                                    ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1, (false), (false)));
                                    ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(CareerWarModMobEffects.VULNERABILITY.get(), 20, 0, (false), (false)));
                                    ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1, (false), (false)));
                                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1, (false), (false)));
                                }
                            }
                            }
                        }
                    }
                }
            }
        }
}
    public void use(Player player) {

        if (!this.setCoolDown(player)) return;
       // Elementalist.skillCooldownin(player);
        player.getCooldowns().addCooldown(CareerWarModItems.WOOD_STAFF.get(), 20);
        Elementalist.magicSet(player,"wood");
        if (player.hasEffect(CareerWarModMobEffects.SPELL_BURST_E.get())){
            SkillHandle.ChangeSkillV(player,"Wood",30);
        }
        player.swing(player.getUsedItemHand());
        if (player instanceof ServerPlayer serverPlayer) {
            BlockPos pos = SkillHandle.findFarthestNonAirBlock(serverPlayer, 30);
            BlockPos pos1 = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
            Level world = serverPlayer.level();
            if (world instanceof ServerLevel _level) {
                LivingEntity    entityToSpawn = EntityType.ARMOR_STAND.spawn(_level, pos1, MobSpawnType.MOB_SUMMONED);
                if (entityToSpawn != null) {
                    entityToSpawn.getPersistentData().putInt("KilledTime", 80);
                    entityToSpawn.getPersistentData().putString("Owner", player.getStringUUID());
                    entityToSpawn.getPersistentData().putString("Skill", "Wood");
                    entityToSpawn.setInvisible(true);
                    SkillHandle.sendParticleCircle((ServerLevel) serverPlayer.level(), player, ParticleTypes.COMPOSTER, 1, 10);

                }

            }
        }
    }

}
