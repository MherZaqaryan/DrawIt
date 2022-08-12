package club.mher.drawit.game.tasks;

import club.mher.drawit.DrawIt;
import club.mher.drawit.data.ConfigData;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.game.Game;
import club.mher.drawit.game.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class StartingTask extends BukkitRunnable {

    private Game game;
    private int timer = DrawIt.getConfigData().getInt(ConfigData.COUNTDOWN_STARTING);

    public StartingTask(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (timer == 0) {
            cancel();
            game.getGameManager().startGame();
        }
        else if ((game.getPlayers().size() < game.getMinPlayers() && !game.getGameManager().isForce()) || game.getPlayers().size() == 0) {
            cancel();
            game.setGameState(GameState.WAITING);
        }else {
            if (timer > 5) {
                game.getGameManager().sendActionBar(DrawIt.getMessagesData().getString(MessagesData.START_COUNTDOWN).replace("{time}", String.valueOf(timer)));
            }else {
                game.getGameManager().sendActionBar(DrawIt.getMessagesData().getString(MessagesData.START_COUNTDOWN_UNDER_5).replace("{time}", String.valueOf(timer)));
                game.getGameManager().playSound(DrawIt.getConfigData().getString(ConfigData.SOUND_UNDER_5));
            }
        }
        timer--;
    }

}
