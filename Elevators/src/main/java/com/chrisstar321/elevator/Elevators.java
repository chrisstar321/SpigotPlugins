package com.chrisstar321.elevator;

import com.chrisstar321.elevator.listeners.PlayerCrouchListener;
import com.chrisstar321.elevator.listeners.PlayerJumpListener;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Elevators extends JavaPlugin {

    private static FileConfiguration config;
    public static final Material BACKUP_ELEVATOR_MATERIAL = Material.IRON_BLOCK;

    @Override
    public void onEnable() {
        generateConfig();
        registerEvents();
    }

    @Override
    public void onDisable() {

    }

    private void generateConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        config.options().header("Configuration file for Elevators plugin");

        String elevatorMat = Material.IRON_BLOCK.name();

        config.addDefault("elevator_block", elevatorMat);

        config.options().copyDefaults(true);

        try {
            config.save(configFile);
        } catch (Exception ex) {

        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerJumpListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerCrouchListener(), this);
    }

    public static FileConfiguration _getConfig() {
        return config;
    }
}
