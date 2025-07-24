package net.exmo.excareerwar.content;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static net.exmo.excareerwar.content.PlayerCareerProficiencyManager.FILE_PATH;
import static net.exmo.excareerwar.network.CareerWarModVariables.*;

@Mod.EventBusSubscriber
public class ProficiencyHandle  {
    public static PlayerCareerProficiencyManager playerCareerProficiencyManager = new PlayerCareerProficiencyManager();
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class initClass{
        @SubscribeEvent
        public static void initP(FMLCommonSetupEvent event){
            playerCareerProficiencyManager.loadFromFile();
        }
    }
        public static void saveToJsonFile(String playerName, PlayerVariables playerVars, String filePath) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (Writer writer = new FileWriter(filePath)) {
                Map<String, PlayerVariables> playerData = new HashMap<>();
                playerData.put(playerName, playerVars);
                gson.toJson(playerData, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static PlayerVariables loadFromJsonFile(String playerName, String filePath) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, PlayerVariables>>(){}.getType();
            try (Reader reader = new FileReader(filePath)) {
                Map<String, PlayerVariables> playerData = gson.fromJson(reader, type);
                if (playerData == null) {
                    return null;
                }
                return playerData != null ? playerData.get(playerName) : null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        public static void saveProficiencyToCap(Player player){
            if (player.level().isClientSide)return;
            PlayerVariables playerVars = ((Player) player).getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
           playerCareerProficiencyManager.loadFromFile();
           playerVars.CareerProficiency = playerCareerProficiencyManager.getPlayerProficiencies().get(player.getName().getString());
           playerVars.syncPlayerVariables(player);
        }
        public static void addKillRecord(Player player,int amount){
            PlayerVariables playerVars = ((Player) player).getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
            playerVars.KillRecord +=amount;
            playerVars.syncPlayerVariables(player);
        }
        public static int getKillRecord(Player player){
            PlayerVariables playerVars = ((Player) player).getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
            return playerVars.KillRecord;
        }
        public static void setKillRecord(Player player,int amount){

            PlayerVariables playerVars = ((Player) player).getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
            playerVars.KillRecord = amount;
            playerVars.syncPlayerVariables(player);
        }
        @SubscribeEvent
        public static void useSkill(UseSkillEvent event){
            LivingEntity entity = event.getEntity();
            PlayerVariables playerVars = ((Player) entity).getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
            Integer proficiency = playerCareerProficiencyManager.getProficiency(entity.getName().getString(), playerVars.career);
            if (proficiency==null)proficiency=0;
            playerCareerProficiencyManager.setProficiency(entity.getName().getString(), playerVars.career, (int) ((int) Math.abs(event.Cooldown *0.01)+0.3) + proficiency);
            saveProficiencyToCap((Player) entity);
        }
        @SubscribeEvent
        public static void KilledPlayer(LivingDeathEvent event){

            if (event.getSource().getEntity() instanceof Player player) {
                if (player==null)return;
                if (event.getEntity()==null)return;
                if (!(event.getEntity() instanceof Player player1))return;
                PlayerVariables playerVars = ((Player) player).getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
                addKillRecord(player, 1);
                int killRecord = getKillRecord(player);


                playerCareerProficiencyManager.setProficiency(player.getName().getString(),playerVars.career , (int) Math.abs(getKillRecord(player)*1.5+10));
                playerCareerProficiencyManager.setProficiency(player1.getName().getString(),playerVars.career , (int) Math.abs(getKillRecord(player1)*1.2+2));
                saveProficiencyToCap(player);
                saveProficiencyToCap(player1);
                if (killRecord >=3){
                    if (player.level() instanceof ServerLevel level){
                        level.players().forEach(p->{
                            p.sendSystemMessage(Component.translatable("message.excareerwar.killrecord",player.getDisplayName(), killRecord));
                        });
                    }
                }
            }


        }
        @SubscribeEvent
        public static void spawn(PlayerEvent.PlayerRespawnEvent event){
            Player entity = event.getEntity();
            if (entity.level().isClientSide)return;
            saveProficiencyToCap(entity);
        }

}
