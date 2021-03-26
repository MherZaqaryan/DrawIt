package me.MrIronMan.drawit.menuSystem.menus;

import com.cryptomorin.xseries.XMaterial;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.menuSystem.Menu;
import me.MrIronMan.drawit.menuSystem.PlayerMenuUtility;
import me.MrIronMan.drawit.sql.PlayerData;
import me.MrIronMan.drawit.sql.PlayerDataType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerStatsMenu extends Menu {

    private String playerName;
    private PlayerData playerData;

    public PlayerStatsMenu(PlayerMenuUtility playerMenuUtility, String playerName) {
        super(playerMenuUtility);
        this.playerData = DrawIt.getPlayerData(playerName);
    }

    @Override
    public String getMenuName() {
        return getTitle();
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }

    @Override
    public void setMenuItems() {
        inventory.setItem(10, makeItem(XMaterial.matchXMaterial("EMERALD").get().parseMaterial(), "&eVictories: " + playerData.getData(PlayerDataType.VICTORIES)));
    }

    public String getTitle() {
        if (playerMenuUtility.getPlayer().getName().equalsIgnoreCase(playerName)) {
            return "Your Stats";
        }else {
            return playerName + " Stats";
        }
    }

}
