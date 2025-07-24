package net.exmo.excareerwar.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class HeavenlyEye extends MobEffect {
    public HeavenlyEye() {
        super(MobEffectCategory.BENEFICIAL, -6684673);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
