package com.chrisstar321.elevator.listeners;

import com.chrisstar321.elevator.Elevators;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class PlayerCrouchListener implements Listener {

    @EventHandler
    public void OnPlayerCrouch(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if (!player.isSneaking()) return;

        FileConfiguration config = Elevators._getConfig();
        Material elevatorMat = Material.matchMaterial(config.getString("elevator_block"));
        if (elevatorMat == null) elevatorMat = Elevators.BACKUP_ELEVATOR_MATERIAL;
        Location loc = player.getLocation();
        World w = loc.getWorld();

        if (w.getBlockAt(loc.add(new Vector(0, -1, 0))).getType().equals(elevatorMat)) {
            //Find an elevator below the current position and teleport the player there
            int dY = -1;
            while (loc.getBlockY() + dY > 0) {
                //Try to see if the current block is an elevator block.
                //if not, keep looking
                //if it is, teleport the player
                Location newLocation = loc.add(0, dY, 0);
                if (w.getBlockAt(newLocation).getType().equals(elevatorMat) && checkForAirBlocks(newLocation.clone())) {
                    //Teleport the player
                    e.getPlayer().teleport(newLocation.add(new Vector(0, 1, 0)),
                                           PlayerTeleportEvent.TeleportCause.PLUGIN);
                    return;
                }
            }
        }
    }

    private boolean checkForAirBlocks(Location loc) {
        World w = loc.getWorld();
        return !(w.getBlockAt(loc.add(new Vector(0, 1, 0))).getType().isSolid() || w.getBlockAt(loc.add(new Vector(0, 2, 0))).getType().isSolid());
    }
}
