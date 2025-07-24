package net.exmo.excareerwar.content.skills.ErlangShen;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.ray_of_frost.RayOfFrostVisualEntity;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@AutoInit
public class RootPurpose extends CareerSkill {
    public RootPurpose() {
        super("RootPurpose");
        this.CoolDown = (int) (sendCondTick*18);
        this.Icon = Items.ENDER_EYE;
        this.LocalDescription = "RootPurpose_d";
    }
    public static int getCastCount(Player player){
        return player.getPersistentData().getInt("RootPCount");
    }
    public static void setCastCount(Player player,int count){
        player.getPersistentData().putInt("RootPCount",count);
    }
    @Override
    public void use(Player entity) {
        if (SkillHandle.getSkillV(entity, getLocalName()) > 0 && SkillHandle.getSkillV(entity, getLocalName()) < this.CoolDown * 0.5) {
            setCastCount(entity, 0);
        }
        int castCount = getCastCount(entity);
        if (castCount >0)
        {
            setCastCount(entity, castCount - 1);
            if (castCount == 1){
                for (int index = -1; index < 2; index++) {
                    prepareCast(entity, index,index);
                }
            }
            if (castCount == 2){
                for (int index = -1; index < 2; index++) {
                    if (index==0)continue;
                    prepareCast(entity, index,index);
                }

            }
            if (castCount == 3){
                extracted(entity,entity.getX(),entity.getZ(),0);

            }

        }
        if (!setCoolDown(entity))return;
        setCastCount(entity,3);
        use(entity);

    }

    private void prepareCast(Player entity, int index,int pose) {
      //  double x = entity.getX() + (Math.cos(Math.toRadians(entity.getYRot())) * 2) * index * 0.5;
    //    double z = entity.getZ() + (Math.sin(Math.toRadians(entity.getYRot())) * 2) * index * 0.5;
        extracted(entity,entity.getX(),entity.getZ(),pose);
    }

    private void extracted(Player entity,double x,double z,int pose) {
        Level level = entity.level();
        if (level.isClientSide)return;
        var hitResult = Utils.raycastForEntity(level, entity, 12, true, .15f);
        RayOfFrostVisualEntity p46964 = new RayOfFrostVisualEntity(level, new Vec3(x, entity.getEyeY()+1.5, z), hitResult.getLocation(), entity);


      var    commandSourceStack =  new CommandSourceStack(
                CommandSource.NULL,
              p46964.position(),
              p46964.getRotationVector(),
                (ServerLevel) p46964.level(),
                4,
              p46964.getName().getString(),
              p46964.getDisplayName(),
              p46964.getServer(),p46964);
        if (pose==1){
            p46964.getServer().getCommands().performPrefixedCommand(commandSourceStack, "teleport @s ~ ~ ~ ~-20 ~20");
        }
        if (pose==-1){
            p46964.getServer().getCommands().performPrefixedCommand(commandSourceStack, "teleport @s ~ ~ ~ ~20 ~20");
        }
        level.addFreshEntity(p46964);

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult) hitResult).getEntity();
            var ls = getEntities(level,target.position(), Math.abs(pose) + 5,entity);
            //Set freeze time right here because it scales off of level and power
            Castinit(entity, level, hitResult, ls);
        }else if (hitResult.getType() == HitResult.Type.BLOCK) {
            MagicManager.spawnParticles(level, ParticleHelper.FIREFLY, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 4, 0, 0, 0, .3, true);
            var ls = getEntities(entity, pose, level, hitResult);
            //Set freeze time right here because it scales off of level and power
            Castinit(entity, level, hitResult, ls);
        }
        MagicManager.spawnParticles(level, ParticleHelper.FIRE, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 50, 0, 0, 0, .3, false);
    }

    private static @NotNull List<Entity> getEntities(Player entity, int pose, Level level, HitResult hitResult) {
        var ls = getEntities(level, hitResult.getLocation(), Math.abs(pose) + 3, entity);
        return ls;
    }

    private void Castinit(Player entity, Level level, HitResult hitResult, List<Entity> ls) {
        float healValue = getAttackDamage(entity) * 0.45f;
        for (Entity entity1 : ls) {
            if (entity1==entity)continue;
            if (!(entity1 instanceof LivingEntity))continue;
            if (isSameTeam(entity1, entity))continue;

            AtomicReference<DamageSource> damageSoure = new AtomicReference<>();
            Runnable damageSoureNOI = getDamageSoureNOI(entity, DamageTypes.MAGIC, damageSoure::set);
            healValue = getHealValue(entity, entity1, healValue, damageSoure.get(), level, hitResult);
            damageSoureNOI.run();
        }
    }

    private static float getHealValue(Player entity, Entity target, float healValue, DamageSource damageSoure, Level level, HitResult hitResult) {
        DamageSources.applyDamage(target, healValue * 1.25F, damageSoure);
        if (entity.getHealth() + healValue > entity.getMaxHealth()) {
            healValue -= (entity.getMaxHealth() - entity.getHealth());
            entity.heal(healValue);
            if (entity.getAbsorptionAmount() < entity.getMaxHealth() * 0.2) {
                entity.setAbsorptionAmount((float) Math.min(entity.getMaxHealth() * 0.2, entity.getAbsorptionAmount() + healValue));
            }
        } else {
            entity.heal(healValue*0.7f);
        }
        MagicManager.spawnParticles(level, ParticleHelper.FOG, hitResult.getLocation().x, target.getY(), hitResult.getLocation().z, 4, 0, 0, 0, .3, true);
        return healValue;
    }
}
