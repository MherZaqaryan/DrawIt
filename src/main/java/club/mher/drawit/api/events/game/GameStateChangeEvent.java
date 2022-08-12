package club.mher.drawit.api.events.game;

import club.mher.drawit.game.Game;
import club.mher.drawit.game.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends Event {

    private Game game;
    private GameState oldState;
    private GameState newState;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public GameStateChangeEvent(Game game, GameState oldState, GameState newState) {
        this.game = game;
        this.oldState = oldState;
        this.newState = newState;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public Game getGame() {
        return game;
    }

    public GameState getOldState() {
        return oldState;
    }

    public GameState getNewState() {
        return newState;
    }

}
