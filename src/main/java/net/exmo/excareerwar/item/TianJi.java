package net.exmo.excareerwar.item;

import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TianJi extends SwordItem implements Vanishable {

    public TianJi(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (player.hasEffect(CareerWarModMobEffects.HeavenlyEye.get())){
            CareerWarModVariables.PlayerVariables vars = player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CareerWarModVariables.PlayerVariables());
            final Vec3 _center = player.position();

            List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(42 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (Entity entityiterator1 : _entfound) {
                if (entityiterator1 instanceof LivingEntity livingEntity){
                    if (Objects.equals(entityiterator1.getUUID().toString(), vars.HeavenlyEyeUUID)){
                        vars.HeavenlyEyeUUID = "";
                        vars.syncPlayerVariables(player);
                        Vec3 position = entityiterator1.position();
                        player.teleportTo(position.x(), position.y(), position.z());
                        ((LivingEntity) entityiterator1).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 6, false, false,false));
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 6, false, false,false));
                        SkillHandle.sendDashMessage(player,0,-0.45);
                        entityiterator1.setDeltaMovement(0,0.3,0);
                        player.heal(4);
                        player.swing(InteractionHand.MAIN_HAND);
                        entityiterator1.hurt(new DamageSource(level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC), player), (float) (player.getAttributeValue(Attributes.ATTACK_DAMAGE)*0.75f-1));
                        if (livingEntity.isDeadOrDying()){
                            SkillHandle.ChangeSkillV(player,"KaiSkill",0);
                        }
                        player.removeEffect(CareerWarModMobEffects.HeavenlyEye.get());
                    }
                }
            }
        }
        return super.use(level, player, interactionHand);
    }
}
