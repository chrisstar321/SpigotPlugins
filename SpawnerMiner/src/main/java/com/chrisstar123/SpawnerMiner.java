package com.chrisstar123;

import com.chrisstar123.event.PlaceSpawner;
import com.chrisstar123.event.SpawnerBreak;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnerMiner extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SpawnerBreak(), this);
        getServer().getPluginManager().registerEvents(new PlaceSpawner(), this);
    }

    @Override
    public void onDisable() {

    }
}
