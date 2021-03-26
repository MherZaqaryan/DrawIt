package me.MrIronMan.drawit.menuSystem.menus;

import com.cryptomorin.xseries.XMaterial;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameState;
import me.MrIronMan.drawit.menuSystem.PlayerMenuUtility;
import me.MrIronMan.drawit.menuSystem.UniqueMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.List;

public class GameSelector extends UniqueMenu {

    private int selectorUpdateTask = -1;
    private int[] slots = {28, 29, 30, 31, 32, 33, 34};
    private HashMap<Integer, Game> invGameMap;

    public GameSelector(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "&7DrawIt - Selector";
    }

    @Override
    public int getSlots() {
        return 45;
    }


    @Override
    public void handleClose(InventoryCloseEvent e) {
        if (selectorUpdateTask != -1) Bukkit.getScheduler().cancelTask(selectorUpdateTask);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (invGameMap.containsKey(e.getSlot())) {
            invGameMap.get(e.getSlot()).getGameManager().joinGame(player);
        }else if (e.getSlot() == 14) {
            DrawIt.getInstance().quickJoinGame(player);
        }
    }

    @Override
    public void setMenuItems() {
        this.selectorUpdateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(DrawIt.getInstance(), () -> {
            this.invGameMap = new HashMap<>();

            inventory.setItem(12, makeItem(XMaterial.matchXMaterial("MAGMA_CREAM").get().parseMaterial(), "&c&lSpectate Game"));
            inventory.setItem(14, makeItem(XMaterial.matchXMaterial("MINECART").get().parseMaterial(), "&a&lQuick Join"));

            List<Game> games = DrawIt.getInstance().getGames();

            for (int slot : slots) {
                inventory.setItem(slot, makeItem(XMaterial.matchXMaterial("STAINED_GLASS_PANE").get().parseMaterial(), 1, 7, "&7Waiting..."));
            }

            int index = 0;

            for (Game game : games) {
                if (index > 6) break;
                if (game.isEnabled()) {
                    if (game.isGameState(GameState.WAITING)) {
                        inventory.setItem(slots[index], makeItem(XMaterial.matchXMaterial("STAINED_CLAY").get().parseMaterial(), 1, 5, "&a&l" + game.getName(), "", "&7Players: &f" + game.getPlayers().size() + "/" + game.getMaxPlayers(), "&7State: &f" + GameState.getName(game.getGameState()), "", "&b► Click To Join"));
                    } else if (game.isGameState(GameState.STARTING)) {
                        inventory.setItem(slots[index], makeItem(XMaterial.matchXMaterial("STAINED_CLAY").get().parseMaterial(), 1, 4, "&a&l" + game.getName(), "", "&7Players: &f" + game.getPlayers().size() + "/" + game.getMaxPlayers(), "&7State: &f" + GameState.getName(game.getGameState()), "", "&b► Click To Join"));
                    }
                    invGameMap.put(slots[index], game);
                    index++;
                }
            }

        }, 0L, 10L);
    }

}
