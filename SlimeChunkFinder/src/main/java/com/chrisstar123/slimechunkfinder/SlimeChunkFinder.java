package com.chrisstar123.slimechunkfinder;

import com.chrisstar123.slimechunkfinder.command.BuySlimeChunkFinder;
import com.chrisstar123.slimechunkfinder.command.OpenSlimeChunkFinderMenu;
import com.chrisstar123.slimechunkfinder.listener.UseSlimeChunkFinder;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_13_R2.Tuple;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SlimeChunkFinder extends JavaPlugin {

    private static FileConfiguration config;
    private static Economy econ;
    private static SlimeChunkFinder instance;

    public SlimeChunkFinder() {
        instance = this;
    }

    public static SlimeChunkFinder getInstance() {
        if (instance == null) {
            instance = new SlimeChunkFinder();
        }
        return instance;
    }
    @Override
    public void onEnable() {
        generateConfig();
        registerCommands();
        registerEventListeners();

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if(economyProvider != null){
            econ = economyProvider.getProvider();
        }
        getLogger().info("using economy: " + econ.getName());

        getLogger().info("SlimeChunkFinder enabled");
    }

    private void generateConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        config.options().header("Configuration file for SlimeChunkFinder");

        String[] itemcosts = new String[]{
            "1:5000",
            "10:45000",
            "50:200000"
        };

        config.addDefault("itemcosts", itemcosts);
        config.addDefault("itemmaterial", "stick");

        config.options().copyDefaults(true);

        try {
            config.save(configFile);
        } catch (Exception ex) {

        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void registerCommands() {
        getCommand("BuySlimeChunkFinders").setExecutor(new BuySlimeChunkFinder());
        getCommand("BuySlimeChunkFinder").setExecutor(new OpenSlimeChunkFinderMenu());
    }

    private void registerEventListeners() {
        getServer().getPluginManager().registerEvents(new UseSlimeChunkFinder(), this);
    }

    public static FileConfiguration getConfiguration() {
        return config;
    }

    public static Economy getEcon() {
        return econ;
    }
}
