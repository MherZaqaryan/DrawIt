package club.mher.drawit.menu;

import club.mher.drawit.utility.ItemUtil;
import club.mher.drawit.utility.TextUtil;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder, ItemUtil {


    protected PlayerMenuUtility playerMenuUtility;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = makeItem(XMaterial.WHITE_STAINED_GLASS_PANE.parseItem(), " ");
    protected ItemStack GRAY_GLASS_PANE = makeItem(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem(), " ");
    protected ItemStack LIGHT_GRAY_GLASS_PANE = makeItem(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE.parseItem(), " ");
    protected ItemStack BLACK_GLASS_PANE = makeItem(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem(), " ");

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
