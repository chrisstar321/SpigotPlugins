package com.chrisstar123.chestsorter.command;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class SortChest implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player) commandSender;

        //Get block player is looking at
        HashSet<Material> filter = new HashSet<Material>();
        filter.add(Material.CHEST);
        Block target = player.getTargetBlock(null, 5);

        if (target.getType() != Material.CHEST) {
            return true;
        }

        //Get chest contents
        Chest chest = (Chest) target.getState();
        Inventory chestInv = chest.getInventory();
        ItemStack[] items;
        if (chestInv instanceof DoubleChestInventory) {
            Logger.getGlobal().info("Double chest");
            items = ((DoubleChestInventory) chestInv).getHolder().getInventory().getContents();
        } else {
            items = chestInv.getContents();
        }

        //Filter out null itemstacks
        List<ItemStack> itemsList = new ArrayList<>(Arrays.asList(items));
        itemsList.removeAll(Collections.singleton(null));
        items = itemsList.toArray(new ItemStack[itemsList.size()]);

        //Stack items together
        items = stackItems(items);

        //Sort items and put back in chest
        sortItems(items);
        items = removeAir(items);

        if (chestInv instanceof DoubleChestInventory) {
            //Split list in parts no longer than 27
            ItemStack[] chestA = Arrays.copyOfRange(items, 0, 27);
            ItemStack[] chestB = items.length > 26 ? Arrays.copyOfRange(items, 27, 54) : new ItemStack[]{};

            DoubleChestInventory dChest = (DoubleChestInventory) chestInv;
            dChest.getLeftSide().setContents(chestA);
            dChest.getRightSide().setContents(chestB);
        } else {
            //just set the contents
            chestInv.setContents(items);
        }


        return true;
    }

    private ItemStack[] stackItems(ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < i; j++) {
                if (items[i].isSimilar(items[j])) {
                    //Stack the items and continue
                    //items[i] is the item we're stacking onto items[j]
                    int maxStackSize = items[j].getMaxStackSize();
                    int total = items[j].getAmount() + items[i].getAmount();
                    if (total > maxStackSize) {
                        items[i].setAmount(items[i].getAmount() - (maxStackSize - items[j].getAmount()));
                        items[j].setAmount(maxStackSize);
                    } else {
                        items[j].setAmount(items[i].getAmount() + items[j].getAmount());
                        items[i].setAmount(0);
                    }
                    if (items[i].getAmount() == 0) {
                        break;
                    }
                }
            }
        }

        return items;
    }

    private void sortItems(ItemStack[] items) {
        while (!sorted(items)) {
            for (int i = 1; i < items.length; i++) {
                if (items[i].getType().ordinal() < items[i - 1].getType().ordinal()) {
                    ItemStack temp = items[i];
                    items[i] = items[i - 1];
                    items[i - 1] = temp;
                }
            }
        }
    }

    private boolean sorted(ItemStack[] items) {
        for (int i = 1; i < items.length; i++) {
            int a = items[i].getType().ordinal();
            int b = items[i-1].getType().ordinal();
            if (items[i].getType().ordinal() < items[i-1].getType().ordinal()) {
                return false;
            }
        }
        return true;
    }

    private ItemStack[] removeAir(ItemStack[] items) {
        List<ItemStack> itemsList = new ArrayList<>(Arrays.asList(items));
        List<ItemStack> toRemove = new ArrayList<>();
        for (ItemStack i : itemsList) {
            if (i.getType().equals(Material.AIR)) {
                toRemove.add(i);
            }
        }
        itemsList.removeAll(toRemove);

        return itemsList.toArray(new ItemStack[itemsList.size()]);
    }
}
