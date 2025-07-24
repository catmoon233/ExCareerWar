package net.exmo.excareerwar.content.careers;

import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@AutoInit
@Mod.EventBusSubscriber
public class ErlangShen extends Career {

    public ErlangShen() throws Exception {
        super("ErlangShen");
        this.itemIcon = CareerWarModItems.TIAN_JI.get();
        this.Skills.add(SkillHandle.getSkill("KaiSkill"));
        this.Skills.add(SkillHandle.getSkill("flame_slash"));
        this.Skills.add(SkillHandle.getSkill("RootPurpose"));
        this.bingItems= true;
        this.LocalDescription = "ErlangShen_d";
        this.IsAlwaysHas= true;
        this.Effects.add(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 99999999,0));
        this.Effects.add(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 99999999,0));
        this.Effects.add(new MobEffectInstance(CareerWarModMobEffects.FastCastEMobEffect.get(), 99999999,6));
        this.Effects.add(new MobEffectInstance(MobEffects.JUMP, 99999999,0));
        this.ItemKits.add(new ItemKit(0,"excareerwar:tianji{Unbreakable:true,Enchantments:[{id:\"fire_aspect\",lvl:1s},{id:\"sweeping\",lvl:1s}],AttributeModifiers:[{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"mainhand\",Operation:0,Amount:2.5d,UUID:[I;1673534749,265437927,-1635975972,913088645]},{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"mainhand\",Operation:0,Amount:4d,UUID:[I;161542313,-1020181367,-1704501186,-1320158221]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"mainhand\",Operation:0,Amount:-3d,UUID:[I;-1113774948,-1485027559,-2086690721,-1247416420]}],display:{Name:'{\"text\":\"\\\\u00a7l\\\\u00a76天戟\"}'}}",1,true));
        this.ItemKits.add(new ItemKit(EquipmentSlot.HEAD, "minecraft:golden_helmet{Unbreakable:true,Enchantments:[{id:\"blast_protection\",lvl:1s},{id:\"thorns\",lvl:1s},{id:\"fire_protection\",lvl:1s},{id:\"binding_curse\",lvl:1s},{id:\"depth_strider\",lvl:3s},{id:\"feather_falling\",lvl:10s},{id:\"protection\",lvl:2s},{id:\"projectile_protection\",lvl:1s}],AttributeModifiers:[{AttributeName:\"generic.movement_speed\",Name:\"generic.movement_speed\",Slot:\"head\",Operation:1,Amount:0.15d,UUID:[I;2032902480,-213824230,-1679967255,-977241219]},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Slot:\"head\",Operation:0,Amount:0.01d,UUID:[I;-175730613,-1799009612,-1666383447,-806773003]},{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"head\",Operation:0,Amount:6.0d,UUID:[I;1357404337,1220299345,-1241379974,1274087135]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"head\",Operation:0,Amount:2.0d,UUID:[I;-509474114,-944028974,-1684738383,-270132766]},{AttributeName:\"generic.armor_toughness\",Name:\"generic.armor_toughness\",Slot:\"head\",Operation:0,Amount:5d,UUID:[I;-1104091537,-1289860570,-1547355468,1118852393]}],display:{color:65298,Name:'{\"text\":\"\\\\u00a7l\\\\u00a7e神冠\"}'}}",1));

    }
//    @SubscribeEvent
//    public static void atDamage(LivingHurtEvent e){
//        Entity player = e.getSource().getEntity();
//        if (!(player instanceof Player)) return;
//        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
//            if (capability.career.equals("ErlangShen")) {
//                e.setAmount(e.getAmount()*0.9f);
//            }
//        });
//    }
}
