package net.exmo.excareerwar.network;

import net.exmo.excareerwar.client.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DashMessage {
        public final double dy;
        public final double dashDistance;

        public DashMessage(double dy, double dashDistance) {
            this.dy = dy;
            this.dashDistance = dashDistance;
        }

        public static void encode(DashMessage msg, FriendlyByteBuf buffer) {
            buffer.writeDouble(msg.dy);
            buffer.writeDouble(msg.dashDistance);

        }
        public static void handle(DashMessage msg, Supplier<NetworkEvent.Context> ctx) {

            ctx.get().enqueueWork(()-> ClientPacketHandler.handledash(msg));
            ctx.get().setPacketHandled(true);
        }
        public static DashMessage decode(FriendlyByteBuf buffer) {
            return new DashMessage( buffer.readDouble(), buffer.readDouble());
        }



        public static void vmove(LivingEntity livingEntity, double dy, double dashDistance) {
            float yaw = livingEntity.getYRot();
            double dx = -Math.sin(Math.toRadians(yaw)) * dashDistance;
            double dz = Math.cos(Math.toRadians(yaw)) * dashDistance;
            livingEntity.addDeltaMovement(new Vec3(dx, dy, dz));
        }
    }

