package net.exmo.excareerwar;

import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.exmo.excareerwar.init.*;
import net.exmo.excareerwar.item.CrossbowPlus;
import net.exmo.excareerwar.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.slf4j.Logger;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@SuppressWarnings("ALL")
@Mod(Excareerwar.MODID)
public class Excareerwar {

    public static final String MODID = "excareerwar";
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("unchecked")
    public Excareerwar() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

		CareerWarModItems.REGISTRY.register(modEventBus);
		CareerWarModEntities.REGISTRY.register(modEventBus);



        CareerWarModTabs.REGISTRY.register(modEventBus);

        CareerWarModMobEffects.REGISTRY.register(modEventBus);

        CareerWarModParticleTypes.REGISTRY.register(modEventBus);



        PACKET_HANDLER.messageBuilder(DashMessage.class, messageID++)
                .encoder(DashMessage::encode)
                .decoder(DashMessage::decode)
                .consumerMainThread(DashMessage::handle)
                .add();
        PACKET_HANDLER.messageBuilder(DashMessage2.class, messageID++)
                .encoder(DashMessage2::encode)
                .decoder(DashMessage2::decode)
                .consumerMainThread(DashMessage2::handle)
                .add();
        PACKET_HANDLER.messageBuilder(DashMessage3.class, messageID++)
                .encoder(DashMessage3::encode)
                .decoder(DashMessage3::decode)
                .consumerMainThread(DashMessage3::handle)
                .add();
        PACKET_HANDLER.messageBuilder(BlindnessChangeMessage.class, messageID++)
                .encoder(BlindnessChangeMessage::encode)
                .decoder(BlindnessChangeMessage::decode)
                .consumerMainThread(BlindnessChangeMessage::handle)
                .add();
        PACKET_HANDLER.messageBuilder(PlayerRefreshScreenOverMessageMessage.class, messageID++)
                .encoder(PlayerRefreshScreenOverMessageMessage::encode)
                .decoder(PlayerRefreshScreenOverMessageMessage::decode)
                .consumerMainThread(PlayerRefreshScreenOverMessageMessage::handle)
                .add();
		CareerWarModMenus.REGISTRY.register(modEventBus);
        modEventBus.addListener(ClientSetup::setup);
        //modEventBus.addListener(new PackMods()::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        CareerWarSpellRegistry.register(modEventBus);
    }


    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }
}
