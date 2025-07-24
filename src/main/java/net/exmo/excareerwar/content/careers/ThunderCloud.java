package net.exmo.excareerwar.content.careers;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.content.events.SwingEvent;
import net.exmo.excareerwar.init.CareerWarModMobEffects;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@AutoInit
@Mod.EventBusSubscriber
public class ThunderCloud extends Career {
    public ThunderCloud() throws Exception {
        super("ThunderCloud");
        this.itemIcon = Items.LIGHT_BLUE_BANNER;
        this.bingItems = true;
        this.IsAlwaysHas = true;
        this.LocalDescription = "ThunderCloud_d";
        this.Effects.add((new MobEffectInstance(CareerWarModMobEffects.FastCastEMobEffect.get(), 9999999, 2, false, false, false)));


        this.Skills.add(SkillHandle.getSkill(SpellRegistry.CHAIN_LIGHTNING_SPELL.get().getSpellName()));
        this.Skills.add(SkillHandle.getSkill(SpellRegistry.SHOCKWAVE_SPELL.get().getSpellName()));
        this.Skills.add(SkillHandle.getSkill(SpellRegistry.LIGHTNING_LANCE_SPELL.get().getSpellName()));
        this.Skills.add(SkillHandle.getSkill(SpellRegistry.ASCENSION_SPELL.get().getSpellName()));
        this.Skills.add(SkillHandle.getSkill(SpellRegistry.LIGHTNING_BOLT_SPELL.get().getSpellName()));
        this.ItemKits.add(new ItemKit(EquipmentSlot.LEGS, "minecraft:leather_leggings{Unbreakable:true,Enchantments:[{id:\"feather_falling\",lvl:1s},{id:\"binding_curse\",lvl:1s},{id:\"depth_strider\",lvl:1s},{id:\"protection\",lvl:2s},{id:\"blast_protection\",lvl:1s}],AttributeModifiers:[{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"legs\",Operation:0,Amount:2.0d,UUID:[I;-314118950,1648116038,-1736053350,218469771]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"legs\",Operation:0,Amount:3.0d,UUID:[I;-800020301,-512407496,-1722431928,994496124]}],display:{color:5066239,Name:'{\"text\":\"\\\\u00a77\\\\u00a79雷云战裤\"}'}}", 1));
        this.ItemKits.add(new ItemKit(EquipmentSlot.CHEST, "minecraft:leather_chestplate{Unbreakable:true,Enchantments:[{id:\"feather_falling\",lvl:2s},{id:\"binding_curse\",lvl:1s},{id:\"depth_strider\",lvl:1s},{id:\"protection\",lvl:2s},{id:\"blast_protection\",lvl:1s}],AttributeModifiers:[{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"chest\",Operation:0,Amount:2.0d,UUID:[I;-314118950,1648116038,-1736053350,218469772]}," +
                "{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"chest\",Operation:0,Amount:3.5d,UUID:[I;-800020301,-512407496,-1722431928,994496123]}," +
                "{AttributeName:\"generic.attack_damage\",Name:\"generic.attack_damage\",Slot:\"chest\",Operation:0,Amount:2d,UUID:[I;-800020301,-512407496,-1722431928,994496124]}" +
                "],display:{color:5066239,Name:'{\"text\":\"\\\\u00a77\\\\u00a79雷云战衣\"}'}}", 1));

    }
    @SubscribeEvent
    public static void atDamage(LivingHurtEvent e){
        Entity player = e.getSource().getEntity();
        if (!(player instanceof Player)) return;
        player.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            if (capability.career==null)return;
            if (capability.career.equals("ThunderCloud")) {
                e.setAmount(e.getAmount()*0.7f);
            }
        });
    }
}
