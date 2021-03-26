package me.MrIronMan.drawit.game;

import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigUtils;
import me.MrIronMan.drawit.data.MessagesUtils;
import me.MrIronMan.drawit.game.tasks.ActiveTask;
import me.MrIronMan.drawit.game.tasks.EndingTask;
import me.MrIronMan.drawit.game.tasks.StartingTask;
import me.MrIronMan.drawit.game.tasks.WordChooseTask;
import me.MrIronMan.drawit.game.utility.DrawTools;
import me.MrIronMan.drawit.utility.OtherUtils;
import me.MrIronMan.drawit.game.utility.SideBar;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {

    private Game game;

    private List<String> wordsList;
    private String word;
    private Player drawer;

    private List<UUID> waitingPlayers;
    private List<UUID> skippedPlayers;

    private boolean force;

    private HashMap<UUID, SideBar> playerSidebarMap;
    private HashMap<UUID, Integer> playerPointMap;
    private HashMap<UUID, Integer> playerCorrectGuessesMap;
    private HashMap<UUID, Integer> playerIncorrectGuessesMap;

    private List<UUID> guessersList;

    private ActiveTask activeTask;

    public GameManager(Game game) {
        this.game = game;
        this.wordsList = game.getWords();
        this.playerSidebarMap = new HashMap<>();
        this.playerPointMap = new HashMap<>();
        this.playerCorrectGuessesMap = new HashMap<>();
        this.playerIncorrectGuessesMap = new HashMap<>();
        this.skippedPlayers = new ArrayList<>();
        this.force = false;
    }

    public void joinGame(Player player) {
        UUID uuid = player.getUniqueId();
        if (game.isEnabled()) {
            if (game.isGameState(GameState.WAITING) || game.isGameState(GameState.STARTING)) {
                if (DrawIt.getInstance().isInGame(player)) {
                    player.sendMessage(TextUtil.colorize(MessagesUtils.IN_GAME));
                } else if (game.getPlayers().size() >= game.getMaxPlayers()) {
                    player.sendMessage(TextUtil.colorize(MessagesUtils.FULL_GAME));
                } else {
                    game.getPlayers().add(uuid);
                    DrawIt.getInstance().setPlayerGame(player, game);
                    activateGameSettings(player);
                    player.teleport(game.getLobbyLocation());
                    sendMessage(MessagesUtils.PLAYER_JOIN.replace("{player}", player.getDisplayName()));
                    if (game.getPlayers().size() >= game.getMinPlayers() && !game.isGameState(GameState.STARTING)) {
                        startCountdown();
                    }
                }
            }
        }else {
            player.sendMessage(TextUtil.colorize(MessagesUtils.GAME_NOT_ENABLED));
        }

    }

    public void skip(Player player) {
        if (game.getGameManager().isDrawer(player)) {
            this.skippedPlayers.add(player.getUniqueId());
            game.getGameManager().sendMessage(MessagesUtils.GAME_SKIPPED.replace("{drawer}", game.getGameManager().getCurrentDrawer().getDisplayName()));
            game.getGameManager().getActiveTask().startNext();
        } else {
            player.sendMessage(TextUtil.colorize(MessagesUtils.NOT_DRAWER_TO_SKIP));
        }
    }

    public void leaveGame(Player player) {
        UUID uuid = player.getUniqueId();
        if (!game.getPlayers().contains(uuid)) return;
        game.getPlayers().remove(uuid);
        DrawIt.getInstance().setPlayerGame(player, null);
        if (game.isGameState(GameState.ACTIVE)) {
            waitingPlayers.remove(uuid);
            if (isDrawer(player)) {
                setDrawer(null);
            }
        }
        sendMessage(MessagesUtils.PLAYER_QUIT.replace("{player}", player.getDisplayName()));
    }

    public ActiveTask getActiveTask() {
        return activeTask;
    }

    public void startCountdown() {
        game.setGameState(GameState.STARTING);
        StartingTask startingTask = new StartingTask(game);
        startingTask.runTaskTimer(DrawIt.getInstance(), 0, 20);
    }

    public void startGame() {
        game.setGameState(GameState.ACTIVE);
        this.waitingPlayers = new ArrayList<>(game.getPlayers());
        for (UUID uuid : game.getPlayers()) {
            SideBar sideBar = new SideBar(Bukkit.getPlayer(uuid));
            sideBar.updateTitle(MessagesUtils.SCOREBOARD_GAME_TITLE);
            reloadSidebar(sideBar, -1);
            this.playerSidebarMap.put(uuid, sideBar);
        }
        startNextRound();
    }

    public void startNextRound() {
        game.getGameManager().setDrawer(null);
        this.game.getBoard().burn();
        if (isNoWaiting()) {
            this.game.getBoard().burn();
            game.setGameState(GameState.ENDING);
            new EndingTask(game).runTaskLater(DrawIt.getInstance(), 200L);
            return;
        }
        this.guessersList = new ArrayList<>();
        updateSidebars(-1);
        Bukkit.getScheduler().runTaskLater(DrawIt.getInstance(), () -> {
            WordChooseTask wordChooseTask = new WordChooseTask(game);
            wordChooseTask.runTaskTimer(DrawIt.getInstance(), 0, 20);
        }, 40L);
    }

    public void activateDrawerSettings(Player player) {
        activateGameSettings(player);
        addDrawingTools(player);
    }

    private void addDrawingTools(Player player) {
        Inventory inv = player.getInventory();
        inv.setItem(ConfigUtils.THIN_BRUSH_SLOT, DrawTools.THIN_BRUSH);
        inv.setItem(ConfigUtils.THICK_BRUSH_SLOT, DrawTools.THICK_BRUSH);
        inv.setItem(ConfigUtils.SPRAY_CAN_SLOT, DrawTools.SPRAY_CAN);
        inv.setItem(ConfigUtils.FILL_CAN_SLOT, DrawTools.FILL_CAN);
        inv.setItem(ConfigUtils.BURN_CANVAS_SLOT, DrawTools.BURN_CANVAS);
    }

    public void activateGameSettings(Player player) {
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0.0F);
        player.getInventory().setArmorContents(null);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public Player getCurrentDrawer() {
        return drawer;
    }

    public Player getNextDrawer() {
        if (!isNoWaiting()) {
            int randInt = new Random().nextInt(waitingPlayers.size());
            UUID drawerUUID = waitingPlayers.get(randInt);
            Player newDrawer = Bukkit.getPlayer(drawerUUID);
            waitingPlayers.remove(drawerUUID);
            return newDrawer;
        }else {
            return null;
        }
    }

    public void setDrawer(Player drawer) {
        this.drawer = drawer;
    }

    public boolean isDrawer(Player drawer) {
        return this.drawer == drawer;
    }

    public boolean isNoWaiting() {
        return waitingPlayers.isEmpty();
    }

    public List<String> getWordsForPlayer() {
        if (game.getPlayers().isEmpty()) return null;
        Random random = new Random();
        List<String> wordsForPlayer = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int rand = random.nextInt(wordsList.size());
            wordsForPlayer.add(wordsList.get(rand));
            wordsList.remove(wordsList.get(rand));
        }
        return wordsForPlayer;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void clearTasks() {
        for (BukkitRunnable br : game.getTasks()) {
            br.cancel();
        }
     }

    public List<UUID> getGuessers() {
        List<UUID> guessers = new ArrayList<>();
        for (UUID uuid : game.getPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (!p.equals(drawer)) {
                guessers.add(p.getUniqueId());
            }
        }
        return guessers;
    }

    public void sendMessage(String message) {
        for (UUID uuid : game.getPlayers()) {
            Bukkit.getPlayer(uuid).sendMessage(TextUtil.colorize(message));
        }
    }

    public void sendMessage(List<String> messageList) {
        for (String message : messageList) {
            sendMessage(message);
        }
    }

    public void playSound(Sound sound, float volume, float pitch) {
        for (UUID uuid : game.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            DrawIt.getInstance().playSound(player, sound, volume, pitch);
        }
    }

    public void sendActionBar(Player p, String s) {
        ActionBar.sendActionBar(p, TextUtil.colorize(s));
    }

    public void sendActionBar(String s) {
        for (UUID uuid : game.getPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            sendActionBar(p, s);
        }
    }

    public void sendActionBarToGuessers(String s) {
        if (drawer == null) return;
        for (UUID uuid : getGuessers()) {
            Player p = Bukkit.getPlayer(uuid);
            sendActionBar(p, s);
        }
    }

    public void sendTitle(Player player, String title, String subTitle, int fadeIn, int fadeOut, int stay) {
        if (player == null) return;
        Titles.sendTitle(player, fadeIn, stay, fadeOut, TextUtil.colorize(title), TextUtil.colorize(subTitle));
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
        for (UUID uuid : game.getPlayers()) {
            Player p = Bukkit.getPlayer(uuid);
           sendTitle(p, title, subTitle, fadeIn, fadeOut, stay);
        }
    }

    public void resetGame() {
        clearTasks();
    }

    public void reloadSidebar(SideBar sideBar, int time) {
        List<String> newLines = new ArrayList<>();
        for (String s : MessagesUtils.SCOREBOARD_GAME_LINES) {
            newLines.add(s
                    .replace("{time}", time >= 0 ? OtherUtils.formatTime(time) : "&8Waiting...")
                    .replace("{drawer}", drawer != null ? "&f"+drawer.getDisplayName() : "&8None")
                    .replace("{rounds_left}", "&f"+getWaitingPlayers().size())
                    .replace("{leader_1}", getLeader(0))
                    .replace("{leader_2}", getLeader(1))
                    .replace("{leader_3}", getLeader(2)));
        }
        sideBar.updateLines(newLines);
    }

    public void updateSidebars(int time) {
        for (SideBar sideBar : playerSidebarMap.values()) {
            reloadSidebar(sideBar, time);
        }
    }

    public List<UUID> getWaitingPlayers() {
        return waitingPlayers;
    }

    public void addPoint(Player player, int point) {
        UUID uuid = player.getUniqueId();
        playerPointMap.put(uuid, getPoint(player)+point);
        if (!isDrawer(player)) {
            game.getGameManager().sendMessage(MessagesUtils.PLAYER_GUESSED.replace("{guesser}", player.getDisplayName()).replace("{points}", String.valueOf(point)));
        }
    }

    public int getPoint(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerPointMap.containsKey(uuid)) {
            playerPointMap.put(uuid, 0);
        }
        return playerPointMap.get(uuid);
    }

    public String getLeaderName(int i) {
        if (getLeaders().size() <= i) return "Nobody";
        UUID uuid = getLeaders().get(i).getKey();
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return "Nobody";
        return player.getDisplayName();
    }

    public int getLeaderPoint(UUID uuid) {
        if (getLeaders().isEmpty()) return 0;
        for (Map.Entry<UUID, Integer> map : getLeaders()) {
            if (map.getKey().equals(uuid)) {
                return map.getValue();
            }
        }
        return 0;
    }

    public List<Map.Entry<UUID, Integer>> getLeaders() {
        List<Map.Entry<UUID, Integer>> list = new ArrayList<>(playerPointMap.entrySet());
        list.sort((e1, e2) -> -e1.getValue().compareTo(e2.getValue()));
        return list;
    }

    public String getLeader(int i) {
        if (getLeaders().size() <= i) return "&8Waiting...";
        Map.Entry<UUID, Integer> e = getLeaders().get(i);
        String playerName = Bukkit.getPlayer(e.getKey()).getDisplayName();
        return MessagesUtils.LEADER_FORMAT.replace("{point}", String.valueOf(e.getValue())).replace("{player}", playerName);
    }

    public List<UUID> getWordGuessers() {
        return guessersList;
    }

    public void setActiveTask(ActiveTask activeTask) {
        this.activeTask = activeTask;
    }

    public int getCorrectGuesses(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerCorrectGuessesMap.containsKey(uuid)) {
            playerCorrectGuessesMap.put(uuid, 0);
        }
        return playerCorrectGuessesMap.get(uuid);
    }

    public void addCorrectGuess(Player player) {
        UUID uuid = player.getUniqueId();
        playerCorrectGuessesMap.put(uuid, getCorrectGuesses(player)+1);
    }

    public int getIncorrectGuesses(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerIncorrectGuessesMap.containsKey(uuid)) {
            playerIncorrectGuessesMap.put(uuid, 0);
        }
        return playerIncorrectGuessesMap.get(uuid);
    }

    public void addIncorrectGuess(Player player) {
        UUID uuid = player.getUniqueId();
        playerIncorrectGuessesMap.put(uuid, getIncorrectGuesses(player)+1);
    }

    public boolean isForce() {
        return force;
    }

    public void setForce() {
        this.force = true;
    }

    public List<UUID> getSkippedPlayers() {
        return skippedPlayers;
    }

}
