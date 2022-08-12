package club.mher.drawit.menu;

import club.mher.drawit.menu.menus.GameSelector;
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
