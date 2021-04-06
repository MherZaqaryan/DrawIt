package me.MrIronMan.drawit.listeners;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;
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
                player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.DRAWER_CHAT_LOCK)));
            }
            else if (msg.equalsIgnoreCase(word)) {
                if (!game.getGameManager().getWordGuessers().contains(uuid)) {
                    e.setCancelled(true);
                    game.getGameManager().playSound(DrawIt.getConfigData().getString(ConfigData.SOUND_WORD_GUESS));
                    int i = game.getGameManager().getWordGuessers().size();
                    Set<String> list = DrawIt.getConfigData().getConfig().getConfigurationSection(ConfigData.STATS_POINTS_GUESSER).getKeys(false);
                    list.remove("other");
                    if (list.contains(String.valueOf(i+1))) {
                        game.getGameManager().addPoint(player, DrawIt.getConfigData().getInt(ConfigData.STATS_POINTS_GUESSER+"."+(i+1)));
                    }else {
                        game.getGameManager().addPoint(player, DrawIt.getConfigData().getInt(ConfigData.STATS_POINTS_GUESSER_OTHER));
                    }
                    game.getGameManager().addCorrectGuess(player);
                    game.getGameManager().addPoint(game.getGameManager().getCurrentDrawer(), DrawIt.getConfigData().getInt(ConfigData.STATS_POINTS_DRAWER_PER_RIGHT_WORD));
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
