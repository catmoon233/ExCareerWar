package net.exmo.excareerwar.network;


import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.client.BlindnessRenderEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public record BlindnessChangeMessage(UUID uuid, int level) {

    @SubscribeEvent
    public static void effectUpdate(MobEffectEvent.Added event){
        LivingEntity livingEntity = event.getEntity();
        if (event.getEffectInstance().getEffect().equals(MobEffects.BLINDNESS)){
            Level level = livingEntity.level();
            Vec3 _center = livingEntity.position();
            List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(30), a -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (Entity entityiterator : _entfound) {
                if (entityiterator instanceof ServerPlayer le) {
                    CareerWarModVariables.PlayerVariables playerVariables = le.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY).orElse(null);
                    if (playerVariables!=null) {
                     if (!"TheDestroyer".equals(playerVariables.career)) {
                         BlindnessChangeMessage mingLiChangeMessage = new BlindnessChangeMessage(livingEntity.getUUID(), -1);
                         Excareerwar.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> le), mingLiChangeMessage);
                     }
                        MobEffectInstance effect = livingEntity.getEffect(MobEffects.BLINDNESS);
                        if (effect != null) {
                            BlindnessChangeMessage mingLiChangeMessage = new BlindnessChangeMessage(livingEntity.getUUID(), effect.getAmplifier());
                            Excareerwar.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> le), mingLiChangeMessage);
                        }
                    }
                }
            }
        }
    }

        public static void encode(BlindnessChangeMessage msg, FriendlyByteBuf buffer) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("level", msg.level);
            tag.putUUID("uuid", msg.uuid);
            buffer.writeNbt(tag);


        }
        public static void handle(BlindnessChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> BlindnessRenderEvent.MingLiMap.put(msg.uuid,msg.level));
            ctx.get().setPacketHandled(true);
        }
        public static BlindnessChangeMessage decode(FriendlyByteBuf buffer) {
            CompoundTag tag = buffer.readNbt();
            return new BlindnessChangeMessage(tag.getUUID("uuid"),tag.getInt("level"));
        }

    }

