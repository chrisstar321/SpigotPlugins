package com.chrisstar321.treecapitator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class TreeCapitator extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new OnBlockBreakListener(), this);

        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin disabled.");
    }

    public static void Treecap(Block block, Player player, ItemStack axe) {
        Set<Block> treeBlocks = new HashSet<Block>();
        treeBlocks.add(block);
        Material check = block.getType();

        boolean floodFill = true;
        while (floodFill) {
            floodFill = false;
            Set<Block> toAdd = new HashSet<Block>();
            toAdd.addAll(treeBlocks);

            for (Block b : treeBlocks) {
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            Block toCheck = b.getRelative(x, y, z);
                            if (toCheck.getType().equals(check)) {
                                if (toAdd.add(toCheck)) floodFill = true;
                            }
                        }
                    }
                }
            }

            treeBlocks.addAll(toAdd);
        }

        ItemMeta axeMeta = axe.getItemMeta();
        Damageable axeDamageable = (Damageable) axeMeta;
        boolean axeBroken = false;
        for (Block b : treeBlocks) {
            b.breakNaturally(axe);

            axeDamageable.setDamage(axeDamageable.getDamage() + 1);
            if (axeDamageable.getDamage() >= axe.getType().getMaxDurability()) {
                player.getInventory().remove(axe);
                axeBroken = true;
                break;
            }
        }
        if (!axeBroken) axe.setItemMeta((ItemMeta) axeDamageable);
        player.updateInventory();
    }
}
