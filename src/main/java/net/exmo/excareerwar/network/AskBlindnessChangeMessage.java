
package net.exmo.excareerwar.network;


import net.exmo.excareerwar.Excareerwar;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public record AskBlindnessChangeMessage(UUID uuid) {


        public static void encode(AskBlindnessChangeMessage msg, FriendlyByteBuf buffer) {
            CompoundTag tag = new CompoundTag();

            tag.putUUID("uuid", msg.uuid);
            buffer.writeNbt(tag);


        }
        public static void handle(AskBlindnessChangeMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer sender = ctx.get().getSender();
                Entity entity = sender.serverLevel().getEntity(msg.uuid);
                if (entity instanceof  LivingEntity livingEntity) {
                    if (livingEntity.hasEffect(MobEffects.BLINDNESS)) {
                        BlindnessChangeMessage mingLiChangeMessage = new BlindnessChangeMessage(msg.uuid, livingEntity.getEffect(MobEffects.BLINDNESS).getAmplifier());
                        Excareerwar.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) sender), mingLiChangeMessage);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
        public static AskBlindnessChangeMessage decode(FriendlyByteBuf buffer) {
            CompoundTag tag = buffer.readNbt();
            return new AskBlindnessChangeMessage(tag.getUUID("uuid"));
        }

    }

