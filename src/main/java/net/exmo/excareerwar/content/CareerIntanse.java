package net.exmo.excareerwar.content;

import net.exmo.exmodifier.util.ItemKit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CareerIntanse extends Career{
    public static String[] careerName = {"Farmer","Miner","Lumberjack","Fisher","Blacksmith","Baker","Butcher","Tailor","Carpenter","Mechanic","Scientist","Mineralogist","Potter","Cook","Mason","Miner","CANYUESAMA","CC_PAST","BC","AIRCAT05","XIAO_DS","LUCK_GIRL"};

    public static int itemCount =0;
    private static final Random rand = new Random();
    public CareerIntanse(String LocalName, ArrayList<CareerSkill> skills) {
        super(LocalName);
        this.Skills.addAll(skills);

        var randomItemIcon = ForgeRegistries.ITEMS.getValues().toArray(new Item[itemCount])[rand.nextInt(itemCount)];
        this.itemIcon = randomItemIcon;
        this.LocalDescription = LocalName+"_desc";
    }
    public  CareerIntanse addArmorItemKit(ArmorItem armorItem) throws Exception {
        this.ItemKits.add(new ItemKit(armorItem.getEquipmentSlot(),ForgeRegistries.ITEMS.getKey(armorItem).getPath()+"{Enchantments:[{lvl:"+CareerHandle.random.nextInt(4)+",id:blast_protection},{lvl:"+CareerHandle.random.nextInt(4)+",id:fire_protection},{lvl:"+CareerHandle.random.nextInt(4)+",id:projectile_protection},{lvl:"+CareerHandle.random.nextInt(4)+",id:protection},{lvl:"+CareerHandle.random.nextInt(4)+",id:thorns}]}",1,true));
        return this;
    }
    public  CareerIntanse addSwordItemKit(SwordItem armorItem) throws Exception {
        this.ItemKits.add(new ItemKit(EquipmentSlot.MAINHAND,ForgeRegistries.ITEMS.getKey(armorItem).getPath()+"{Enchantments:[{lvl:"+CareerHandle.random.nextInt(5)+",id:bane_of_arthropods},{lvl:"+CareerHandle.random.nextInt(2)+",id:fire_aspect},{lvl:"+CareerHandle.random.nextInt(2)+",id:knockback},{lvl:"+CareerHandle.random.nextInt(3)+",id:smite},{lvl:"+CareerHandle.random.nextInt(4)+",id:sharpness}]}",1,true));
        return this;
    }

}
