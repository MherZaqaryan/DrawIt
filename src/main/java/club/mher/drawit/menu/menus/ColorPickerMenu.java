package club.mher.drawit.menu.menus;

import club.mher.drawit.DrawIt;
import club.mher.drawit.data.ConfigData;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.game.Game;
import club.mher.drawit.menu.Menu;
import club.mher.drawit.menu.PlayerMenuUtility;
import club.mher.drawit.utility.OtherUtils;
import com.cryptomorin.xseries.XSound;
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
        if (DrawIt.getInstance().isInGame(player)) {
            Game game = DrawIt.getInstance().getGame(player);
            player.closeInventory();
            XSound.play(player.getLocation(), DrawIt.getConfigData().getString(ConfigData.SOUND_COLOR_PICK));
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
