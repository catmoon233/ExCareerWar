package net.exmo.excareerwar.content.careers;

import java.util.ArrayList;
import java.util.List;

import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.SkillHandle;
import net.exmo.excareerwar.util.AutoInit;
import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@AutoInit
@Mod.EventBusSubscriber
public class Garen extends Career {

    public Garen() throws Exception {
        super("Garen");
        this.IsAlwaysHas = true;
        this.LocalDescription = "勇猛的德玛西亚战士";
        this.bingItems = true;
        this.Skills.add(SkillHandle.getSkill("BladeRush"));
        this.Skills.add(SkillHandle.getSkill("Judgment"));
        this.Skills.add(SkillHandle.getSkill("DemacianJustice"));
        this.Effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10000 * 20, 1, false, false, false));
        this.Effects.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10000 * 20, 0, false, false, false));
        this.itemIcon = Items.GOLDEN_SWORD;
        this.ItemKits.add(new ItemKit(0,"golden_sword{display:{color:16777215,Name:'{\"text\":\"\\\\u00a77\\\\u00a76西格玛荣誉\"}'},AttributeModifiers:[{AttributeName:\"generic.attack_damage\",Amount:6,Slot:mainhand,UUID:[I;-1241121,69540,145226,-139080],Name:1734764217495},{AttributeName:\"generic.attack_speed\",Amount:-3,Slot:mainhand,UUID:[I;-1241121,69840,145226,-139680],Name:1734764217495}],Unbreakable:1}", 1));
        this.ItemKits.add(new ItemKit(EquipmentSlot.CHEST, "golden_chestplate{display:{color:16777215,Name:'{\"text\":\"\\\\u00a77\\\\u00a7e勇士黄金甲\"}'},Enchantments:[{lvl:1,id:blast_protection},{lvl:1,id:binding_curse},{lvl:1,id:feather_falling},{lvl:1,id:projectile_protection},{lvl:1,id:protection}],AttributeModifiers:[{AttributeName:\"generic.armor\",Amount:3,Slot:chest,UUID:[I;-1241121,28140,145226,-56280],Name:1734763970224},{AttributeName:\"generic.max_health\",Amount:2,Slot:chest,UUID:[I;-1241121,28440,145226,-56880],Name:1734763970225}],Unbreakable:1}", 1));
        this.ItemKits.add(new ItemKit(EquipmentSlot.LEGS, "golden_leggings{display:{color:16777215,Name:'{\"text\":\"\\\\u00a77\\\\u00a7e誓死诺言\"}'},Enchantments:[{lvl:1,id:blast_protection},{lvl:1,id:binding_curse},{lvl:1,id:feather_falling},{lvl:1,id:projectile_protection},{lvl:2,id:protection}],AttributeModifiers:[{AttributeName:\"generic.armor\",Amount:5,Slot:legs,UUID:[I;-1241121,49140,145226,-98280],Name:1734764119043},{AttributeName:\"generic.max_health\",Amount:2,Slot:legs,UUID:[I;-1241121,49440,145226,-98880],Name:1734764119043}],Unbreakable:1}", 1));
    }
}