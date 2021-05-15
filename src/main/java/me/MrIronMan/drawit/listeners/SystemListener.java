package me.MrIronMan.drawit.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.MrIronMan.drawit.menu.Menu;
import me.MrIronMan.drawit.menu.UniqueMenu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class SystemListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof Menu)) return;
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
        Menu menu = (Menu) holder;
        menu.handleMenu(e);
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof UniqueMenu)) return;
        UniqueMenu drawerMenu = (UniqueMenu) holder;
        drawerMenu.handleClose(e);
    }

    @EventHandler
    public void onDragItem(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
        ItemStack itemStack = e.getCurrentItem();
        NBTItem nbti = new NBTItem(itemStack);
        if (nbti.getString("name").equals("lobby-item") ||
                nbti.getString("name").equals("drawer-tool") ||
                nbti.getString("name").equals("waiting-tool") ||
                nbti.getString("name").equals("waiting-item") ||
                nbti.getString("name").equals("spectate-item")) {
            e.setCancelled(true);
        }
    }

}
