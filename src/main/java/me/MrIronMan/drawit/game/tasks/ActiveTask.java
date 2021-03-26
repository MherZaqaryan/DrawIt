package me.MrIronMan.drawit.game.tasks;

import me.MrIronMan.drawit.data.MessagesUtils;
import me.MrIronMan.drawit.game.Game;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActiveTask extends BukkitRunnable {

    private Game game;
    private Player drawer;

    private String word;
    private StringBuilder wordToShow;
    private List<Integer> charIndex;

    private boolean isClean = true;

    private int time = 80;
    private final int finalTime = time;

    public ActiveTask(Game game) {
        this.game = game;
        this.drawer = game.getGameManager().getCurrentDrawer();
        this.game.getGameManager().activateDrawerSettings(drawer);
        this.charIndex = new ArrayList<>();
        this.word = game.getGameManager().getWord();
        this.wordToShow = new StringBuilder(this.word.replaceAll("[a-zA-Z]", String.valueOf(MessagesUtils.HIDING_CHARACTER.replaceAll(" ", "").charAt(0))));
    }

    @Override
    public void run() {
        game.getGameManager().updateSidebars(time);
        if (game.getGameManager().getCurrentDrawer() == null) {
            game.getGameManager().sendMessage(MessagesUtils.QUIT_MID_GAME);
            startNext();
        }else if (time == 0) {
            startNext();
        }
        else if (game.getGameManager().getWordGuessers().size() == game.getGameManager().getGuessers().size() && game.getGameManager().getWordGuessers().size() != 0) {
            game.getGameManager().sendMessage(MessagesUtils.EVERYONE_GOT_THE_WORD);
            game.getGameManager().getActiveTask().startNext();
        }
        else {
            if (!game.getBoard().isClean() && isClean) isClean = false;
            addCharacter();
            game.getGameManager().sendActionBar(drawer, MessagesUtils.DRAWER.replace("{word}", word));
            game.getGameManager().sendActionBarToGuessers(isClean ? MessagesUtils.START_DRAW : MessagesUtils.GUESSERS.replace("{word}", wordToShow.toString().replaceAll(".(?=.)", "$0 ")));
            time--;
        }
    }

    public void addCharacter() {
        if (time != getNextCharTime()) return;
        Random random = new Random();
        int rand = random.nextInt(word.length());
        if (wordToShow.length() == charIndex.size() - 1) return;
        if (charIndex.contains(rand)) {
            addCharacter();
        }else {
            wordToShow.setCharAt(rand, word.charAt(rand));
            charIndex.add(rand);
            game.getGameManager().playSound(Sound.CHICKEN_EGG_POP, 1, 2);
        }
    }

    public int getNextCharTime() {
        int timePerChar = (int) Math.ceil((double) finalTime / word.length());
        int i = word.length() - charIndex.size() - 1;
        return timePerChar*i;
    }

    public void startNext() {
        cancel();
        game.getGameManager().updateSidebars(-1);
        game.getGameManager().setDrawer(null);
        if (drawer != null) {
            drawer.teleport(game.getLobbyLocation());
            game.getGameManager().activateGameSettings(drawer);
        }
        game.getGameManager().sendMessage(MessagesUtils.WORD_ANNOUNCE.replace("{word}", word));
        game.getGameManager().startNextRound();
    }

}
