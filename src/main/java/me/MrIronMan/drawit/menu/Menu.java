package me.MrIronMan.drawit.menu;

import me.MrIronMan.drawit.utility.ItemUtil;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder, ItemUtil {


    protected PlayerMenuUtility playerMenuUtility;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = makeItem(Material.STAINED_GLASS_PANE, " ");
    protected ItemStack GRAY_GLASS_PANE = makeItem(Material.STAINED_GLASS_PANE, 1, 7, " ");
    protected ItemStack LIGHT_GRAY_GLASS_PANE = makeItem(Material.STAINED_GLASS_PANE, 1, 8, " ");
    protected ItemStack BLACK_GLASS_PANE = makeItem(Material.STAINED_GLASS_PANE, 1, 15, " ");

    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
    }

    public abstract String getMenuName();
    public abstract int getSlots();
    public abstract void handleMenu(InventoryClickEvent e);
    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), TextUtil.colorize(getMenuName()));
        this.setMenuItems();
        playerMenuUtility.getPlayer().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setFillerGlass(){
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i, GRAY_GLASS_PANE);
            }
        }
    }

}
