package com.chrisstar123.event;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

public class PlaceSpawner implements Listener {

    @EventHandler
    public void onSpawnerPlace(BlockPlaceEvent event) {
        //First check if the block placed is a spawner
        Block block = event.getBlockPlaced();
        if (!(block.getState() instanceof CreatureSpawner)) {
            return;
        }

        //Check if the spawner placed is from this plugin
        ItemStack item = event.getItemInHand();
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbt = nmsItem.getOrCreateTag();
        if (!nbt.hasKey("SpawnerMinerSpawner")) {
            //The spawner placed was not from this plugin
            return;
        }

        try {
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            EntityType entity = EntityType.valueOf(nbt.getString("entity"));
            spawner.setSpawnedType(entity);
            spawner.update();
        } catch (Exception e) {
            Logger.getGlobal().warning("Something went wrong when " + event.getPlayer().getDisplayName() + " tried to place a spawner");
            event.getPlayer().sendMessage("Something went wrong trying to place this spawner, contact an admin to get help");
            event.setCancelled(true);
            return;
        }
    }
}
