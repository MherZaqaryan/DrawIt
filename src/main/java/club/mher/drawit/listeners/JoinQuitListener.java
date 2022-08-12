package club.mher.drawit.listeners;

import club.mher.drawit.DrawIt;
import club.mher.drawit.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player player = e.getPlayer();
        DrawIt.getPlayerData(player).insertData();
        DrawIt.getInstance().activateLobbySettings(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player player = e.getPlayer();
        if (DrawIt.getInstance().isInGame(player)) {
            Game game = DrawIt.getInstance().getGame(player);
            game.getGameManager().leaveGame(player);
        }
        DrawIt.getPlayerDataMap().remove(player);
    }

}
