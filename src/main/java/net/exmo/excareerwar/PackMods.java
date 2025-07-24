package net.exmo.excareerwar;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class PackMods {
    public static Map<String,String> paths = new HashMap<>();
    static {
        paths.put("/mods/1.20.1-damage_number-1.3.1.jar","mods/1.20.1-damage_number-1.3.1.jar");
        paths.put("/mods/packetfixer-forge-1.4.2-1.19-to-1.20.1.jar","mods/packetfixer-forge-1.4.2-1.19-to-1.20.1.jar");
        paths.put("/mods/xlpackets-1.18.2-2.1.jar","mods/xlpackets-1.18.2-2.1.jar");
        paths.put("/mods/curios-forge-5.10.0+1.20.1.jar","mods/curios-forge-5.10.0+1.20.1.jar");
        paths.put("/mods/player-animation-lib-forge-1.0.2-rc1+1.20.jar","mods/player-animation-lib-forge-1.0.2-rc1+1.20.jar");
        paths.put("/mods/geckolib-forge-1.20.1-4.4.9.jar","mods/geckolib-forge-1.20.1-4.4.9.jar");
        paths.put("/mods/irons_spellbooks-1.20.1-3.4.0.jar","mods/irons_spellbooks-1.20.1-3.4.0.jar");
        paths.put("/mods/caelus-forge-3.2.0+1.20.1.jar","mods/caelus-forge-3.2.0+1.20.1.jar");
        paths.put("/mods/embeddium-0.3.31+mc1.20.1.jar","mods/embeddium-0.3.31+mc1.20.1.jar");
        paths.put("/mods/cloth-config-11.1.136-forge.jar","mods/cloth-config-11.1.136-forge.jar");
        paths.put("/mods/CustomSkinLoader_ForgeV2-14.21.2.jar","mods/");
        paths.put("/mods/exmodifier-0.036.jar","mods/");
        paths.put("/mods/AttributeFix-Forge-1.20.1-21.0.4.jar","mods/");
        paths.put("/mods/architectury-9.2.14-forge.jar","mods/");
        paths.put("/mods/jei-1.20.1-forge-15.12.2.51.jar","mods/");
        paths.put("/mods/oculus-mc1.20.1-1.7.0.jar","mods/");
        paths.put("/mods/embeddiumplus-1.20.1-v1.2.13.jar","mods/");
        paths.put("/mods/Jade-1.20.1-forge-11.10.1.jar","mods/");
        paths.put("/mods/mcwifipnp-1.7.3-1.20.1-forge.jar","mods/");
        paths.put("/mods/TES-forge-1.20.1-1.5.1.jar","mods/");
        paths.put("/mods/XaerosWorldMap_1.39.0_Forge_1.20.jar","mods/");
        paths.put("/mods/Xaeros_Minimap_24.6.1_Forge_1.20.jar","mods/");
    }
    //public static final Path ConfigPath = FMLPaths.GAMEDIR.get().resolve("config/exmo/entry/mod-normal.json");

    public  void commonSetup(final FMLCommonSetupEvent event) {
        paths.forEach((path, targetPath)->{
            if (!new File(FMLPaths.GAMEDIR.get().resolve(targetPath).toString()).exists()){
                copyResourceToFile(path, path);
            }
        });

    }
    public  void copyResourceToFile(String resourcePath, String targetPath) {
        // 获取Minecraft实例目录
        File mcDir = FMLPaths.GAMEDIR.get().toFile();

        // 构建目标目录
        File targetDir = new File(mcDir, targetPath).getParentFile();

        // 创建目录
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            throw new IllegalStateException("Could not create directory: " + targetDir.getAbsolutePath());
        }

        // 构建目标文件
        File targetFile = new File(targetDir, new File(targetPath).getName());

        // 使用Java NIO来复制文件，简化代码
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy file from " + resourcePath + " to " + targetFile, e);
        }
    }
}
