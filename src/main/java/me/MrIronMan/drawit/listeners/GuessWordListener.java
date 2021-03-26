package me.MrIronMan.drawit.listeners;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.MessagesUtils;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class GuessWordListener implements Listener {

    @EventHandler
    public void onGuess(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String msg = e.getMessage();
        if (DrawIt.getInstance().isInGame(player)) {
            Game game = DrawIt.getInstance().getGame(player);
            String word = game.getGameManager().getWord();
            if (game.getGameManager().isDrawer(player)) {
                e.setCancelled(true);
                player.sendMessage(TextUtil.colorize(MessagesUtils.CHAT_WHILE_DRAWER));
            }
            else if (msg.equalsIgnoreCase(word)) {
                if (!game.getGameManager().getWordGuessers().contains(uuid)) {
                    e.setCancelled(true);
                    game.getGameManager().playSound(Sound.ANVIL_LAND, 1, 1);
                    DrawIt.getInstance().playSound(player, Sound.LEVEL_UP, 1, 1);
                    int i = game.getGameManager().getWordGuessers().size();
                    if (i == 0) {
                        game.getGameManager().addPoint(player, 10);
                    }else if (i == 1) {
                        game.getGameManager().addPoint(player, 8);
                    }else if (i == 2){
                        game.getGameManager().addPoint(player, 6);
                    }else if (i == 3) {
                        game.getGameManager().addPoint(player, 4);
                    }else {
                        game.getGameManager().addPoint(player, 2);
                    }
                    game.getGameManager().addCorrectGuess(player);
                    game.getGameManager().addPoint(game.getGameManager().getCurrentDrawer(), 2);
                    game.getGameManager().getWordGuessers().add(uuid);
                }else {
                    e.setCancelled(true);
                }
            }else {
                game.getGameManager().addIncorrectGuess(player);
            }
        }
    }

}
