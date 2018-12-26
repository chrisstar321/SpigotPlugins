package com.chrisstar123.slimechunkfinder.command;

import com.chrisstar123.slimechunkfinder.SlimeChunkFinder;
import com.chrisstar123.slimechunkfinder.util.IconMenu;
import com.chrisstar123.slimechunkfinder.util.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OpenSlimeChunkFinderMenu implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player) || strings.length > 0) {
            return true;
        }
        Player player = (Player) commandSender;
        FileConfiguration config = SlimeChunkFinder.getConfiguration();
        List<String> itemcosts = (List<String>) config.get("itemcosts");

        IconMenu menu = new IconMenu("Buy SlimeChunkFinders", (int) Math.ceil((float) itemcosts.size() / 9f) * 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                ItemStack item = event.getItem();
                int amount = item.getAmount();

                event.getPlayer().performCommand("BuySlimeChunkFinders " + amount);
            }
        }, SlimeChunkFinder.getInstance());

        int i = 0;
        for (String itemcost : itemcosts) {
            String[] _s = itemcost.split(":");
            int amount = Integer.parseInt(_s[0]);
            int cost = Integer.parseInt(_s[1]);

            ItemStack item = Utility.createSlimeChunkFinder(amount);

            menu.setOption(i, item, "SlimeChunkFinder item", "Buy " + amount + " SlimeChunkFinders for " + cost);

            i++;
        }
        menu.open(player);

        return true;
    }
}
