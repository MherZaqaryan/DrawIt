package me.MrIronMan.drawit;

import de.tr7zw.nbtapi.NBTItem;
import me.MrIronMan.drawit.commands.DrawItCommand;
import me.MrIronMan.drawit.commands.LeaveCommand;
import me.MrIronMan.drawit.commands.SkipCommand;
import me.MrIronMan.drawit.data.*;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameState;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.listeners.*;
import me.MrIronMan.drawit.menu.PlayerMenuUtility;
import me.MrIronMan.drawit.sql.MySQL;
import me.MrIronMan.drawit.sql.PlayerData;
import me.MrIronMan.drawit.sql.PlayerDataType;
import me.MrIronMan.drawit.sql.SQLite;
import me.MrIronMan.drawit.game.utility.SideBar;
import me.MrIronMan.drawit.support.PlaceholderAPI;
import me.MrIronMan.drawit.utility.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

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
    private static HashMap<Player, Boolean> playerBuildMap = new HashMap<>();

    private HashMap<Player, Game> playerGameMap = new HashMap<>();
    private HashMap<Player, SideBar> lobbySidebarMap = new HashMap<>();
    private HashMap<Player, SetupGame> setupGameMap = new HashMap<>();

    private boolean isPlaceholderAPI = false;

    @Override
    public void onEnable() {
        instance = this;
        Metrics metrics = new Metrics(this, 11065);
        loggerMessage();
        loadDataFiles();
        loadCommands();
        registerListeners();
        loadDataFiles();
        loadGames();
        connectDatabase();
        placeholderApiHook();
    }

    @Override
    public void onDisable() {
        disconnectDatabase();
        Bukkit.getScheduler().cancelAllTasks();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("Server Reloading!");
        }
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
            new InteractListener(),
            new SystemListener(),
            new JoinQuitListener(),
            new ChatListener(),
            new MainListener())
            .forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
        if (!ReflectionUtils.VERSION.contains("1_8")) {
            Bukkit.getPluginManager().registerEvents(new SwapItemListener(), this);
        }
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

    public List<Game> getGames() {
        return this.games;
    }

    public Set<World> getGamesWorlds() {
        Set<World> worlds = new HashSet<>();
        for (Game game : games) {
            if (game.isEnabled()) {
                worlds.add(game.getWorld());
            }
        }
        return worlds;
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
            if (game.isGameState(GameState.STARTING) || game.isGameState(GameState.WAITING)) {
                gameMap.put(game, game.getPlayers().size());
            }
        }
        List<Map.Entry<Game, Integer>> list = new ArrayList<>(gameMap.entrySet());
        list.sort((e1, e2) -> -e1.getValue().compareTo(e2.getValue()));
        if (gameMap.isEmpty() || !list.get(0).getKey().isEnabled()) {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.QUICK_JOIN_GAME_NOT_FOUND)));
            return;
        }
        list.get(0).getKey().getGameManager().joinGame(player);
    }

    public void activateLobbySettings(Player player) {
        if (player.hasPermission(PermissionsUtil.COMMAND_BUILD)) {
            setBuildMode(player, false);
        }
        teleportToLobby(player);
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0.0F);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.getInventory().setArmorContents(null);
        setPlayerGame(player, null);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
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
        for (Player p : getGamePlayers()) {
            player.hidePlayer(p);
            p.hidePlayer(player);
        }
        for (Player p : getLobbyPlayers()) {
            player.showPlayer(p);
            p.showPlayer(player);
        }
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
                .replace("{player}", player.getDisplayName())
                .replace("{points}", String.valueOf(pd.getData(PlayerDataType.POINTS)))
                .replace("{games_played}", String.valueOf(pd.getData(PlayerDataType.GAMES_PLAYED)))
                .replace("{victories}", String.valueOf(pd.getData(PlayerDataType.VICTORIES)))
                .replace("{correct_guesses}", String.valueOf(pd.getData(PlayerDataType.CORRECT_GUESSES)))
                .replace("{incorrect_guesses}", String.valueOf(pd.getData(PlayerDataType.INCORRECT_GUESSES)))
                .replace("{skips}", String.valueOf(pd.getData(PlayerDataType.SKIPS))));
        }
        lobbySidebarMap.get(player).updateLines(TextUtil.getByPlaceholders(newLines, player));
    }

    public static PlayerData getPlayerData(Player player) {
        if (!playerDataMap.containsKey(player)) {
            playerDataMap.put(player, new PlayerData(player));
        }
        return playerDataMap.get(player);
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

    public Location getLobbyLocation() {
        if (isLobbySet()) {
            return getConfigData().getLocation(ConfigData.LOBBY_LOCATION);
        }else {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }
    }

    public void teleportToLobby(Player player) {
        player.teleport(getLobbyLocation());
        if (!isLobbySet()) {
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

    private void placeholderApiHook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            isPlaceholderAPI = true;
            new PlaceholderAPI().register();
            getLogger().info("Hook into PlaceholderAPI support...");
        }
    }

    public List<Player> getLobbyPlayers() {
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Game game : games) {
            for (UUID uuid : game.getPlayers()) {
                playerList.remove(Bukkit.getPlayer(uuid));
            }
        }
        return playerList;
    }

    public List<Player> getGamePlayers() {
        List<Player> playerList = new ArrayList<>();
        for (Game game : games) {
            for (UUID uuid : game.getPlayers()) {
                playerList.add(Bukkit.getPlayer(uuid));
            }
            for (UUID uuid : game.getSpectators()) {
                playerList.add(Bukkit.getPlayer(uuid));
            }
        }
        return playerList;
    }

    public static void setBuildMode(Player player, Boolean bool) {
        playerBuildMap.put(player, bool);
    }

    public static boolean getBuildMode(Player player) {
        if (!player.hasPermission(PermissionsUtil.COMMAND_BUILD)) return false;
        return playerBuildMap.get(player);
    }

    public boolean isPlaceholderAPI() {
        return isPlaceholderAPI;
    }

    public static HashMap<Player, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public SQLite getSqLite() {
        return sqLite;
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

    public static DrawIt getInstance() {
        return instance;
    }

    public World getLobbyWorld() {
        return getLobbyLocation().getWorld();
    }

    public boolean isIn(Player player) {
        return player.getLocation().getWorld().equals(getLobbyWorld()) || getGamesWorlds().contains(player.getLocation().getWorld());
    }

}
