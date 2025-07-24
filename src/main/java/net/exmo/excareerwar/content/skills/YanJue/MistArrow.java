package net.exmo.excareerwar.content.skills.YanJue;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.exmo.excareerwar.content.CareerSkill;
import net.exmo.excareerwar.entity.MistArrowEntity;
import net.exmo.excareerwar.init.CareerWarModItems;
import net.exmo.excareerwar.util.AutoInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

@AutoInit
public class MistArrow extends CareerSkill {
    public MistArrow() {
        super("MistArrow");
        this.CoolDown = (int) (22 * 40);
        // 20秒冷却
        this.Icon = CareerWarModItems.MistArrow.get();
        this.LocalDescription = "skill.mist_arrow.description";
    }

    @Override
    public void use(Player player) {
        if (isInCooldown(player)) return;
        
        // 创建箭矢实体
        ItemStack itemStack = new ItemStack(CareerWarModItems.MistArrow.get());
        itemStack.getOrCreateTag().putBoolean("Bind", true);
        EnderArrow.moveItemAndAdd(player, itemStack);
        
        // 附加施法效果
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 1));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2));
        player.heal(4);
        super.use(player);
    }

    // 烟雾持续时间随机化方法
    private int getRandomDuration() {
        return 100 + new java.util.Random().nextInt(41);
        // 5-7秒（以tick为单位）
    }
}