package me.MrIronMan.drawit.menuSystem;

import me.MrIronMan.drawit.menuSystem.menus.GameSelector;
import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private GameSelector gameSelector;
    private Player player;

    public PlayerMenuUtility(Player player) {
        this.player = player;
        this.gameSelector = null;
    }

    public Player getPlayer() {
        return player;
    }

    public GameSelector getGameSelector() {
        return gameSelector;
    }

    public void setGameSelector(GameSelector gameSelector) {
        this.gameSelector = gameSelector;
    }

}
