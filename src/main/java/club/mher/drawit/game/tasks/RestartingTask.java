package club.mher.drawit.game.tasks;

import club.mher.drawit.DrawIt;
import club.mher.drawit.data.ConfigData;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.game.Game;
import club.mher.drawit.sql.PlayerData;
import club.mher.drawit.sql.PlayerDataType;
import club.mher.drawit.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RestartingTask extends BukkitRunnable {

    private Game game;

    public RestartingTask(Game game) {
        this.game = game;
        game.getBoard().burn(game.getBoardColor());
        for (UUID uuid : game.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            PlayerData pd = DrawIt.getPlayerData(player);
            pd.addData(PlayerDataType.POINTS, game.getGameManager().getPoint(player));
            pd.addData(PlayerDataType.GAMES_PLAYED, 1);
            pd.addData(PlayerDataType.CORRECT_GUESSES, game.getGameManager().getCorrectGuesses(player));
            pd.addData(PlayerDataType.INCORRECT_GUESSES, game.getGameManager().getIncorrectGuesses(player));
            if (game.getGameManager().getSkippedPlayers().contains(player.getUniqueId())) {
                pd.addData(PlayerDataType.SKIPS, 1);
            }
            if (game.getGameManager().getLeaders().get(0).getKey().equals(player.getUniqueId())) {
                pd.addData(PlayerDataType.VICTORIES, 1);
            }
            for (String msg : getMessage()) {
                player.sendMessage(TextUtil.getByPlaceholders(msg, player));
            }
        }
        game.getGameManager().playSound(DrawIt.getConfigData().getString(ConfigData.SOUND_GAME_OVER));
        game.getGameManager().sendTitle("&c&lGame Over!", "&9" + game.getGameManager().getLeaderName(0) + " won the game.", 10, 10, 50);
        for (UUID uuid : game.getSpectators()) {
            Player player = Bukkit.getPlayer(uuid);
            for (String msg : getMessage()) {
                player.sendMessage(TextUtil.getByPlaceholders(msg, player));
            }
        }
    }

    @Override
    public void run() {
        List<UUID> players = new ArrayList<>(game.getPlayers());
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            game.getPlayers().remove(uuid);
            DrawIt.getInstance().activateLobbySettings(player);
        }
        game.getPlayers().clear();
        List<UUID> spectators = new ArrayList<>(game.getSpectators());
        for (UUID uuid : spectators) {
            Player player = Bukkit.getPlayer(uuid);
            game.getSpectators().remove(uuid);
            DrawIt.getInstance().activateLobbySettings(player);
        }
        game.getSpectators().clear();
        DrawIt.getInstance().restartGame(game.getName());
    }

    public List<String> getMessage() {
        List<String> msg = new ArrayList<>();
        for (String s : DrawIt.getMessagesData().getStringList(MessagesData.GAME_END_MESSAGE)) {
            msg.add(s.replace("{winner_1}", game.getGameManager().getLeaderName(0))
                    .replace("{winner_2}",game.getGameManager().getLeaderName(1))
                    .replace("{winner_3}", game.getGameManager().getLeaderName(2)));
        }
        return msg;
    }

}
