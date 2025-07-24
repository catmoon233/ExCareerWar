package net.exmo.excareerwar.content.careers;

import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

import java.awt.*;

@AutoInit
public class ShuShuXia extends Career {
    public ShuShuXia() throws Exception {
        super("ShuShuXia");
        this.itemIcon = CareerWarModItems.Mouse_Chui.get();
        this.bingItems= true;
        this.LocalDescription = "ShuShuXia_d";
        this.IsAlwaysHas= true;
        this.ItemKits.add(new ItemKit(0, "excareerwar:mouse_chui{Unbreakable:true}", 1));
        this.ItemKits.add(new ItemKit(EquipmentSlot.CHEST,"minecraft:leather_chestplate{Unbreakable:true,display:{color:" + Color.orange.getRGB() + ",Name:'{\"text\":\"\\\\u00a7l\\\\u00a76鼠衣\"}'},HideFlags:120}",1));
        this.ItemKits.add(new ItemKit(EquipmentSlot.LEGS,"minecraft:leather_leggings{Unbreakable:true,display:{color:" + Color.orange.getRGB() + ",Name:'{\"text\":\"\\\\u00a7l\\\\u00a76鼠裤\"}'},HideFlags:120}",1));
        this.ItemKits.add(new ItemKit(EquipmentSlot.FEET,"minecraft:leather_boots{Unbreakable:true,display:{color:" + Color.orange.getRGB() + ",Name:'{\"text\":\"\\\\u00a7l\\\\u00a76鼠鞋\"}'},HideFlags:120}",1));
        this.ItemKits.add(new ItemKit(EquipmentSlot.HEAD,"minecraft:leather_helmet{Unbreakable:true,display:{color:" + Color.orange.getRGB() + ",Name:'{\"text\":\"\\\\u00a7l\\\\u00a76鼠帽\"}'},HideFlags:120}",1));


    }

}
