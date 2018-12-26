package com.chrisstar123.slimechunkfinder.command;

import com.chrisstar123.slimechunkfinder.SlimeChunkFinder;
import com.chrisstar123.slimechunkfinder.util.Utility;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class BuySlimeChunkFinder implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player) || strings.length < 1) {
            Logger.getGlobal().info("[BuySlimeChunkFinder] Amount of parameters: " + strings.length);
            return true;
        }
        Player player = (Player) commandSender;

        //Parse 1st argument as quantity
        int amount = 0;
        try {
            amount = Integer.parseInt(strings[0]);
        } catch (Exception ex) {
            commandSender.sendMessage("Please enter an amount to buy");
            return true;
        }

        FileConfiguration config = SlimeChunkFinder.getConfiguration();
        List<String> itemcosts = (List<String>) config.get("itemcosts");

        for (String itemcost: itemcosts) {
            String[] _s = itemcost.split(":");
            if (Integer.parseInt(_s[0]) == amount) {
                if (buySlimeChunkFinder(player, Integer.parseInt(_s[0]), Integer.parseInt(_s[1]))) {
                    player.sendMessage("You successfully bought " + amount + " Slime Chunk Finders");
                }
                return true;
            }
        }

        player.sendMessage("That amount was not a valid amount");
        return true;
    }

    private boolean buySlimeChunkFinder(Player player, int amount, int cost) {
        Economy econ = SlimeChunkFinder.getEcon();

        double bal = econ.getBalance(player);
        if (bal >= cost) {
            //Give player x amount of slime chunk finders
            //withdraw cost from player's balance
            ItemStack item = Utility.createSlimeChunkFinder(amount);
            player.getInventory().addItem(item);
            econ.withdrawPlayer(player, cost);
            return true;
        } else {
            player.sendMessage("You don't have enough money to buy that many!");
            return false;
        }
    }
}
