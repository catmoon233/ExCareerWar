package net.exmo.excareerwar.content.careers;

import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.item.CrossbowPlus;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.events.LivingSwingEvent;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@AutoInit
@Mod.EventBusSubscriber
public class YanJue extends Career {
    public YanJue() throws Exception {
        super("YanJue");
        this.Skills.add(SkillHandle.getSkill("EnderArrow"));
        this.Skills.add(SkillHandle.getSkill("FenrirArrow"));
        this.Skills.add(SkillHandle.getSkill("MistArrow"));
        this.Skills.add(SkillHandle.getSkill("WangJianTianYing"));
        this.Skills.add(SkillHandle.getSkill("ArrowPassive"));
        this.Skills.add(SkillHandle.getSkill("ArrowAggrandizement"));
        this.ItemKits.add(new ItemKit(0, "excareerwar:crossbow_plus{Unbreakable:true,Enchantments:[{id:\"power\",lvl:1s},{id:\"quick_charge\",lvl:2s},{id:\"infinity\",lvl:1s},{id:\"knockback\",lvl:1s},{id:\"knockback\",lvl:2s}],AttributeModifiers:[{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"mainhand\",Operation:0,Amount:-0.5d,UUID:[I;-578286535,1016743318,-1156934988,-576272163]}," +
                "{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Slot:\"mainhand\",Operation:0,Amount:2.5d,UUID:[I;1534268828,1514359241,-2030784481,-1928449843]}]" +
                ",display:{Name:'{\"text\":\"\\\\u00a7a\\\\u00a76辰炎\"}'}}", 1));
        this.ItemKits.add(new ItemKit(EquipmentSlot.HEAD, "irons_spellbooks:shadowwalker_helmet{Unbreakable:true,Enchantments:[{id:\"blast_protection\",lvl:1s},{id:\"fire_protection\",lvl:1s},{id:\"binding_curse\",lvl:1s},{id:\"depth_strider\",lvl:3s},{id:\"feather_falling\",lvl:5s},{id:\"protection\",lvl:1s},{id:\"projectile_protection\",lvl:2s}],AttributeModifiers:[{AttributeName:\"generic.movement_speed\",Name:\"generic.movement_speed\",Slot:\"head\",Operation:1,Amount:0.05d,UUID:[I;2032902480,-213824330,-1679967255,-977241219]},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Slot:\"head\",Operation:0,Amount:0.05d,UUID:[I;-175731613,-1799009612,-1666383447,-806773003]},{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"head\",Operation:-4,Amount:1.0d,UUID:[I;1357404667,1220299345,-1241379974,1274085135]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"head\",Operation:0,Amount:2.5d,UUID:[I;-501474114,-944028974,-1674738383,-270132766]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"head\",Operation:1,Amount:-0.5d,UUID:[I;-1104091537,-1289860770,-1547355458,1118852393]}],display:{color:65298,Name:'{\"text\":\"\\\\u00a7l\\\\u00a78游侠斗篷-上\"}'}}", 1));
        this.ItemKits.add(new ItemKit(EquipmentSlot.CHEST, "irons_spellbooks:shadowwalker_chestplate{Unbreakable:true,Enchantments:[{id:\"blast_protection\",lvl:1s},{id:\"fire_protection\",lvl:1s},{id:\"binding_curse\",lvl:1s},{id:\"depth_strider\",lvl:3s},{id:\"feather_falling\",lvl:5s},{id:\"protection\",lvl:1s},{id:\"projectile_protection\",lvl:1s}],AttributeModifiers:[{AttributeName:\"generic.movement_speed\",Name:\"generic.movement_speed\",Slot:\"chest\",Operation:1,Amount:0.1d,UUID:[I;1032902480,-213824230,-1679967255,-977241219]},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Slot:\"chest\",Operation:0,Amount:0.05d,UUID:[I;-165730613,-1799009612,-1666383447,-806773003]},{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"chest\",Operation:0,Amount:1.5d,UUID:[I;1357404437,1220299345,-1241379974,1274085135]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"chest\",Operation:0,Amount:2.0d,UUID:[I;-509454114,-944028974,-1674738383,-270132766]},{AttributeName:\"generic.attack_speed\",Name:\"generic.attack_speed\",Slot:\"chest\",Operation:1,Amount:-0.2d,UUID:[I;-1104091537,-1289850570,-1547355458,1118852393]}],display:{color:65298,Name:'{\"text\":\"\\\\u00a7l\\\\u00a78游侠斗篷-中\"}'}}", 1));
        this.IsAlwaysHas = true;
        this.bingItems = true;
        this.itemIcon = CareerWarModItems.CrossbowPlus.get();
        this.LocalDescription = "YanJue_d";


    }
    @SubscribeEvent
    public static void AutoReload(LivingSwingEvent event){
        if (event.getEntity() instanceof Player serverPlayer){
            if (!serverPlayer.isShiftKeyDown())return;
            CareerSkill skill = SkillHandle.getSkill("ArrowPassive");
            if (serverPlayer.isSpectator())return;
            if (!skill.setCoolDown(serverPlayer)) return;
            ItemStack mainHandItem = serverPlayer.getMainHandItem();
            if (mainHandItem.getItem() == CareerWarModItems.CrossbowPlus.get()){
                if (!CrossbowPlus.isCharged(mainHandItem)) {
                    serverPlayer.heal(2);
                    CrossbowPlus.addChargedProjectile(mainHandItem, Items.ARROW.getDefaultInstance());
                    CrossbowPlus.setCharged(mainHandItem, true);
                    SoundSource soundsource = serverPlayer instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
                    serverPlayer.level().playSound((Player)null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.ALLAY_ITEM_GIVEN, soundsource, 1.0F, 1.0F / (serverPlayer.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);


                }
                //else mainHandItem.getOrCreateTag().putBoolean("ChargedA", true);
            }
        }
    }
}