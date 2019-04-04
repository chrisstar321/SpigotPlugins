package me.chrisstar123.event;

import me.chrisstar123.util.XPManager;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.Tuple;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ThrowBottleEvent implements Listener {

    @EventHandler
    public void onPlayerThrowBottle(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (!(meta.getLore().contains("Splash this xp bottle on the ground to get the xp stored inside."))) {
            return;
        }
        try {
            net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
            NBTTagCompound nbt = nmsItem.getOrCreateTag();
            float xp = nbt.getFloat("exp");

            int playerXp = XPManager.getTotalExperience(player.getLevel(), player.getExp());
            Tuple<Integer, Float> newLevel = XPManager.setTotalExperience(playerXp + (int) xp);
            player.setLevel(newLevel.a());
            player.setExp(newLevel.b());
            player.setTotalExperience(playerXp + (int) xp);

            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItemInMainHand(item);
            event.setCancelled(true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
