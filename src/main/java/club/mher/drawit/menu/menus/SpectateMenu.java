package club.mher.drawit.menu.menus;

import club.mher.drawit.DrawIt;
import club.mher.drawit.data.ConfigData;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.game.Game;
import club.mher.drawit.game.GameState;
import club.mher.drawit.menu.Menu;
import club.mher.drawit.menu.PlayerMenuUtility;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpectateMenu extends Menu {

    private HashMap<Integer, Game> gameSlotMap;
    private Integer[] slots = DrawIt.getConfigData().getIntegerList(ConfigData.SPECTATE_MENU_SLOTS).toArray(new Integer[0]);

    public SpectateMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return DrawIt.getMessagesData().getString(MessagesData.SPECTATE_MENU_SETTINGS_TITLE);
    }

    @Override
    public int getSlots() {
        return DrawIt.getConfigData().getInt(ConfigData.SPECTATE_MENU_SIZE);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (gameSlotMap.containsKey(e.getSlot())) {
            gameSlotMap.get(e.getSlot()).getGameManager().activateSpectatorSettings(player);
            return;
        }
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasKey("name")) {
            if (nbtItem.getString("name").equals("spectate-menu")) {
                if (!nbtItem.getString("command").equals("none")) {
                    Bukkit.dispatchCommand(player, nbtItem.getString("command"));
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        for (ItemStack item : DrawIt.getConfigData().getSpectateMenuItems()) {
            if (item == null || item.getType().equals(Material.AIR)) return;
            NBTItem nbti = new NBTItem(item);
            if (nbti.hasKey("name")) {
                if (nbti.getString("name").equals("spectate-menu")) {
                    inventory.setItem(nbti.getInteger("slot"), item);
                }
            }
        }

        this.gameSlotMap = new HashMap<>();
        List<Game> games = new ArrayList<>(DrawIt.getInstance().getGames());
        games.removeIf(game -> !game.isGameState(GameState.PLAYING));

        int index = 0;
        if (!games.isEmpty()) {
            for (Game game : games) {
                if (index > slots.length || game == null) break;
                inventory.setItem(slots[index], DrawIt.getConfigData().getItem(ConfigData.SPECTATE_MENU_GAME, game));
                gameSlotMap.put(slots[index], game);
                index++;
            }
        }
    }

}
