package net.exmo.excareerwar.content.skills.YanJue;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@AutoInit
public class EnderArrow extends CareerSkill {
    public EnderArrow() {
        super("EnderArrow");
        this.CoolDown = (int) (16 * 40);
        this.Icon = CareerWarModItems.EnderArrow.get();
        this.LocalDescription = "EnderArrow_d";
    }

    public static void moveItemAndAdd(Player player, ItemStack itemStack){
        Inventory inventory = player.getInventory();
        
        // 保存第七格物品
        ItemStack slot7 = inventory.getItem(6);
        ItemStack slot8 = inventory.getItem(7);
       // ItemStack slot9 = inventory.getItem(8);
        
        // 仅在物品存在时后移（避免空物品覆盖槽位）
        if (!slot7.isEmpty()) {
            inventory.setItem(7, slot7);
        }
        if (!slot8.isEmpty()) {
            inventory.setItem(8, slot8);
        }
        
        // 添加新物品到第七格
        inventory.setItem(6, itemStack);
    }
    @Override
    public void use(Player player) {
        if (isInCooldown(player))return;
        ItemStack itemStack = new ItemStack(CareerWarModItems.EnderArrow.get());
        itemStack.getOrCreateTag().putBoolean("Bind",true);
        EnderArrow.moveItemAndAdd(player, itemStack);
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 60,2));
        player.heal(4);
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 60,1));
        super.use(player);
    }
}
