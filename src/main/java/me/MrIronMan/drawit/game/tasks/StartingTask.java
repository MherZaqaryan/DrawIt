package me.MrIronMan.drawit.game.tasks;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameState;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class StartingTask extends BukkitRunnable {

    private Game game;
    private int timer = 20;

    public StartingTask(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (timer == 0) {
            cancel();
            game.getGameManager().startGame();
        }
        else if ((game.getPlayers().size() < game.getMinPlayers() || game.getPlayers().size() == 0) && !game.getGameManager().isForce()) {
            cancel();
            game.setGameState(GameState.WAITING);
        }else {
            if (timer > 5) {
                game.getGameManager().sendActionBar(DrawIt.getMessagesData().getString(MessagesData.START_COUNTDOWN).replace("{time}", String.valueOf(timer)));
            }else {
                game.getGameManager().sendActionBar(DrawIt.getMessagesData().getString(MessagesData.START_COUNTDOWN_UNDER_5).replace("{time}", String.valueOf(timer)));
                game.getGameManager().playSound(Sound.CLICK, 1.0F, 1.0F);
            }
        }
        timer--;
    }

}
