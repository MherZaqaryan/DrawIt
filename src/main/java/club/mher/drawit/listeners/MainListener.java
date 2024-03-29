package club.mher.drawit.listeners;

import club.mher.drawit.DrawIt;
import club.mher.drawit.game.Game;
import club.mher.drawit.game.SetupGame;
import com.cryptomorin.xseries.XMaterial;
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
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class MainListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        Player player = e.getPlayer();
        if (DrawIt.getInstance().isIn(player)) {
            if (DrawIt.getBuildMode(player)) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        Player player = e.getPlayer();
        if (DrawIt.getInstance().isIn(player)) {
            if (DrawIt.getBuildMode(player)) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (e.isCancelled()) return;
        Player player = e.getPlayer();
        if (DrawIt.getInstance().isIn(player)) {
            if (DrawIt.getBuildMode(player)) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPick(PlayerPickupItemEvent e) {
        if (e.isCancelled()) return;
        Player player = e.getPlayer();
        if (DrawIt.getInstance().isIn(player)) {
            if (DrawIt.getBuildMode(player)) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (DrawIt.getInstance().isIn(player)) {
                e.setCancelled(true);
                if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                    if (DrawIt.getInstance().isInGame(player)) {
                        Game game = DrawIt.getInstance().getGame(player);
                        player.teleport(game.getLobbyLocation());
                    }else if (DrawIt.getInstance().isInSetup(player)) {
                        SetupGame setupGame = DrawIt.getInstance().getSetupGame(player);
                        player.teleport(setupGame.getWorld().getSpawnLocation());
                    }else {
                        player.teleport(DrawIt.getInstance().getLobbyLocation());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (DrawIt.getInstance().isIn(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (e.isCancelled()) return;
            Player damager = (Player) e.getDamager();
            if (DrawIt.getInstance().isIn(damager)) {
                e.setCancelled(true);
            }
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
        if (e.isCancelled()) return;
        if (DrawIt.getInstance().isIn(player)) {
            if (DrawIt.getBuildMode(player)) return;
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

}
