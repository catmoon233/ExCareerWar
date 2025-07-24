package net.exmo.excareerwar.network;

import net.exmo.excareerwar.client.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DashMessage2 {

        public final double dashDistance;

        public DashMessage2(double dashDistance) {

            this.dashDistance = dashDistance;
        }

        public static void encode(DashMessage2 msg, FriendlyByteBuf buffer) {
            buffer.writeDouble(msg.dashDistance);

        }
        public static void handle(DashMessage2 msg, Supplier<NetworkEvent.Context> ctx) {

            ctx.get().enqueueWork(()-> ClientPacketHandler.handledash(msg));
            ctx.get().setPacketHandled(true);
        }
        public static DashMessage2 decode(FriendlyByteBuf buffer) {
            return new DashMessage2( buffer.readDouble());
        }




    }

