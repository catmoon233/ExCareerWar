package net.exmo.excareerwar.client;

import net.exmo.excareerwar.network.DashMessage;
import net.exmo.excareerwar.network.DashMessage2;
import net.exmo.excareerwar.network.DashMessage3;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import static net.exmo.excareerwar.network.DashMessage.vmove;

public class ClientPacketHandler {
    public static void handledash(DashMessage msg){

        LivingEntity entity = Minecraft.getInstance().player;
        vmove(entity, msg.dy, msg.dashDistance);
    }
    public static void handledash(DashMessage2 msg){

        LivingEntity entity = Minecraft.getInstance().player;
        if (entity != null) {
            entity.setDeltaMovement(entity.getLookAngle().scale(msg.dashDistance));
        }
    }
    public static void handledash(DashMessage3 msg){

        LivingEntity entity = Minecraft.getInstance().player;
        if (entity != null) {
            Vec3 deltaMovement = entity.getDeltaMovement();
            entity.setDeltaMovement(new Vec3(deltaMovement.x*msg.dashDistance, 0, deltaMovement.z*msg.dashDistance));
        }
    }
}
