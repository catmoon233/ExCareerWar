package net.exmo.excareerwar.network;

import net.exmo.excareerwar.client.ClientPacketHandler;
import net.exmo.excareerwar.client.screens.SkillBarOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UseSkillMessage(String skillName) {



        public UseSkillMessage(String skillName) {
            this.skillName = skillName;
        }

        public static void encode(UseSkillMessage msg, FriendlyByteBuf buffer) {
            buffer.writeUtf(msg.skillName);


        }
        public static void handle(UseSkillMessage msg, Supplier<NetworkEvent.Context> ctx) {

            ctx.get().enqueueWork(()-> SkillBarOverlay.triggerSkillAnimation(msg.skillName));
            ctx.get().setPacketHandled(true);
        }
        public static UseSkillMessage decode(FriendlyByteBuf buffer) {
            return new UseSkillMessage( buffer.readUtf());
        }




    }

