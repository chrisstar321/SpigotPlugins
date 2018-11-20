package com.chrisstar123.chestsorter;

import com.chrisstar123.chestsorter.command.SortChest;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestSorter extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabling ChestSorter");

        getCommand("SortChest").setExecutor(new SortChest());

        getLogger().info("ChestSorter is enabled");
    }

    @Override
    public void onDisable() {

    }
}
