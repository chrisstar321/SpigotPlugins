package com.chrisstar321.elevator.listeners;

import com.chrisstar321.elevator.Elevators;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.logging.Logger;

public class PlayerJumpListener implements Listener {

    @EventHandler
    public void OnPlayerJump(PlayerMoveEvent e) {
        //Check if the player went up
        if (e.getFrom().getY() < e.getTo().getY()) {
            //Player went up, now check if they were standing on the elevator block
            FileConfiguration config = Elevators._getConfig();
            Material elevatorMat = Material.matchMaterial(config.getString("elevator_block"));
            if (elevatorMat == null) elevatorMat = Elevators.BACKUP_ELEVATOR_MATERIAL;

            World w = e.getFrom().getWorld();
            if (w.getBlockAt(e.getFrom().add(new Vector(0, -1, 0))).getType().equals(elevatorMat)) {
                //Find an elevator above the current position and teleport the player there
                int dY = 1;
                while (dY + e.getFrom().getBlockY() < 256) {
                    //Try to see if the current block is an elevator block.
                    //if not, keep looking
                    //if it is, teleport the player
                    Location newLocation = e.getFrom().add(0, dY, 0);
                    if (w.getBlockAt(newLocation).getType().equals(elevatorMat) && checkForAirBlocks(newLocation.clone())) {
                        //Teleport the player
                        e.setCancelled(true);
                        e.getPlayer().teleport(newLocation.add(new Vector(0, 1, 0)));
                        return;
                    }
                }
            }
        }
    }

    private boolean checkForAirBlocks(Location loc) {
        World w = loc.getWorld();
        return !(w.getBlockAt(loc.add(new Vector(0, 1, 0))).getType().isSolid() || w.getBlockAt(loc.add(new Vector(0, 2, 0))).getType().isSolid());
    }
}
