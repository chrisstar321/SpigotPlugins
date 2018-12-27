package me.chrisstar123;

import me.chrisstar123.commands.XpBottle;
import me.chrisstar123.event.ThrowBottleEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class XpBottler extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("xpbottle").setExecutor(new XpBottle());
        getServer().getPluginManager().registerEvents(new ThrowBottleEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
