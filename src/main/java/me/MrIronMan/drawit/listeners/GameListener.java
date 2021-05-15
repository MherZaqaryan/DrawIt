package me.MrIronMan.drawit.listeners;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.events.game.GameStateChangeEvent;
import me.MrIronMan.drawit.api.events.player.PlayerJoinGameEvent;
import me.MrIronMan.drawit.api.events.player.PlayerQuitGameEvent;
import me.MrIronMan.drawit.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!DrawIt.getInstance().isInGame(player)) return;
        Game game = DrawIt.getInstance().getGame(player);
        if (!game.getGameManager().isDrawer(player)) return;
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            player.teleport(event.getFrom());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinGameEvent e) {
        DrawIt.updateGameSelector();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitGameEvent e) {
        DrawIt.updateGameSelector();
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent e) {
        DrawIt.updateGameSelector();
    }

}
