package me.MrIronMan.drawit.menu.menus;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.menu.Menu;
import me.MrIronMan.drawit.menu.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.UUID;

public class TeleporterMenu extends Menu {

    private final Game game;
    private HashMap<Integer, Player> teleporterMap = new HashMap<>();

    public TeleporterMenu(PlayerMenuUtility playerMenuUtility, Game game) {
        super(playerMenuUtility);
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return DrawIt.getMessagesData().getString(MessagesData.TELEPORTER_MENU_SETTINGS_TITLE);
    }

    @Override
    public int getSlots() {
        return getSlotSize();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!teleporterMap.containsKey(e.getSlot())) return;
        player.teleport(teleporterMap.get(e.getSlot()));
    }

    @Override
    public void setMenuItems() {
        teleporterMap = new HashMap<>();
        int index = 0;
        for (UUID uuid : game.getUuids()) {
            Player player = Bukkit.getPlayer(uuid);
            inventory.setItem(index, makeHead(player.getName(), DrawIt.getMessagesData().getString(MessagesData.TELEPORTER_MENU_PLAYER_HEAD+".display-name").replace("{player}", player.getDisplayName()), DrawIt.getMessagesData().getStringList(MessagesData.TELEPORTER_MENU_PLAYER_HEAD+".lore").toArray(new String[0])));
            teleporterMap.put(index, player);
            index++;
        }
    }

    public int getSlotSize() {
        return (int) (9*(Math.ceil(game.getUuids().size()/9.0D)));
    }

}
