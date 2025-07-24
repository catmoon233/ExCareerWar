package net.exmo.excareerwar.content;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PlayerCareerProficiencyManager {

    public static final String FILE_PATH = String.valueOf(FMLPaths.GAMEDIR.get().resolve("player_proficiencies.json"));
    private Map<String, Map<String, Integer>> playerProficiencies = new HashMap<>();


    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Map<String, Map<String, Integer>> getPlayerProficiencies(){
        return playerProficiencies;
    }

    // Save the player's career proficiency to a JSON file
    public void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(playerProficiencies, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load player career proficiencies from a JSON file
    public void loadFromFile() {
        Type type = new TypeToken<Map<String, Map<String, Integer>>>(){}.getType();
        try (Reader reader = new FileReader(FILE_PATH)) {
            Map<String, Map<String, Integer>> loadedData = gson.fromJson(reader, type);
            if (loadedData != null) {
                playerProficiencies.putAll(loadedData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add or update a player's career proficiency
    public void setProficiency(String playerName, String careerName, int proficiency) {
        playerProficiencies.computeIfAbsent(playerName, k -> new HashMap<>()).put(careerName, proficiency);
        saveToFile(); // Automatically save after each change
    }

    // Get a player's proficiency in a specific career
    public Integer getProficiency(String playerName, String careerName) {
        Map<String, Integer> careerProficiencies = playerProficiencies.get(playerName);
        return careerProficiencies != null ? careerProficiencies.get(careerName) : null;
    }

    // Remove a player's proficiency data
    public void removePlayer(String playerName) {
        playerProficiencies.remove(playerName);
        saveToFile(); // Automatically save after each change
    }
}