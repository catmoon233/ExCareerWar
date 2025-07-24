package net.exmo.excareerwar;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mod.EventBusSubscriber(modid = Excareerwar.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue DEATH_COOLDOWN_OK = BUILDER.comment("死亡后冷却不重置").define("DEATH_COOLDOWN_OK", true);


    static final ForgeConfigSpec SPEC = BUILDER.build();




    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {

    }
}
