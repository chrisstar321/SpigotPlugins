package com.chrisstar123.event;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.logging.Logger;

public class SpawnerBreak implements Listener {

    @EventHandler
    public void onBreakSpawner(BlockBreakEvent event) {
        //First check if the block broken is a spawner
        Block block = event.getBlock();
        if (!(block.getState() instanceof CreatureSpawner)) {
            return;
        }

        //Check if the block was broken with a Silk Touch pickaxe
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
        if (!enchantments.containsKey(Enchantment.SILK_TOUCH)) {
            return;
        }

        //The spawner was mined with a silk touch pick
        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        ItemStack spawnerItem = new ItemStack(Material.SPAWNER, 1);
        spawnerItem.getItemMeta().setDisplayName(spawner.getSpawnedType().name() + " spawner");

        NBTTagCompound spawnerData = new NBTTagCompound();

        //Create spawner data
        spawnerData.setBoolean("SpawnerMinerSpawner", true);
        spawnerData.setString("entity", spawner.getSpawnedType().name());

        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(spawnerItem);
        nmsItem.setTag(spawnerData);

        //Drop spawner item
        block.getLocation().getWorld().dropItem(block.getLocation(), CraftItemStack.asBukkitCopy(nmsItem));
    }

}
