package me.MrIronMan.drawit.menu.menus;

import de.tr7zw.nbtapi.NBTItem;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameState;
import me.MrIronMan.drawit.menu.PlayerMenuUtility;
import me.MrIronMan.drawit.menu.UniqueMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class GameSelector extends UniqueMenu {

    private Integer[] slots = DrawIt.getConfigData().getIntegerList(ConfigData.GAMES_MENU_SETTINGS_SLOTS).toArray(new Integer[0]);
    private HashMap<Integer, Game> invGameMap;

    public GameSelector(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setGameSelector(this);
    }

    @Override
    public void handleClose(InventoryCloseEvent e) {
        playerMenuUtility.setGameSelector(null);
    }

    @Override
    public String getMenuName() {
        return DrawIt.getMessagesData().getString(MessagesData.GAME_MENU_SETTINGS_TITLE);
    }

    @Override
    public int getSlots() {
        return DrawIt.getConfigData().getInt(ConfigData.GAMES_MENU_SETTINGS_SIZE);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (invGameMap.containsKey(e.getSlot())) {
            invGameMap.get(e.getSlot()).getGameManager().joinGame(player);
            return;
        }
        NBTItem nbtItem = new NBTItem(item);
        if (nbtItem.hasKey("name")) {
            if (nbtItem.getString("name").equals("game-menu")) {
                if (!nbtItem.getString("command").equals("none")) {
                    Bukkit.dispatchCommand(player, nbtItem.getString("command"));
                }
            }
        }
    }

    @Override
    public void setMenuItems() {

        for (int slot : slots) {
            inventory.setItem(slot, DrawIt.getConfigData().getItem(ConfigData.GAMES_MENU_SETTINGS_WAITING_SLOTS, null));
        }

        if (!DrawIt.getConfigData().getGameMenuItems().isEmpty()) {
            for (ItemStack item : DrawIt.getConfigData().getGameMenuItems()) {
                if (item == null || item.getType().equals(Material.AIR)) return;
                NBTItem nbti = new NBTItem(item);
                if (nbti.hasKey("name")) {
                    if (nbti.getString("name").equals("game-menu")) {
                        inventory.setItem(nbti.getInteger("slot"), item);
                    }
                }
            }
        }

        int index = 0;

        this.invGameMap = new HashMap<>();

        for (Game game : new ArrayList<>(DrawIt.getInstance().getGames())) {
            if (index > slots.length || game == null) break;
            if (game.isEnabled()) {
                if (game.isGameState(GameState.WAITING)) {
                    inventory.setItem(slots[index], DrawIt.getConfigData().getItem(ConfigData.GAMES_MENU_SETTINGS_WAITING, game));
                    invGameMap.put(slots[index], game);
                    index++;
                } else if (game.isGameState(GameState.STARTING)) {
                    inventory.setItem(slots[index], DrawIt.getConfigData().getItem(ConfigData.GAMES_MENU_SETTINGS_STARTING, game));
                    invGameMap.put(slots[index], game);
                    index++;
                } else if (game.isGameState(GameState.PLAYING)) {
                    if (DrawIt.getConfigData().getBoolean(ConfigData.GAMES_MENU_SETTINGS_SHOW_PLAYINGS)) {
                        inventory.setItem(slots[index], DrawIt.getConfigData().getItem(ConfigData.GAMES_MENU_SETTINGS_PLAYING, game));
                        invGameMap.put(slots[index], game);
                        index++;
                    }
                }
            }
        }

    }

}
