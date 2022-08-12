package me.MrIronMan.drawit.listeners;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameState;
import me.MrIronMan.drawit.sql.PlayerData;
import me.MrIronMan.drawit.sql.PlayerDataType;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatListener implements Listener {

    @EventHandler
    public void onGuess(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        String msg = e.getMessage();

        if (DrawIt.getConfigData().getBoolean(ConfigData.CHAT_FORMAT)) {
            if (!DrawIt.getInstance().isInGame(player)) {
                e.setFormat(getFormat(player, "lobby"));
            } else {
                Game game = DrawIt.getInstance().getGame(player);
                if (game.isSpectator(uuid)) {
                    e.setFormat(getFormat(player, "spectator"));
                } else {
                    if (game.isGameState(GameState.WAITING) || game.isGameState(GameState.STARTING)) {
                        e.setFormat(getFormat(player, "waiting"));
                    } else if (game.isGameState(GameState.PLAYING) || game.isGameState(GameState.RESTARTING)) {
                        e.setFormat(getFormat(player, "game"));
                    }
                }
            }
        }

        e.setMessage(TextUtil.strip(msg));

        if (DrawIt.getInstance().isInGame(player)) {
            Game game = DrawIt.getInstance().getGame(player);
            if (game.isGameState(GameState.PLAYING)) {
                String word = game.getGameManager().getWord();
                if (game.isSpectator(uuid)) {
                    e.getRecipients().clear();
                    for (UUID id : game.getSpectators()) {
                        e.getRecipients().add(Bukkit.getPlayer(id));
                    }
                } else if (game.getGameManager().isDrawer(player)) {
                    e.setCancelled(true);
                    player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.DRAWER_CHAT_LOCK)));
                } else {
                    e.getRecipients().clear();
                    for (UUID id : game.getPlayers()) {
                        e.getRecipients().add(Bukkit.getPlayer(id));
                    }
                    if (msg.equalsIgnoreCase(word)) {
                        if (game.getGameManager().getActiveTask() != null) {
                            if (!game.getGameManager().getWordGuessers().contains(uuid)) {
                                e.setCancelled(true);

                                int i = game.getGameManager().getWordGuessers().size();

                                if (DrawIt.getConfigData().getConfig().getConfigurationSection(ConfigData.STATS_POINTS_GUESSER).getKeys(false).contains(String.valueOf(i + 1))) {
                                    game.getGameManager().addPoint(player, DrawIt.getConfigData().getInt(ConfigData.STATS_POINTS_GUESSER + "." + (i + 1)));
                                } else {
                                    game.getGameManager().addPoint(player, DrawIt.getConfigData().getInt(ConfigData.STATS_POINTS_GUESSER_OTHER));
                                }

                                game.getGameManager().addCorrectGuess(player);
                                game.getGameManager().getWordGuessers().add(uuid);
                                game.getGameManager().addPoint(game.getGameManager().getCurrentDrawer(), DrawIt.getConfigData().getInt(ConfigData.STATS_POINTS_DRAWER_PER_RIGHT_WORD));
                                game.getGameManager().playSound(DrawIt.getConfigData().getString(ConfigData.SOUND_WORD_GUESS));

                            } else {
                                e.setCancelled(true);
                            }
                        }
                    } else {
                        if (!game.getGameManager().getWordGuessers().contains(uuid)) {
                            game.getGameManager().addIncorrectGuess(player);
                        }
                    }
                }
            }else {
                e.getRecipients().clear();
                for (UUID id : game.getPlayers()) {
                    e.getRecipients().add(Bukkit.getPlayer(id));
                }
            }
        }else {
            e.getRecipients().clear();
            for (Player p : DrawIt.getInstance().getLobbyPlayers()) {
                e.getRecipients().add(p);
            }
        }
    }

    public String getFormat(Player player, String format) {
        PlayerData playerData = DrawIt.getPlayerData(player);
        return TextUtil.getByPlaceholders(DrawIt.getMessagesData().getString(MessagesData.CHAT_FORMAT+"."+format)
                .replace("{points}", String.valueOf(playerData.getData(PlayerDataType.POINTS)))
                .replace("{pointFormat}", DrawIt.getConfigData().getPointFormat(player))
                .replace("{player}", "%1$s")
                .replace("{message}", "%2$s"), player);
    }

}
