package me.MrIronMan.drawit.game.tasks;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayingTask extends BukkitRunnable {

    private Game game;
    private String word;
    private StringBuilder wordToShow;
    private List<Integer> charIndex;
    private boolean isClean = true;

    private int time = DrawIt.getConfigData().getInt(ConfigData.COUNTDOWN_PER_ROUND);
    private final int finalTime = time;

    public PlayingTask(Game game) {
        this.game = game;
        this.game.getGameManager().activateDrawerSettings();
        this.charIndex = new ArrayList<>();
        this.word = game.getGameManager().getWord();
        this.wordToShow = new StringBuilder(this.word.replaceAll("[a-zA-Z]", String.valueOf(DrawIt.getMessagesData().getString(MessagesData.HIDING_CHARACTER).replaceAll(" ", "").charAt(0))));
    }

    @Override
    public void run() {
        game.getGameManager().updateSidebars(time);
        if (game.getUuids().isEmpty()) {
            startNext();
        }
        else if (game.getGameManager().getCurrentDrawer() == null) {
            game.getGameManager().sendMessage(DrawIt.getMessagesData().getString(MessagesData.QUIT_MID_GAME));
            startNext();
        }else if (time == 0) {
            startNext();
        }
        else if (game.getGameManager().getWordGuessers().size() == game.getGameManager().getGuessers().size() && !game.getGameManager().getWordGuessers().isEmpty()) {
            game.getGameManager().sendMessage(DrawIt.getMessagesData().getString(MessagesData.EVERYONE_GOT_THE_WORD));
            game.getGameManager().getActiveTask().startNext();
        }
        else {
            if (!game.getBoard().isClean(game.getBoardColor()) && isClean) isClean = false;
            addCharacter();
            game.getGameManager().sendActionBar(game.getGameManager().getCurrentDrawer(), DrawIt.getMessagesData().getString(MessagesData.DRAWER).replace("{word}", word));
            game.getGameManager().sendActionBarToGuessers(isClean ? DrawIt.getMessagesData().getString(MessagesData.START_DRAW) : DrawIt.getMessagesData().getString(MessagesData.GUESSERS).replace("{word}", wordToShow.toString().replaceAll(".(?=.)", "$0 ")));
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
            game.getGameManager().playSound(DrawIt.getConfigData().getString(ConfigData.SOUND_LETTER_EXPLAIN));
        }
    }

    public int getNextCharTime() {
        int timePerChar = (int) Math.ceil((double) finalTime / word.length());
        int i = word.length() - charIndex.size() - 1;
        return timePerChar*i;
    }

    public void startNext() {
        cancel();
        if (game.getGameManager().getCurrentDrawer() != null) {
            game.getGameManager().getCurrentDrawer().teleport(game.getLobbyLocation());
            game.getGameManager().activateGameSettings(game.getGameManager().getCurrentDrawer());
        }
        game.getGameManager().setDrawer(null);
        game.getGameManager().updateSidebars(-1);
        game.getGameManager().sendMessage(DrawIt.getMessagesData().getString(MessagesData.WORD_ANNOUNCE).replace("{word}", word));
        game.getGameManager().startNextRound();
    }

}
