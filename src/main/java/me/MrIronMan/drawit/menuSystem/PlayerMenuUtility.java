package me.MrIronMan.drawit.menuSystem;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private Menu gameSelector;
    private Player player;

    public PlayerMenuUtility(Player player) {
        this.player = player;
        this.gameSelector = null;
    }

    public Player getPlayer() {
        return player;
    }

    public Menu getGameSelector() {
        return gameSelector;
    }

    public void setGameSelector(Menu gameSelector) {
        this.gameSelector = gameSelector;
    }

}
