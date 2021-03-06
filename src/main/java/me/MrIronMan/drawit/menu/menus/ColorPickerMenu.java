package me.MrIronMan.drawit.menu.menus;

import com.cryptomorin.xseries.XSound;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.menu.Menu;
import me.MrIronMan.drawit.menu.PlayerMenuUtility;
import me.MrIronMan.drawit.utility.OtherUtils;
import org.bukkit.Material;
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
        return DrawIt.getMessagesData().getString(MessagesData.COLOR_PICKER_TITLE);
    }

    @Override
    public int getSlots() {
        return DrawIt.getConfigData().getInt(ConfigData.COLOR_PICKER+".settings.size");
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
        Player player = (Player) e.getWhoClicked();
        if (!DrawIt.getInstance().isInGame(player)) return;
        Game game = DrawIt.getInstance().getGame(player);
        player.closeInventory();
        XSound.play(player.getLocation(), DrawIt.getConfigData().getString(ConfigData.SOUND_COLOR_PICK));
        game.setPlayerColor(player.getUniqueId(), e.getCurrentItem());
    }

    @Override
    public void setMenuItems() {
        HashMap<Integer, ItemStack> colorPickerMap = OtherUtils.getColorPicker();
        colorPickerMap.keySet().forEach(i -> inventory.setItem(i, colorPickerMap.get(i)));
    }

}
