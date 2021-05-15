package me.MrIronMan.drawit.game.tasks;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.api.game.GameState;
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
        else if ((game.getUuids().size() < game.getMinPlayers() && !game.getGameManager().isForce()) || game.getUuids().size() == 0) {
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
