package club.mher.drawit.game.tasks;

import club.mher.drawit.DrawIt;
import club.mher.drawit.data.ConfigData;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.game.Game;
import club.mher.drawit.game.GameState;
import club.mher.drawit.menu.menus.WordChooseMenu;
import com.cryptomorin.xseries.XSound;
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
