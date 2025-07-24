package net.exmo.excareerwar.content.skills.YanJue;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.ArrowVolleyEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.spells.evocation.ArrowVolleySpell;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@AutoInit
public class WangJianTianYing extends CareerSkill {
    public WangJianTianYing() {
        super("WangJianTianYing");
        this.CoolDown = (int) (20 * 40);
        this.IconTexture = SpellRegistry.ARROW_VOLLEY_SPELL.get().getSpellIconResource();
        this.LocalDescription = "WangJianTianYing_d";
        this.firstCooldown = 20*40;
        this.isSelfCooldown = true;
    }


    @Override
    public void use(Player entity) {
        if (isInCooldown(entity))return;
        Vec3 targetLocation = null;
        Level level = entity.level();
        if (level.isClientSide)return;
        targetLocation = Utils.raycastForEntity(level, entity, 100.0F, true).getLocation();

        SkillHandle.sendDashMessage(entity, 1.4, 0);
        Vec3 finalTargetLocation = targetLocation;
        Excareerwar.queueServerWork(5,()->{
            entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 50, 3));
            Vec3 backward = (new Vec3(finalTargetLocation.x - entity.getX(), (double)0.0F, finalTargetLocation.z - entity.getZ())).normalize().scale((double)-4.0F);
            Vec3 raycastTarget = Utils.moveToRelativeGroundLevel(level, finalTargetLocation.add((double)0.0F, (double)2.0F, (double)0.0F), 4).add(backward).add((double)0.0F, (double)6.0F, (double)0.0F);
            Vec3 spawnLocation = Utils.raycastForBlock(level, finalTargetLocation, raycastTarget, ClipContext.Fluid.NONE).getLocation();
            spawnLocation = spawnLocation.subtract(finalTargetLocation).scale((double)0.9F).add(finalTargetLocation);
            float dx = Mth.sqrt((float)((spawnLocation.x - finalTargetLocation.x) * (spawnLocation.x - finalTargetLocation.x) + (spawnLocation.z - finalTargetLocation.z) * (spawnLocation.z - finalTargetLocation.z)));
            float arrowAngleX = dx == 0.0F ? 70.0F : (float)(Mth.atan2((double)dx, spawnLocation.y - finalTargetLocation.y) * (double)(180F / (float)Math.PI));
            float arrowAngleY = entity.getX() == finalTargetLocation.x && entity.getZ() == finalTargetLocation.z ? (entity.getYRot() - 90.0F) * ((float)Math.PI / 180F) : Utils.getAngle(entity.getX(), entity.getZ(), finalTargetLocation.x, finalTargetLocation.z);
            ArrowVolleyEntity arrowVolleyEntity = new ArrowVolleyEntity(EntityRegistry.ARROW_VOLLEY_ENTITY.get(), level);
            arrowVolleyEntity.moveTo(spawnLocation);
            arrowVolleyEntity.setYRot(arrowAngleY * (180F / (float)Math.PI) + 90.0F);
            arrowVolleyEntity.setXRot(arrowAngleX + 25.0F);
            arrowVolleyEntity.setDamage(7f);
            arrowVolleyEntity.setArrowsPerRow(24);
            arrowVolleyEntity.setRows(11);
            arrowVolleyEntity.setOwner(entity);
            level.addFreshEntity(arrowVolleyEntity);
        });


        super.use(entity);
    }
}
