package me.MrIronMan.drawit;

import de.tr7zw.nbtapi.NBTItem;
import me.MrIronMan.drawit.commands.DrawItCommand;
import me.MrIronMan.drawit.commands.LeaveCommand;
import me.MrIronMan.drawit.commands.SkipCommand;
import me.MrIronMan.drawit.data.*;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.listeners.*;
import me.MrIronMan.drawit.menuSystem.PlayerMenuUtility;
import me.MrIronMan.drawit.sql.MySQL;
import me.MrIronMan.drawit.sql.PlayerData;
import me.MrIronMan.drawit.sql.PlayerDataType;
import me.MrIronMan.drawit.sql.SQLite;
import me.MrIronMan.drawit.game.utility.SideBar;
import me.MrIronMan.drawit.utility.OtherUtils;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.ReflectionUtils;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;


public class DrawIt extends JavaPlugin {

    private static DrawIt instance;

    private List<Game> games = new ArrayList<>();

    private static ConfigData config;
    private static MessagesData messages;
    private static WordsData words;

    private SQLite sqLite;
    private MySQL mySQL;

    private static HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static HashMap<Player, PlayerData> playerDataMap = new HashMap<>();

    private HashMap<Player, Game> playerGameMap = new HashMap<>();
    private HashMap<Player, SideBar> lobbySidebarMap = new HashMap<>();
    private HashMap<Player, SetupGame> setupGameMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        loggerMessage();
        loadDataFiles();
        loadCommands();
        registerListeners();
        loadDataFiles();
        loadGames();
        connectDatabase();
    }

    @Override
    public void onDisable() {
        disconnectDatabase();
        Bukkit.getScheduler().cancelAllTasks();
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) {
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);
            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p);
        }
    }

    public void connectDatabase() {
        if (getConfigData().isMySql()) {
            this.mySQL = new MySQL(
                getConfigData().getMySqlInfo().get(0),
                getConfigData().getMySqlInfo().get(1),
                getConfigData().getMySqlInfo().get(2),
                getConfigData().getMySqlInfo().get(3),
                getConfigData().getMySqlInfo().get(4)
            );
        }else {
            this.sqLite = new SQLite();
        }
        new PlayerData();
    }

    public void disconnectDatabase() {
        if (getConfigData().isMySql()) {
            this.mySQL.disconnect();
        }else {
            this.sqLite.disconnect();
        }
    }

    public void registerListeners() {
        Arrays.asList(
            new GameListener(),
            new InteractEvent(),
            new SystemListener(),
            new JoinQuitListener(),
            new GuessWordListener())
            .forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    public void loadDataFiles() {
        config = new ConfigData(this, getDataFolder().getPath(), "Config");
        messages = new MessagesData(this, getDataFolder().getPath(), "Messages");
        words = new WordsData(this, getDataFolder().getPath(), "Words");
    }

    public void loadCommands() {
        ReflectionUtils.registerCommand("DrawIt", new DrawItCommand("DrawIt"));
        ReflectionUtils.registerCommand("Leave", new LeaveCommand("Leave"));
        ReflectionUtils.registerCommand("Skip", new SkipCommand("Skip"));
    }

    public void loadGames() {
        File gamesFolder = new File(getDataFolder().getPath() + File.separator + "Games");
        File[] gameFiles = gamesFolder.listFiles();
        if (gameFiles == null) return;
        for (File gameFile : gameFiles) {
            StringBuilder sb = new StringBuilder(gameFile.getName());
            if (gameFile.getName().endsWith(".yml")) {
                Game game = new Game(sb.delete(sb.length()-4, sb.length()).toString());
                registerGame(game);
            }
        }
    }



    public static DrawIt getInstance() {
        return instance;
    }

    public List<Game> getGames() {
        return this.games;
    }

    public boolean isInGame(Player player) {
        return playerGameMap.get(player) != null;
    }

    public void setPlayerGame(Player player, Game game) {
        this.playerGameMap.put(player, game);
    }

    public Game getGame(Player player) {
        return playerGameMap.getOrDefault(player, null);
    }

    public Game getGame(String game) {
        for (Game g : games) {
            if (g.getName().equalsIgnoreCase(game)) {
                return g;
            }
        }
        return null;
    }

    public void registerGame(String s) {
        Game game = new Game(s);
        registerGame(game);
    }

    public void registerGame(Game game) {
        games.add(game);
    }

    public void restartGame(String gameName) {
        if (this.getGame(gameName) == null) return;
        Game gameToRestart = this.getGame(gameName);
        this.games.remove(gameToRestart);
        gameToRestart = new Game(gameName);
        this.games.add(gameToRestart);
    }

    public void quickJoinGame(Player player) {
        HashMap<Game, Integer> gameMap = new HashMap<>();
        for (Game game : games) {
            gameMap.put(game, game.getPlayers().size());
        }
        List<Map.Entry<Game, Integer>> list = new ArrayList<>(gameMap.entrySet());
        list.sort((e1, e2) -> -e1.getValue().compareTo(e2.getValue()));
        list.get(0).getKey().getGameManager().joinGame(player);
    }

    public static ConfigData getConfigData() {
        return config;
    }

    public static MessagesData getMessagesData() {
        return messages;
    }

    public static WordsData getWordsData() {
        return words;
    }

    public void activateLobbySettings(Player player) {
        teleportToLobby(player);
        player.setGameMode(GameMode.ADVENTURE);
        player.setLevel(0);
        player.setExp(0.0F);
        player.setHealth(20.0D);
        PlayerInventory pi = player.getInventory();
        pi.clear();
        pi.setArmorContents(null);
        for (ItemStack item : getConfigData().getLobbyItems()) {
            NBTItem nbti = new NBTItem(item);
            if (item.getType().toString().contains("SKULL_ITEM")) {
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setOwner(player.getDisplayName());
                item.setItemMeta(skullMeta);
            }
            pi.setItem(nbti.getInteger("slot"), item);
        }
        setLobbySidebar(player);
    }

    public void setLobbyLocation(Location loc) {
        getConfigData().saveLocation("lobby-location", loc);
    }

    public void setLobbySidebar(Player player) {
        SideBar sideBar = new SideBar(player);
        sideBar.updateTitle(getMessagesData().getString(MessagesData.BOARD_LOBBY_TITLE));
        lobbySidebarMap.put(player, sideBar);
        updateSidebar(player);
    }

    public void updateSidebar(Player player) {
        List<String> newLines = new ArrayList<>();
        PlayerData pd = DrawIt.getPlayerData(player);
        for (String s : getMessagesData().getStringList(MessagesData.BOARD_LOBBY_LINES)) {
            newLines.add(s
                .replace("{tokens}", String.valueOf(pd.getData(PlayerDataType.TOKENS)))
                .replace("{points}", String.valueOf(pd.getData(PlayerDataType.POINTS)))
                .replace("{games_played}", String.valueOf(pd.getData(PlayerDataType.GAMES_PLAYED)))
                .replace("{victories}", String.valueOf(pd.getData(PlayerDataType.VICTORIES)))
                .replace("{correct_guesses}", String.valueOf(pd.getData(PlayerDataType.CORRECT_GUESSES)))
                .replace("{incorrect_guesses}", String.valueOf(pd.getData(PlayerDataType.INCORRECT_GUESSES)))
                .replace("{skips}", String.valueOf(pd.getData(PlayerDataType.SKIPS))));
        }
        lobbySidebarMap.get(player).updateLines(newLines);
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public SQLite getSqLite() {
        return sqLite;
    }

    public static PlayerData getPlayerData(Player player) {
        if (!playerDataMap.containsKey(player)) {
            playerDataMap.put(player, new PlayerData(player));
        }
        return playerDataMap.get(player);
    }

    public static HashMap<Player, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }


    public void enableSetupMode(Player player, SetupGame setupGame) {
        setupGameMap.put(player, setupGame);
        player.sendMessage(TextUtil.colorize("{prefix} &aLoading world &2" + setupGame.getName() + "&a, please wait..."));
        WorldCreator wc = new WorldCreator(setupGame.getName());
        World world = wc.createWorld();
        player.getInventory().clear();
        player.setGameMode(GameMode.CREATIVE);
        player.teleport(world.getSpawnLocation());
        player.setFlying(true);
    }

    public void exitSetupMode(Player player) {
        setupGameMap.remove(player);
        DrawIt.getInstance().activateLobbySettings(player);
        player.sendMessage(TextUtil.colorize(PluginMessages.EXIT_SETUP));
    }

    public boolean isInSetup(Player player) {
        return setupGameMap.containsKey(player);
    }

    public SetupGame getSetupGame(Player player) {
        if (!isInSetup(player)) return null;
        return setupGameMap.get(player);
    }

    public void teleportToLobby(Player player) {
        if (isLobbySet()) {
            player.teleport(OtherUtils.readLocation(getConfigData().getString(ConfigData.LOBBY_LOCATION)));
        }else {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            if (player.hasPermission(PermissionsUtil.COMMAND_SETMAINLOBBY)) {
                player.sendMessage(TextUtil.colorize(PluginMessages.LOBBY_NOT_SET));
            }
        }
    }

    public boolean isLobbySet() {
        return getConfigData().getString(ConfigData.LOBBY_LOCATION) != null;
    }

    public static void updateGameSelector() {
        for (PlayerMenuUtility pmu : playerMenuUtilityMap.values()) {
            if (pmu.getGameSelector() != null) {
                pmu.getGameSelector().setMenuItems();
            }
        }
    }

    private void loggerMessage() {
        Bukkit.getConsoleSender().sendMessage("§b __                  ");
        Bukkit.getConsoleSender().sendMessage("§b|  \\  _  _      §a| |_ ");
        Bukkit.getConsoleSender().sendMessage("§b|__/ |  (_| \\)/ §a| |_ ");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§cAuthor: §aMrIronMan (Mher)");
        Bukkit.getConsoleSender().sendMessage("§cVersion: §a" + getDescription().getVersion());
    }

}
