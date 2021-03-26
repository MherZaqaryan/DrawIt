package me.MrIronMan.drawit.menuSystem.menus;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.MessagesUtils;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.menuSystem.Menu;
import me.MrIronMan.drawit.menuSystem.PlayerMenuUtility;
import me.MrIronMan.drawit.utility.OtherUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ColorPickerMenu extends Menu {

    public ColorPickerMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return MessagesUtils.COLOR_PICKER_NAME;
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
        Player player = (Player) e.getWhoClicked();
        if (DrawIt.getInstance().isInGame(player)) {
            Game game = DrawIt.getInstance().getGame(player);
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
            game.setPlayerColor(player.getUniqueId(), e.getCurrentItem());
        }
    }

    @Override
    public void setMenuItems() {
        HashMap<Integer, ItemStack> colorPickerMap = OtherUtils.getColorPicker();
        for (int i : colorPickerMap.keySet()) {
            inventory.setItem(i, colorPickerMap.get(i));
        }
    }

}
