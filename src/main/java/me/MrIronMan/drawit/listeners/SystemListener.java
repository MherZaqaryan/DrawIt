package me.MrIronMan.drawit.listeners;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.menuSystem.UniqueMenu;
import me.MrIronMan.drawit.menuSystem.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class SystemListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            Menu menu = (Menu) holder;
            menu.handleMenu(e);
        }
        if (DrawIt.getInstance().isInGame(player)) {
            if (e.getInventory().equals(player.getInventory())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMenuClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof UniqueMenu) {
            UniqueMenu drawerMenu = (UniqueMenu) holder;
            drawerMenu.handleClose(e);
        }
    }


}
