package me.MrIronMan.drawit.api.events.player;

import me.MrIronMan.drawit.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerQuitGameEvent extends Event {

    private Game game;
    private Player player;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerQuitGameEvent(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

}
