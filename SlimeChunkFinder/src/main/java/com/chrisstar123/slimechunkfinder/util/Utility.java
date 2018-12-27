package com.chrisstar123.slimechunkfinder.util;

import com.chrisstar123.slimechunkfinder.SlimeChunkFinder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Utility {
    public static ItemStack createSlimeChunkFinder(int amount) {
        FileConfiguration config = SlimeChunkFinder.getConfiguration();

        ItemStack itemStack = new ItemStack(Material.getMaterial(config.getString("itemmaterial").toUpperCase()), amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("SlimeChunkFinder item");
        List<String> lore = Arrays.asList("Right click me to check whether the current chunk is a slime chunk");
        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
