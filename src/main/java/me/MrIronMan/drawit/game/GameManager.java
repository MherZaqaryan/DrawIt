package me.MrIronMan.drawit.game;

import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import de.tr7zw.nbtapi.NBTItem;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.events.player.PlayerJoinGameEvent;
import me.MrIronMan.drawit.api.events.player.PlayerQuitGameEvent;
import me.MrIronMan.drawit.api.game.GameState;
import me.MrIronMan.drawit.data.ConfigData;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.tasks.PlayingTask;
import me.MrIronMan.drawit.game.tasks.RestartingTask;
import me.MrIronMan.drawit.game.tasks.StartingTask;
import me.MrIronMan.drawit.game.tasks.WordChooseTask;
import me.MrIronMan.drawit.api.game.DrawerTool;
import me.MrIronMan.drawit.game.utility.SideBar;
import me.MrIronMan.drawit.utility.OtherUtils;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class GameManager {

    private final Game game;
    private String word;
    private Player drawer;

    private final List<String> wordsList;
    private List<UUID> waitingPlayers;
    private final List<UUID> skippedPlayers;
    private List<UUID> guessersList;

    private final HashMap<UUID, SideBar> playerSidebarMap;
    private final HashMap<UUID, Integer> playerPointMap;
    private final HashMap<UUID, Integer> playerCorrectGuessesMap;
    private final HashMap<UUID, Integer> playerIncorrectGuessesMap;

    private PlayingTask activeTask;
    private boolean force;

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
        PlayerJoinGameEvent event = new PlayerJoinGameEvent(player, this.game);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        if (!game.isEnabled()) {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.GAME_NOT_ENABLED)));
            return;
        }
        if (game.isGameState(GameState.WAITING) || game.isGameState(GameState.STARTING)) {
            if (DrawIt.getInstance().isInGame(player)) {
                player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.IN_GAME)));
            }
            else if (game.getUuids().size() >= game.getMaxPlayers()) {
                player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.FULL_GAME)));
            }
            else {
                game.getUuids().add(uuid);
                DrawIt.getInstance().setPlayerGame(player, game);
                activateGameSettings(player);
                player.teleport(game.getLobbyLocation());
                setWaitingItems(player);
                sendMessage(DrawIt.getMessagesData().getString(MessagesData.PLAYER_JOIN).replace("{player}", player.getDisplayName()));
                if (game.getUuids().size() >= game.getMinPlayers() && !game.isGameState(GameState.STARTING)) {
                    startCountdown();
                }
            }
        }
        else if (game.isGameState(GameState.PLAYING)) {
            if (DrawIt.getInstance().isInGame(player)) {
                player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.IN_GAME)));
            }
            else {
                activateSpectatorSettings(player);
            }
        }
    }

    public void skip(Player player) {
        if (!game.getGameManager().isDrawer(player)) {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NOT_DRAWER_TO_SKIP)));
            return;
        }
        this.skippedPlayers.add(player.getUniqueId());
        game.getGameManager().sendMessage(DrawIt.getMessagesData().getString(MessagesData.GAME_SKIPPED).replace("{drawer}", game.getGameManager().getCurrentDrawer().getDisplayName()));
        game.getGameManager().getActiveTask().startNext();
    }

    public void leaveGame(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerQuitGameEvent event = new PlayerQuitGameEvent(player, game);
        Bukkit.getPluginManager().callEvent(event);
        if (!game.getUuids().contains(uuid)) return;
        game.getUuids().remove(uuid);
        DrawIt.getInstance().setPlayerGame(player, null);
        if (game.isGameState(GameState.PLAYING)) {
            waitingPlayers.remove(uuid);
            if (!isDrawer(player)) {
                setDrawer(null);
            }
        }
        game.removeSpectator(uuid);
        sendMessage(DrawIt.getMessagesData().getString(MessagesData.PLAYER_QUIT).replace("{player}", player.getDisplayName()));
    }


    public void startCountdown() {
        game.setGameState(GameState.STARTING);
        StartingTask startingTask = new StartingTask(game);
        startingTask.runTaskTimer(DrawIt.getInstance(), 0, 20);
    }

    public void startGame() {
        game.setGameState(GameState.PLAYING);
        this.waitingPlayers = new ArrayList<>(game.getUuids());
        game.getUuids().forEach(this::setScoreboard);
        game.getUuids().forEach(id -> activateGameSettings(Bukkit.getPlayer(id)));
        startNextRound();
    }

    public void startNextRound() {
        game.getGameManager().setActiveTask(null);
        game.getGameManager().setDrawer(null);
        this.game.getBoard().burn(game.getBoardColor());
        if (isNoWaiting()) {
            this.game.getBoard().burn(game.getBoardColor());
            game.setGameState(GameState.RESTARTING);
            new RestartingTask(game).runTaskLater(DrawIt.getInstance(), DrawIt.getConfigData().getInt(ConfigData.COUNTDOWN_RESTART) * 20L);
            return;
        }
        this.guessersList = new ArrayList<>();
        updateSidebars(-1);
        Bukkit.getScheduler().runTaskLater(DrawIt.getInstance(), () -> {
            WordChooseTask wordChooseTask = new WordChooseTask(game);
            wordChooseTask.runTaskTimer(DrawIt.getInstance(), 0, 20);
        }, DrawIt.getConfigData().getInt(ConfigData.COUNTDOWN_AFTER_ROUND) * 20L);
    }

    public void activateDrawerSettings() {
        activateGameSettings(drawer);
        addDrawingTools(drawer);
    }

    private void addDrawingTools(Player player) {
        Inventory inv = player.getInventory();
        for (DrawerTool tool : DrawerTool.values()) {
            NBTItem nbti = new NBTItem(DrawIt.getConfigData().getDrawerTool(tool));
            inv.setItem(nbti.getInteger("slot"), nbti.getItem());
        }
    }

    public void setWaitingItems(Player player) {
        Inventory inv = player.getInventory();
        for (ItemStack itemStack : DrawIt.getConfigData().getWaitingItems()) {
            NBTItem nbtItem = new NBTItem(itemStack);
            inv.setItem(nbtItem.getInteger("slot"), itemStack);
        }
    }

    public void activateGameSettings(Player player) {
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0.0F);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.getInventory().setArmorContents(null);
        for (Player p : DrawIt.getInstance().getLobbyPlayers()) {
            player.hidePlayer(p);
            p.hidePlayer(player);
        }
        for (UUID uuid : game.getUuids()) {
            Player p = Bukkit.getPlayer(uuid);
            player.showPlayer(p);
            p.showPlayer(player);
        }
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public void activateSpectatorSettings(Player player) {
        activateGameSettings(player);
        DrawIt.getInstance().setPlayerGame(player, game);
        game.setSpectator(player.getUniqueId());
        player.setAllowFlight(true);
        player.setFlying(true);
        player.teleport(game.getLobbyLocation());
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
        setScoreboard(player.getUniqueId());
        for (Player p : DrawIt.getInstance().getLobbyPlayers()) {
            p.hidePlayer(player);
            player.hidePlayer(p);
        }
        for (UUID uuid : game.getUuids()) {
            Player p = Bukkit.getPlayer(uuid);
            player.showPlayer(p);
            p.hidePlayer(player);
        }
        for (UUID uuid : game.getSpectators()) {
            Player p = Bukkit.getPlayer(uuid);
            p.showPlayer(player);
            player.showPlayer(p);
        }
        if (DrawIt.getConfigData().getSpectatorItems().isEmpty()) return;
        PlayerInventory inv = player.getInventory();
        for (ItemStack itemStack : DrawIt.getConfigData().getSpectatorItems()) {
            if (itemStack.getType().equals(Material.AIR)) continue;
            NBTItem nbti = new NBTItem(itemStack);
            inv.setItem(nbti.getInteger("slot"), itemStack);
        }
    }

    public Player getNextDrawer() {
        if (isNoWaiting()) return null;
        int randInt = new Random().nextInt(waitingPlayers.size());
        UUID drawerUUID = waitingPlayers.get(randInt);
        Player newDrawer = Bukkit.getPlayer(drawerUUID);
        waitingPlayers.remove(drawerUUID);
        return newDrawer;
    }

    public List<String> getWordsForPlayer() {
        if (game.getUuids().isEmpty()) return null;
        Random random = new Random();
        List<String> wordsForPlayer = new ArrayList<>();
        for (int i = 0; i < DrawIt.getConfigData().getIntegerList(ConfigData.SELECT_WORD_MENU_SETTINGS_SLOTS).size(); i++) {
            int rand = random.nextInt(wordsList.size());
            wordsForPlayer.add(wordsList.get(rand));
            wordsList.remove(wordsList.get(rand));
        }
        return wordsForPlayer;
    }

    public List<UUID> getGuessers() {
        List<UUID> guessers = new ArrayList<>();
        for (UUID uuid : game.getUuids()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p.equals(drawer)) continue;
            guessers.add(p.getUniqueId());
        }
        return guessers;
    }

    public void reloadSidebar(SideBar sideBar, int time, Player player) {
        List<String> newLines = new ArrayList<>();
        DrawIt.getMessagesData().getStringList(MessagesData.BOARD_GAME_LINES)
                .forEach(s -> TextUtil.getByPlaceholders(s
                        .replace("{time}", time != -1 ? OtherUtils.formatTime(time) : DrawIt.getMessagesData().getString(MessagesData.SCOREBOARD_WAITING))
                        .replace("{drawer}", drawer != null ? "&f" + drawer.getDisplayName() : DrawIt.getMessagesData().getString(MessagesData.SCOREBOARD_NO_DRAWER))
                        .replace("{rounds_left}", "&f" + getWaitingPlayers().size())
                        .replace("{leader_1}", getLeader(0))
                        .replace("{leader_2}", getLeader(1))
                        .replace("{leader_3}", getLeader(2)), player));
        sideBar.updateLines(newLines);
    }

    public void updateSidebars(int time) {
        for (UUID uuid : playerSidebarMap.keySet()) {
            SideBar sideBar = playerSidebarMap.get(uuid);
            Player player = Bukkit.getPlayer(uuid);
            reloadSidebar(sideBar, time, player);
        }
    }

    public void addPoint(Player player, int point) {
        if (player == null) return;
        UUID uuid = player.getUniqueId();
        playerPointMap.put(uuid, getPoint(player) + point);
        if (isDrawer(player)) return;
        game.getGameManager().sendMessage(DrawIt.getMessagesData().getString(MessagesData.PLAYER_GUESSED).replace("{guesser}", player.getDisplayName()).replace("{points}", String.valueOf(point)));
    }

    public int getPoint(Player player) {
        if (player == null) return 0;
        UUID uuid = player.getUniqueId();
        if (!playerPointMap.containsKey(uuid)) playerPointMap.put(uuid, 0);
        return playerPointMap.get(uuid);
    }

    public String getLeaderName(int i) {
        if (getLeaders().size() <= i) return DrawIt.getMessagesData().getString(MessagesData.NO_BODY);
        UUID uuid = getLeaders().get(i).getKey();
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return DrawIt.getMessagesData().getString(MessagesData.NO_BODY);
        return player.getDisplayName();
    }

    public int getLeaderPoint(UUID uuid) {
        if (getLeaders().isEmpty()) return 0;
        for (Map.Entry<UUID, Integer> map : getLeaders()) {
            if (!map.getKey().equals(uuid)) continue;
            return map.getValue();
        }
        return 0;
    }

    public List<Map.Entry<UUID, Integer>> getLeaders() {
        List<Map.Entry<UUID, Integer>> list = new ArrayList<>(playerPointMap.entrySet());
        list.sort((e1, e2) -> -e1.getValue().compareTo(e2.getValue()));
        return list;
    }

    public String getLeader(int i) {
        if (getLeaders().size() <= i) return DrawIt.getMessagesData().getString(MessagesData.SCOREBOARD_WAITING);
        Map.Entry<UUID, Integer> e = getLeaders().get(i);
        String playerName = Bukkit.getPlayer(e.getKey()).getDisplayName();
        return DrawIt.getMessagesData().getString(MessagesData.SCOREBOARD_LEADER_FORMAT).replace("{point}", String.valueOf(e.getValue())).replace("{player}", playerName);
    }

    public int getCorrectGuesses(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerCorrectGuessesMap.containsKey(uuid)) playerCorrectGuessesMap.put(uuid, 0);
        return playerCorrectGuessesMap.get(uuid);
    }

    public void addCorrectGuess(Player player) {
        UUID uuid = player.getUniqueId();
        playerCorrectGuessesMap.put(uuid, getCorrectGuesses(player) + 1);
    }

    public int getIncorrectGuesses(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerIncorrectGuessesMap.containsKey(uuid)) playerIncorrectGuessesMap.put(uuid, 0);
        return playerIncorrectGuessesMap.get(uuid);
    }

    public void addIncorrectGuess(Player player) {
        UUID uuid = player.getUniqueId();
        playerIncorrectGuessesMap.put(uuid, getIncorrectGuesses(player) + 1);
    }

    public void setScoreboard(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        SideBar sideBar = new SideBar(player);
        sideBar.updateTitle(DrawIt.getMessagesData().getString(MessagesData.BOARD_GAME_TITLE));
        reloadSidebar(sideBar, -1, player);
        this.playerSidebarMap.put(uuid, sideBar);
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

    public PlayingTask getActiveTask() {
        return activeTask;
    }

    public void setActiveTask(PlayingTask activeTask) {
        this.activeTask = activeTask;
    }

    public List<UUID> getWordGuessers() {
        return guessersList;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Player getCurrentDrawer() {
        return drawer;
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

    public List<UUID> getWaitingPlayers() {
        return waitingPlayers;
    }

    // Utilities

    public void sendMessage(String message) {
        game.getUuids().forEach(id -> Bukkit.getPlayer(id).sendMessage(TextUtil.colorize(message)));
        game.getSpectators().forEach(id -> Bukkit.getPlayer(id).sendMessage(TextUtil.colorize(message)));
    }

    public void sendActionBar(Player p, String s) {
        ActionBar.sendActionBar(p, TextUtil.colorize(s));
    }

    public void sendActionBar(String s) {
        game.getPlayers().forEach(p -> sendActionBar(p, s));
        game.getSpectators().forEach(id -> sendActionBar(Bukkit.getPlayer(id), s));
    }

    public void sendActionBarToGuessers(String s) {
        if (drawer == null) return;
        this.getGuessers().forEach(id -> sendActionBar(Bukkit.getPlayer(id), s));
        game.getSpectators().forEach(id -> sendActionBar(Bukkit.getPlayer(id), s));
    }

    public void sendTitle(Player player, String title, String subTitle, int fadeIn, int fadeOut, int stay) {
        if (player == null) return;
        Titles.sendTitle(player, fadeIn, stay, fadeOut, TextUtil.colorize(title), TextUtil.colorize(subTitle));
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
        game.getPlayers().forEach(p -> sendTitle(p, title, subTitle, fadeIn, fadeOut, stay));
        game.getSpectators().forEach(p -> sendTitle(Bukkit.getPlayer(p), title, subTitle, fadeIn, fadeOut, stay));
    }

    public void playSound(String sound) {
        game.getPlayers().forEach(p -> XSound.play(p, sound));
        game.getSpectators().forEach(p -> XSound.play(Bukkit.getPlayer(p), sound));
    }

}
