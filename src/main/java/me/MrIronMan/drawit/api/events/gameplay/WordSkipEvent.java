package me.MrIronMan.drawit.api.events.gameplay;

import me.MrIronMan.drawit.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WordSkipEvent extends Event {

    private Game game;
    private Player player;
    private String word;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public WordSkipEvent(Player player, Game game, String word) {
        this.player = player;
        this.game = game;
        this.word = word;
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

    public String getWord() {
        return word;
    }
}
