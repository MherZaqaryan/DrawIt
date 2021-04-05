package me.MrIronMan.drawit.menuSystem.menus;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameState;
import me.MrIronMan.drawit.menuSystem.Menu;
import me.MrIronMan.drawit.menuSystem.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class GameSelector extends Menu {

    private Integer[] slots = DrawIt.getConfigData().getIntegerList(ConfigData.GAMES_MENU_SETTINGS_SLOTS).toArray(new Integer[0]);
    private HashMap<Integer, Game> invGameMap;

    public GameSelector(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        playerMenuUtility.setGameSelector(this);
    }

    @Override
    public String getMenuName() {
        return "Selector";
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
                Bukkit.dispatchCommand(player, nbtItem.getString("command"));
            }
        }
    }

    @Override
    public void setMenuItems() {
        for (int slot : slots) {
            inventory.setItem(slot, makeItem(XMaterial.matchXMaterial("STAINED_GLASS_PANE").get().parseMaterial(), 1, 7, "&7Waiting..."));
        }

        for (ItemStack item : DrawIt.getConfigData().getGameMenuItems()) {
            if (item == null || item.getType().equals(Material.AIR)) return;
            NBTItem nbti = new NBTItem(item);
            if (nbti.hasKey("name")) {
                if (nbti.getString("name").equals("game-menu")) {
                    inventory.setItem(nbti.getInteger("slot"), item);
                }
            }
        }

        this.invGameMap = new HashMap<>();
        List<Game> games = DrawIt.getInstance().getGames();
        int index = 0;

        for (Game game : games) {
            if (index > slots.length) break;
            if (game.isEnabled()) {
                if (game.isGameState(GameState.WAITING)) {
                    inventory.setItem(slots[index], makeItem(XMaterial.matchXMaterial("STAINED_CLAY").get().parseMaterial(), 1, 5, "&a&l" + game.getDisplayName(), "", "&7Players: &f" + game.getPlayers().size() + "/" + game.getMaxPlayers(), "&7State: &f" + GameState.getName(game.getGameState()), "", "&b► Click To Join"));
                } else if (game.isGameState(GameState.STARTING)) {
                    inventory.setItem(slots[index], makeItem(XMaterial.matchXMaterial("STAINED_CLAY").get().parseMaterial(), 1, 4, "&a&l" + game.getDisplayName(), "", "&7Players: &f" + game.getPlayers().size() + "/" + game.getMaxPlayers(), "&7State: &f" + GameState.getName(game.getGameState()), "", "&b► Click To Join"));
                }else if (game.isGameState(GameState.PLAYING)) {
                    if (DrawIt.getConfigData().getBoolean(ConfigData.GAMES_MENU_SETTINGS_SHOW_PLAYINGS)) {
                        inventory.setItem(slots[index], makeItem(XMaterial.matchXMaterial("STAINED_CLAY").get().parseMaterial(), 1, 14, "&a&l" + game.getDisplayName(), "", "&7Players: &f" + game.getPlayers().size() + "/" + game.getMaxPlayers(), "&7State: &f" + GameState.getName(game.getGameState()), "", "&b► Click To Spectate"));
                    }
                }
                invGameMap.put(slots[index], game);
                index++;
            }
        }

    }

}
