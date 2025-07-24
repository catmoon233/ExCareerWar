package net.exmo.excareerwar.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.spellcraftgaming.rpghud.gui.hud.element.simple.HudElementHealthSimple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author canyuesama
 */
@Mixin(HudElementHealthSimple.class)
public class SimpleHealthMixin {
    @ModifyVariable(remap = false,method = "drawElement", at = @At("STORE"),ordinal = 5)
    public int modifierSize(int size) {
        if (size==84)
            return 182;
        return size;
    }
    @Redirect(remap = false,method = "drawElement",at = @At(value = "INVOKE",target = "Lnet/spellcraftgaming/rpghud/gui/hud/element/simple/HudElementHealthSimple;drawCustomBar(Lnet/minecraft/client/gui/GuiGraphics;IIIIDIIIII)V"))
    public void drawElement(GuiGraphics guiGraphics, int a, int b, int c, int d, double e, int f, int g, int h, int i ,int j) {
        HudElementHealthSimple.drawCustomBar(guiGraphics,a,b,c,10,e,f,g,h,i,j);
    }
}
