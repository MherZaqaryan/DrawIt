package me.MrIronMan.drawit.game.tasks;

import com.cryptomorin.xseries.XSound;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameState;
import me.MrIronMan.drawit.menuSystem.menus.WordChooseMenu;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WordChooseTask extends BukkitRunnable {

    private PlayingTask activeTask;
    private WordChooseMenu menu;
    private Game game;
    private Player drawer;

    private int time = DrawIt.getConfigData().getInt(ConfigData.COUNTDOWN_WORD_CHOOSE);
    private final int constTime = time;

    public WordChooseTask(Game game) {
        this.game = game;
        this.drawer = game.getGameManager().getNextDrawer();
        this.game.getGameManager().setDrawer(drawer);
        this.drawer.teleport(game.getDrawerLocation());
        this.menu = new WordChooseMenu(DrawIt.getPlayerMenuUtility(drawer), game);
        this.menu.open();
        this.game.addRound();
        this.game.getGameManager().sendMessage(DrawIt.getMessagesData().getString(MessagesData.NEXT_ROUND).replace("{round}", String.valueOf(game.getRound())).replace("{drawer}", drawer.getDisplayName()));
    }

    @Override
    public void run() {
        if (game.getPlayers().isEmpty()) {
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
            XSound.play(drawer, DrawIt.getConfigData().getString(ConfigData.SOUND_DRAWER_WORD_CHOOSE));
        }
        time--;
        game.getGameManager().sendActionBarToGuessers(MessagesData.PICK_WORD);
        game.getGameManager().updateSidebars(-1);
    }

    public void startActiveTask() {
        this.activeTask = new PlayingTask(game);
        this.game.getGameManager().setActiveTask(activeTask);
        activeTask.runTaskTimer(DrawIt.getInstance(), 0, 20);
    }

}
