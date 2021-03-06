package me.MrIronMan.drawit.game.tasks;

import com.cryptomorin.xseries.XSound;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.api.game.GameState;
import me.MrIronMan.drawit.menu.menus.WordChooseMenu;
import org.bukkit.scheduler.BukkitRunnable;

public class WordChooseTask extends BukkitRunnable {

    private final WordChooseMenu menu;
    private final Game game;

    private int time = DrawIt.getConfigData().getInt(ConfigData.COUNTDOWN_WORD_CHOOSE);
    private final int constTime = time;

    public WordChooseTask(Game game) {
        this.game = game;
        this.game.getGameManager().setDrawer(game.getGameManager().getNextDrawer());
        this.game.getGameManager().getCurrentDrawer().teleport(game.getDrawerLocation());
        this.menu = new WordChooseMenu(DrawIt.getPlayerMenuUtility(game.getGameManager().getCurrentDrawer()), game);
        this.menu.open();
        this.game.addRound();
        this.game.getGameManager().sendMessage(DrawIt.getMessagesData().getString(MessagesData.NEXT_ROUND).replace("{round}", String.valueOf(game.getRound())).replace("{drawer}", game.getGameManager().getCurrentDrawer().getDisplayName()));
    }

    @Override
    public void run() {
        if (game.getUuids().isEmpty()) {
            cancel();
            game.setGameState(GameState.WAITING);
        }
        else if (menu.isChooses) {
            cancel();
            startActiveTask();
        }
        else if (time == 0) {
            cancel();
            menu.chooseRandomWord();
            startActiveTask();
        }
        else if (time <= (constTime/2)) {
            XSound.play(game.getGameManager().getCurrentDrawer(), DrawIt.getConfigData().getString(ConfigData.SOUND_DRAWER_WORD_CHOOSE));
        }
        time--;
        game.getGameManager().sendActionBarToGuessers(DrawIt.getMessagesData().getString(MessagesData.PICK_WORD));
        game.getGameManager().updateSidebars(-1);
    }

    public void startActiveTask() {
        PlayingTask activeTask = new PlayingTask(game);
        this.game.getGameManager().setActiveTask(activeTask);
        activeTask.runTaskTimer(DrawIt.getInstance(), 0L, 20L);
    }

}
