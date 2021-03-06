package me.MrIronMan.drawit.menu;

import com.cryptomorin.xseries.XMaterial;
import me.MrIronMan.drawit.utility.ItemUtil;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

public abstract class Menu implements InventoryHolder, ItemUtil {

    protected PlayerMenuUtility playerMenuUtility;
    protected Inventory inventory;
    protected ItemStack GRAY_GLASS_PANE = makeItem(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem(), " ");

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
        IntStream.range(0, getSlots()).filter(i -> inventory.getItem(i) == null).forEachOrdered(i -> inventory.setItem(i, GRAY_GLASS_PANE));
    }

}
