
package net.exmo.excareerwar.content.careers;


import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;
@AutoInit
@Mod.EventBusSubscriber
public class Puppeteer extends Career {
    public Puppeteer() throws Exception {
        super("Puppeteer");
        this.LocalDescription = "Puppeteer_d";
        this.bingItems = true;
        this.itemIcon = Items.PLAYER_HEAD;
        this.Skills.add(SkillHandle.getSkill("Summon_Puppets"));
        this.Skills.add(SkillHandle.getSkill("Soul_Flash"));
        this.Skills.add(SkillHandle.getSkill("Soul_Shift"));
        this.IsAlwaysHas = true;
        this.ItemKits.add(new ItemKit(0, "excareerwar:vertical_string_a{}", 1));
        this.ItemKits.add(new ItemKit(EquipmentSlot.LEGS, "minecraft:leather_leggings{Unbreakable:true,Enchantments:[{id:\"feather_falling\",lvl:3s},{id:\"binding_curse\",lvl:1s},{id:\"depth_strider\",lvl:1s},{id:\"protection\",lvl:5s},{id:\"blast_protection\",lvl:1s}],AttributeModifiers:[{AttributeName:\"generic.max_health\",Name:\"generic.max_health\",Slot:\"legs\",Operation:0,Amount:2.0d,UUID:[I;-314118950,1648116038,-1736053350,218469770]},{AttributeName:\"generic.armor\",Name:\"generic.armor\",Slot:\"legs\",Operation:0,Amount:5.0d,UUID:[I;-800020301,-512407496,-1722431928,994496122]}],display:{color:16777215,Name:'{\"text\":\"\\\\u00a77\\\\u00a7l演绎下装\"}'}}", 1));
    }
    @SubscribeEvent
    public static void  displayHealth(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

    }
}
