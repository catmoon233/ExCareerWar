package net.exmo.excareerwar.content.careers;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.content.events.SwingEvent;
import net.exmo.excareerwar.content.skills.TheDestroyer.DefeatedBodyTechnique;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
@AutoInit
public class TheDestroyer extends Career {
    public TheDestroyer() throws Exception {
        super("TheDestroyer");
        this.bingItems = true;
        this.IsAlwaysHas = true;
        this.itemIcon= Items.OBSIDIAN;
        this.LocalDescription = "TheDestroyer_d";
        this.Skills.add(SkillHandle.getSkill("AnnihilationSlash"));
        this.Skills.add(SkillHandle.getSkill("DefeatedBodyTechnique"));
        this.Skills.add(SkillHandle.getSkill("SculkTentacles"));
        this.ItemKits.add(new ItemKit(0,"irons_spellbooks:keeper_flamberge{Unbreakable:true,Enchantments:[{id:\"fire_aspect\",lvl:1s},{id:\"sweeping\",lvl:1s}],AttributeModifiers:[{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"mainhand\",Operation:0,Amount:3.25d,UUID:[I;1672534749,265437927,-1635975972,913088645]},{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"mainhand\",Operation:0,Amount:4.5d,UUID:[I;161552313,-1020181367,-1704501186,-1320158221]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"mainhand\",Operation:0,Amount:-2.8d,UUID:[I;-1113777948,-1485027559,-2086690721,-1247416420]}],display:{Name:'{\"text\":\"\\\\u00a7l\\\\u00a78破灭之刃\"}'}}",1,true));
        this.ItemKits.add(new ItemKit(EquipmentSlot.HEAD, "irons_spellbooks:tarnished_helmet{Unbreakable:true,Enchantments:[{id:\"blast_protection\",lvl:2s},{id:\"thorns\",lvl:1s},{id:\"fire_protection\",lvl:1s},{id:\"binding_curse\",lvl:1s},{id:\"depth_strider\",lvl:3s},{id:\"feather_falling\",lvl:5s},{id:\"protection\",lvl:2s},{id:\"projectile_protection\",lvl:2s}],AttributeModifiers:[{AttributeName:\"generic.movement_speed\",Name:\"generic.movement_speed\",Slot:\"head\",Operation:1,Amount:0.2d,UUID:[I;2032902480,-213824230,-1679967255,-977241219]},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Slot:\"head\",Operation:0,Amount:0.10d,UUID:[I;-175730613,-1799009612,-1666383447,-806773003]},{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"head\",Operation:0,Amount:4.0d,UUID:[I;1357404337,1220299345,-1241379974,1274087135]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"head\",Operation:0,Amount:4.0d,UUID:[I;-509474114,-944028974,-1684738383,-270132766]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"head\",Operation:1,Amount:-0.2d,UUID:[I;-1104091537,-1289860570,-1547355468,1118852393]}],display:{color:65298,Name:'{\"text\":\"\\\\u00a7l\\\\u00a74破灭王冠\"}'}}",1));

        this.Effects.add((new MobEffectInstance(MobEffectRegistry.PLANAR_SIGHT.get(), 9999999, 2, false, false, false)));
     //   this.Effects.add((new MobEffectInstance(MobEffects.BLINDNESS, 9999999, 2, false, false, false)));
        this.Effects.add((new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 9999999, 0, false, false, false)));


    }
    @SubscribeEvent
    public static void Swing(SwingEvent event){
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            extracted(player);
        }
    }

    private static void extracted(Player player) {
        if (!(player.getMainHandItem().getItem() instanceof SwordItem)) return;
        if (player.getAttackStrengthScale(0) <= 0.6) return;
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (capability.career == null || !capability.career.equals("TheDestroyer")) return;
            int theDestroyerP = player.getPersistentData().getInt("TheDestroyerP");
            if (theDestroyerP >= 2) {
                if (player instanceof ServerPlayer serverPlayer) {
                    SpellRegistry.BLOOD_NEEDLES_SPELL.get().attemptInitiateCast(ItemStack.EMPTY, 2, player.level(), serverPlayer, CastSource.COMMAND, false, "command");
                }
                player.getPersistentData().putInt("TheDestroyerP", 0);
            } else player.getPersistentData().putInt("TheDestroyerP", theDestroyerP + 1);
        });
    }

    public static void addEffectB(LivingEntity entity,int em){
        if (entity.hasEffect(MobEffects.UNLUCK)) {
            int am = entity.getEffect(MobEffects.UNLUCK).getAmplifier() + em;
            entity.removeEffect(MobEffects.UNLUCK);
            entity.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 20 * 8, am, false, false, false));
        } else
            entity.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 20 * 8, 0, false, false, false));

    }
    @SubscribeEvent
    public static void DamageBoost(LivingHurtEvent event) {
        if (event.getEntity() instanceof VoidTentacle){
            event.setCanceled(true);
            return;
        }
        if (event.getSource().getDirectEntity() instanceof BloodSlashProjectile bloodSlashProjectile) {
            if (bloodSlashProjectile.getPersistentData().getBoolean("AnnihilationSlash")) {
                LivingEntity entity = event.getEntity();
                if (entity != null) {
                    entity.invulnerableTime = 0;
                    addEffectB(entity,1);
                }
            }
        }
        if (event.getSource().getEntity() instanceof Player player) {
            if (!event.getSource().isIndirect()) extracted(player);
            player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                if (capability.career==null || !capability.career.equals("TheDestroyer"))return;
                LivingEntity entity = event.getEntity();
                player.heal(0.2f);
                if (entity != null) {
                    if (entity.hasEffect(MobEffects.BLINDNESS)) {
                        event.setAmount((float) (event.getAmount() * (0.85 + Math.min(0.5, 0.05 * (entity.getEffect(MobEffects.BLINDNESS).getAmplifier() + 1)))));
                    }
                }
            });
        }
    }
}
