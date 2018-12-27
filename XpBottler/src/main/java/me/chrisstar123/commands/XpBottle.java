package me.chrisstar123.commands;

import me.chrisstar123.util.XPManager;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class XpBottle implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players can execute this command.");
            return true;
        }

        Player player = (Player) sender;

        // Parse arguments
        int xp;
        try {
            int x = 0;
            if (args[0].endsWith("L")) {
                // Parse number as amount of levels
                int levels = Integer.parseInt(args[0].substring(0, args[0].length() - 1));
                if (levels > 16) {
                    x = (int) (2.5 * (levels * levels) - 40.5 * levels + 360);
                }
                else if (levels > 31) {
                    x = (int) (4.5 * (levels*levels) - 162.5 * levels + 2220);
                }else {
                    x = (levels * levels) + (6 * levels);
                }
            } else {
                // Parse number as number of xp
                x = Integer.parseInt(args[0]);
            }
            xp = x;
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Could not bottle xp. Value entered was incorrect");
            return false;
        }
        if (xp <= 0) {
            player.sendMessage("The minimum amount of xp you can bottle is 1.");
            return true;
        }

        int playerXp = XPManager.getTotalExperience(player.getLevel(), player.getExp());

        if (playerXp >= xp) {
            //Give player xp bottle

            int newXp = playerXp - xp;
            Tuple<Integer, Float> newLevel = XPManager.setTotalExperience(newXp);
            player.setLevel(newLevel.a());
            player.setExp(newLevel.b());
            player.setTotalExperience(newXp);
            Inventory inv = player.getInventory();
            net.minecraft.server.v1_13_R2.ItemStack item = createXPBottle(xp);
            inv.addItem(CraftItemStack.asBukkitCopy(item));
        } else {
            player.sendMessage(ChatColor.RED + "You did not have enough xp to bottle.");
        }

        return true;
    }

    private net.minecraft.server.v1_13_R2.ItemStack createXPBottle(float xp) {
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("Splash this xp bottle on the ground to get the xp stored inside.");
        lore.add(xp + " xp stored inside");
        meta.setLore(lore);
        item.setItemMeta(meta);

        net.minecraft.server.v1_13_R2.ItemStack i = CraftItemStack.asNMSCopy(item);

        NBTTagCompound nbt = i.getOrCreateTag();
        nbt.setFloat("exp", xp);
        i.setTag(nbt);

        return i;
    }
}
