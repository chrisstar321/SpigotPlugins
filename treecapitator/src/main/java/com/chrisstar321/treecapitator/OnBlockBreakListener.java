package com.chrisstar321.treecapitator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class OnBlockBreakListener implements Listener {

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        Material blockBroken = event.getBlock().getType();
        if (!blockBroken.name().toLowerCase().contains("log")) {
            //Block broken was not wood
            return;
        }

        // Block was broken by a player
        //Check player's held item
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (!heldItem.getType().name().toLowerCase().contains("axe")) {
            //Player was not holding an axe
            return;
        }

        TreeCapitator.Treecap(event.getBlock(), player, heldItem);
    }
}
