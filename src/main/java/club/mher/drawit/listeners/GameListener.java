package club.mher.drawit.listeners;

import club.mher.drawit.DrawIt;
import club.mher.drawit.api.events.game.GameStateChangeEvent;
import club.mher.drawit.api.events.player.PlayerJoinGameEvent;
import club.mher.drawit.api.events.player.PlayerQuitGameEvent;
import club.mher.drawit.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!DrawIt.getInstance().isInGame(player)) {
            return;
        }
        Game game = DrawIt.getInstance().getGame(player);
        if (game.isDrawerMovement()) {
            return;
        }
        if (!game.getGameManager().isDrawer(player)) {
           return;
        }
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
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
