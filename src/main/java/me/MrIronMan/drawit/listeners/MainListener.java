package me.MrIronMan.drawit.listeners;

import com.cryptomorin.xseries.XMaterial;
import me.MrIronMan.drawit.DrawIt;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class MainListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (DrawIt.getBuildMode(player)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (DrawIt.getBuildMode(player)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (DrawIt.getBuildMode(player)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemPick(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if (DrawIt.getBuildMode(player)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.toWeatherState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (DrawIt.getBuildMode(player)) return;
        if (e.isCancelled()) return;
        Block b = e.getClickedBlock();
        if (b != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (b.getType() == XMaterial.CRAFTING_TABLE.parseMaterial()) {
                e.setCancelled(true);
            } else if (b.getType() == XMaterial.ENCHANTING_TABLE.parseMaterial()) {
                e.setCancelled(true);
            } else if (b.getType() == Material.FURNACE) {
                e.setCancelled(true);
            } else if (b.getType() == Material.BREWING_STAND) {
                e.setCancelled(true);
            } else if (b.getType() == Material.ANVIL) {
                e.setCancelled(true);
            }
        }
    }

}
