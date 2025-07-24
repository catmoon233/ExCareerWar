package net.exmo.excareerwar.content.skills.TheDestroyer;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.eldritch.AbstractEldritchSpell;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.item.enchantment.ThornsEnchantment.getDamage;

@AutoInit
public class SculkTentacles extends CareerSkill {


    public SculkTentacles() {
        super("SculkTentacles");
        this.CoolDown = (int) (40 * 45);
        this.IconTexture = SpellRegistry.SCULK_TENTACLES_SPELL.get().getSpellIconResource();
        this.LocalDescription = "SculkTentacles_d";
        this.firstCooldown = 30*40;
    }

    @Override
    public void use(Player player) {
        if (!setCoolDown(player))return;
        onCast(player.level(), 2, player);

    }

    public CastType getCastType() {
        return CastType.LONG;
    }



    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.VOID_TENTACLES_START.get());
    }

    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.VOID_TENTACLES_FINISH.get());
    }



    public void onCast(Level level, int spellLevel, Player entity) {
        int rings = 3;
        int count = 2;
         Vec3 _center = entity.position();
         boolean castIs = false;
        List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20 / 2d), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (Entity entityiterator1 : _entfound) {
            if (entityiterator1 == entity) continue;
            if (isSameTeam(entityiterator1, entity))continue;

            if (entityiterator1 instanceof VoidTentacle) continue;
            if (entityiterator1 instanceof LivingEntity living) {

                if (living.getHealth()<=0)continue;
                if (!living.hasEffect(MobEffects.UNLUCK))continue;
                if (living.getEffect(MobEffects.UNLUCK).getAmplifier()<8)continue;
                Vec3 center = living.position();

                living.addEffect(new MobEffectInstance(CareerWarModMobEffects.ImprisonMobEffect.get(), 20 * 4, 4, false, false, false));
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 6, 1, false, false, false));
                level.playSound(entity, center.x, center.y, center.z, SoundRegistry.VOID_TENTACLES_FINISH.get(), SoundSource.AMBIENT, 1, 1);

                for (int r = 0; r < rings; r++) {
                    float tentacles = count + r * 2;
                    for (int i = 0; i < tentacles; i++) {
                        Vec3 random = new Vec3(Utils.getRandomScaled(1), Utils.getRandomScaled(1), Utils.getRandomScaled(1));
                        Vec3 spawn = center.add(new Vec3(0, 0, 1.3 * (r + 1)).yRot(((6.281f / tentacles) * i))).add(random);

                        spawn = Utils.moveToRelativeGroundLevel(level, spawn, 8);
                        if (!level.getBlockState(BlockPos.containing(spawn).below()).isAir()) {
                            VoidTentacle tentacle = new VoidTentacle(level, entity, getAttackDamage(entity)*0.8f);
                            tentacle.moveTo(spawn);
                            tentacle.setYRot(Utils.random.nextInt(360));
                            level.addFreshEntity(tentacle);
                        }
                    }
                }
                level.gameEvent(null, GameEvent.ENTITY_ROAR, center);
                castIs = true;
            }
        }
        if (!castIs){
            SkillHandle.ChangeSkillV((Player) entity, "SculkTentacles", 0);
        }
        //In order to trigger sculk sensors

    }


    private int getRings(int spellLevel, LivingEntity entity) {
        return 1 + spellLevel;
    }
}
