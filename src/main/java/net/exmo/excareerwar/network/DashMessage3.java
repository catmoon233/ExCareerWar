package net.exmo.excareerwar.network;

import net.exmo.excareerwar.client.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DashMessage3 {

        public final double dashDistance;

        public DashMessage3(double dashDistance) {

            this.dashDistance = dashDistance;
        }

        public static void encode(DashMessage3 msg, FriendlyByteBuf buffer) {
            buffer.writeDouble(msg.dashDistance);

        }
        public static void handle(DashMessage3 msg, Supplier<NetworkEvent.Context> ctx) {

            ctx.get().enqueueWork(()-> ClientPacketHandler.handledash(msg));
            ctx.get().setPacketHandled(true);
        }
        public static DashMessage3 decode(FriendlyByteBuf buffer) {
            return new DashMessage3( buffer.readDouble());
        }




    }

