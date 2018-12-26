package com.chrisstar123.slimechunkfinder.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UseSlimeChunkFinder implements Listener {

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Action action = event.getAction();

        if (!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta.getDisplayName().equals("SlimeChunkFinder item")) {
            //Use the item and tell the player whether they are standing in a slime chunk
            item.setAmount(item.getAmount() - 1);
            String message = "Chunk " + player.getLocation().getChunk().getX() + "," + player.getLocation().getChunk().getZ() + " is ";
            message += (player.getLocation().getChunk().isSlimeChunk()) ? "" : "not ";
            message += "a slime chunk";
            player.sendMessage(message);
        }
    }
}
