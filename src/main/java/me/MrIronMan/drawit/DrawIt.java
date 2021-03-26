package me.MrIronMan.drawit;

import com.cryptomorin.xseries.XSound;
import me.MrIronMan.drawit.commands.DrawItCommand;
import me.MrIronMan.drawit.commands.LeaveCommand;
import me.MrIronMan.drawit.commands.SkipCommand;
import me.MrIronMan.drawit.data.ConfigUtils;
import me.MrIronMan.drawit.data.CustomConfig;
import me.MrIronMan.drawit.data.MessagesUtils;
import me.MrIronMan.drawit.data.WordsUtils;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.listeners.*;
import me.MrIronMan.drawit.menuSystem.PlayerMenuUtility;
import me.MrIronMan.drawit.sql.MySQL;
import me.MrIronMan.drawit.sql.PlayerData;
import me.MrIronMan.drawit.sql.PlayerDataType;
import me.MrIronMan.drawit.sql.SQLite;
import me.MrIronMan.drawit.utility.OtherUtils;
import me.MrIronMan.drawit.game.utility.SideBar;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.ReflectionUtils;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;


public class DrawIt extends JavaPlugin {

    private static DrawIt instance;

    private List<Game> games = new ArrayList<>();

    private CustomConfig config;
    private CustomConfig messages;
    private CustomConfig words;

    private SQLite sqLite;
    private MySQL mySQL;

    private static HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static HashMap<Player, PlayerData> playerDataMap = new HashMap<>();

    private HashMap<Player, Game> playerGameMap = new HashMap<>();
    private HashMap<Player, SideBar> lobbySidebarMap = new HashMap<>();
    private HashMap<Player, SetupGame> setupGameMap = new HashMap<>();

    private Location lobbyLocation;

    @Override
    public void onEnable() {
        instance = this;
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
        if (ConfigUtils.MYSQL_ENABLED) {
            this.mySQL = new MySQL(
                ConfigUtils.MYSQL_HOST,
                ConfigUtils.MYSQL_PORT,
                ConfigUtils.MYSQL_DATABASE,
                ConfigUtils.MYSQL_USERNAME,
                ConfigUtils.MYSQL_PASSWORD
            );
        }else {
            this.sqLite = new SQLite();
        }
        new PlayerData();
    }

    public void disconnectDatabase() {
        if (ConfigUtils.MYSQL_ENABLED) {
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
        config = new CustomConfig(this, "Config.yml");
        messages = new CustomConfig(this, "Messages.yml");
        words = new CustomConfig(this, "Words.yml");
        this.lobbyLocation = OtherUtils.readLocation(ConfigUtils.LOBBY_LOCATION);
        new WordsUtils().sort();
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

    public CustomConfig getConfigData() {
        return config;
    }

    public CustomConfig getMessagesFile() {
        return messages;
    }

    public CustomConfig getWordsData() {
        return words;
    }

    public void activateLobbySettings(Player player) {
        if (isLobbySet()) {
            player.teleport(lobbyLocation);
        }else {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            if (player.hasPermission(PermissionsUtil.COMMAND_SETMAINLOBBY)) {
                player.sendMessage(TextUtil.colorize(MessagesUtils.LOBBY_NOT_SET));
            }
        }
        setLobbySidebar(player);
        Inventory pi = player.getInventory();
        for (Map.Entry<Integer, ItemStack> item : ConfigUtils.getLobbyItems()) {
            if (item.getValue().getType().toString().contains("SKULL_ITEM")) {
                SkullMeta skullMeta = (SkullMeta) item.getValue().getItemMeta();
                skullMeta.setOwner(player.getDisplayName());
                item.getValue().setItemMeta(skullMeta);
            }
            pi.setItem(item.getKey(), item.getValue());
        }
    }

    public void setLobbyLocation(Location loc) {
        ConfigUtils.getData().set("lobby-location", OtherUtils.writeLocation(loc, true));
        getConfigData().saveConfig();
    }

    public void setLobbySidebar(Player player) {
        SideBar sideBar = new SideBar(player);
        sideBar.updateTitle(MessagesUtils.SCOREBOARD_LOBBY_TITLE);
        lobbySidebarMap.put(player, sideBar);
        updateSidebar(player);
    }

    public void updateSidebar(Player player) {
        List<String> newLines = new ArrayList<>();
        PlayerData pd = DrawIt.getPlayerData(player);
        for (String s : MessagesUtils.SCOREBOARD_LOBBY_LINES) {
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

    public List<Game> getGames() {
        return this.games;
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
        player.sendMessage(TextUtil.colorize(MessagesUtils.EXIT_SETUP));
    }

    public boolean isInSetup(Player player) {
        return setupGameMap.containsKey(player);
    }

    public SetupGame getSetupGame(Player player) {
        if (!isInSetup(player)) return null;
        return setupGameMap.get(player);
    }

    public void playSound(Player player, Sound sound, float volume, float pitch) {
        XSound.play(player, sound.toString() + "," + volume + "," + pitch);
    }

    public boolean isLobbySet() {
        return lobbyLocation != null;
    }

    public static PlayerData getPlayerData(String name) {
        for (Map.Entry<Player, PlayerData> data : playerDataMap.entrySet()) {
            if (data.getKey().getName().equalsIgnoreCase(name)) {
                return data.getValue();
            }
        }
        return null;
    }

}
