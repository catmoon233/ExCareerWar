package net.exmo.excareerwar.content.skills.YanJue;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.entity.FenrirArrowEntity;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

@AutoInit
public class FenrirArrow extends CareerSkill {
    public FenrirArrow() {
        super("FenrirArrow");
        this.CoolDown = (int) (16 * 40);
        // 10秒冷却
        this.Icon = CareerWarModItems.FenrirArrow.get();
        this.LocalDescription = "FenrirArrow_d";
    }

    @Override
    public void use(Player player) {
        if (isInCooldown(player)) return;
        
        // 消耗箭矢精华（假设需要1个）
        // if (!consumeEssence(player, 1)) return;
        
        // 创建箭矢

        ItemStack itemStack = new ItemStack(CareerWarModItems.FenrirArrow.get());
        itemStack.getOrCreateTag().putBoolean("Bind",true);
        EnderArrow.moveItemAndAdd(player, itemStack);
        // 施加击退和减速效果
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 40, 1));
        player.heal(4);
        
        super.use(player);
    }
    
    // 消耗箭矢精华的实现（需要根据实际资源系统实现）
    private boolean consumeEssence(Player player, int amount) {
        // 这里需要实现实际的资源消耗逻辑
        return true;
    }
}