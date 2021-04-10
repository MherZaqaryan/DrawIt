package me.MrIronMan.drawit.data;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.data.DataManager;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameState;
import me.MrIronMan.drawit.game.utility.DrawerTool;
import me.MrIronMan.drawit.sql.PlayerData;
import me.MrIronMan.drawit.sql.PlayerDataType;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ConfigData extends DataManager {

    public ConfigData(Plugin plugin, String dir, String name) {
        super(plugin, dir, name);
        YamlConfiguration config = getConfig();

        config.options().header("DrawIt Mini-game by MrIronMan (Spigot: Mher)");
        config.addDefault("lobby-server", "lobby");
        config.addDefault("mysql.enabled", false);
        config.addDefault("mysql.host", "localhost");
        config.addDefault("mysql.port", "3306");
        config.addDefault("mysql.database", "drawit");
        config.addDefault("mysql.username", "root");
        config.addDefault("mysql.password", "pass");

        config.addDefault(CHAT_FORMAT, true);

        config.addDefault(COUNTDOWN_STARTING, 20);
        config.addDefault(COUNTDOWN_WORD_CHOOSE, 10);
        config.addDefault(COUNTDOWN_PER_ROUND, 70);
        config.addDefault(COUNTDOWN_AFTER_ROUND, 3);
        config.addDefault(COUNTDOWN_RESTART, 10);

        config.addDefault(SOUND_UNDER_5, "CLICK,1,1");
        config.addDefault(SOUND_DRAWER_WORD_CHOOSE, "SUCCESSFUL_HIT,1,0");
        config.addDefault(SOUND_SPRAY_CANVAS, "DIG_GRASS,0.5,0.5");
        config.addDefault(SOUND_COLOR_PICK, "SUCCESSFUL_HIT,1,1");
        config.addDefault(SOUND_LETTER_EXPLAIN, "CHICKEN_EGG_POP,1,2");
        config.addDefault(SOUND_LESS_TIME, "BLOCK_NOTE_BASS,1,1");
        config.addDefault(SOUND_WORD_GUESS, "ANVIL_LAND,1,1");
        config.addDefault(SOUND_GAME_OVER, "WITHER_DEATH,1,1");

        config.addDefault(LOBBY_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(LOBBY_ITEMS+".game-selector", "CHEST", false, 0, "drawit menu games");
            saveItem(LOBBY_ITEMS+".return-to-lobby", "SLIME_BALL", false, 8, "drawit leave");
        }

        config.addDefault(WAITING_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(WAITING_ITEMS+".leave", "SLIME_BALL", false, 8, "drawit leave");
        }

        config.addDefault(GAMES_MENU_SETTINGS_SIZE, 45);
        config.addDefault(GAMES_MENU_SETTINGS_SLOTS, new Integer[]{28, 29, 30, 31, 32, 33, 34});
        config.addDefault(GAMES_MENU_SETTINGS_SHOW_PLAYINGS, false);

        saveItem(GAMES_MENU_SETTINGS_WAITING, "STAINED_CLAY:5", false);
        saveItem(GAMES_MENU_SETTINGS_STARTING, "STAINED_CLAY:4", false);
        saveItem(GAMES_MENU_SETTINGS_PLAYING, "STAINED_CLAY:14", false);
        saveItem(GAMES_MENU_SETTINGS_WAITING_SLOTS, "STAINED_GLASS_PANE:7", false);

        config.addDefault(GAMES_MENU_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(GAMES_MENU_ITEMS+".spectate-game", "MAGMA_CREAM", false, 12, "drawit menu spectate");
            saveItem(GAMES_MENU_ITEMS+".quick-join", "MINECART", false, 14, "drawit quickjoin");
        }

        config.addDefault(SPECTATE_MENU_SIZE, 45);
        config.addDefault(SPECTATE_MENU_SLOTS, new Integer[]{10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34});
        saveItem(SPECTATE_MENU_GAME, "MAGMA_CREAM", false);

        config.addDefault(SPECTATE_MENU_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(SPECTATE_MENU_ITEMS+".back", "ARROW", false, 36, "drawit menu games");
        }

        config.addDefault(SPECTATE_ITEMS, new String[]{});

        if (isFirstTime()) {
            saveItem(SPECTATE_ITEMS+".teleporter", "COMPASS", false, 0, "di spectate teleporter");
            saveItem(SPECTATE_ITEMS+".leave", "ARROW", false, 8, "di spectate leave");
        }

        saveItem("drawer-tools.thin-brush", "WOOD_SWORD", false, 0);
        saveItem("drawer-tools.thick-brush", "DIAMOND_SWORD", false, 1);
        saveItem("drawer-tools.spray-canvas", "SHEARS", false, 2);
        saveItem("drawer-tools.fill-can", "BUCKET", false, 3);
        saveItem("drawer-tools.burn-canvas", "BLAZE_POWDER", false, 8);

        config.addDefault("color-picker", new String[]{"" +
                "0; &7Orange; WOOL; 1",
                "1; &7Magenta; WOOL; 2",
                "2; &7Light Blue; WOOL; 3",
                "3; &7Yellow; WOOL; 4",
                "4; &7Lime; WOOL; 5",
                "5; &7Cyan; WOOL; 9",
                "6; &7Purple; WOOL; 10",
                "7; &7Blue; WOOL; 11",
                "8; &7Green; WOOL; 13",
                "9; &7Red; WOOL; 14",
                "10; &7Pastel Orange; STAINED_CLAY; 1",
                "11; &7Pastel Magenta; STAINED_CLAY; 2",
                "12; &7Pastel Light Blue; STAINED_CLAY; 3",
                "13; &7Pastel Yellow; STAINED_CLAY; 4",
                "14; &7Pastel Lime; STAINED_CLAY; 5",
                "15; &7Pastel Cyan; STAINED_CLAY; 9",
                "16; &7Pastel Purple; STAINED_CLAY; 10",
                "17; &7Pastel Blue; STAINED_CLAY; 11",
                "18; &7Pastel Green; STAINED_CLAY; 13",
                "19; &7Pastel Red; STAINED_CLAY; 14",
                "20; &7Gold; GOLD_BLOCK; 0",
                "21; &7Diamond; DIAMOND_BLOCK; 0",
                "22; &7Redstone; REDSTONE_BLOCK; 0",
                "23; &7Sponge; SPONGE; 0",
                "27; &7Pink; WOOL; 6",
                "29; &7Emerald; EMERALD_BLOCK; 0",
                "31; &7Black; WOOL; 15",
                "32; &7Brown; WOOL; 12",
                "33; &7Gray; WOOL; 7",
                "34; &7Light Gray; WOOL; 8",
                "35; &7White; WOOL; 0",
                "36; &7Pastel Pink; STAINED_CLAY; 6",
                "38; &7Iron; IRON_BLOCK; 0",
                "40; &7Pastel Black; STAINED_CLAY; 15",
                "41; &7Pastel Brown; STAINED_CLAY; 12",
                "42; &7Pastel Gray; STAINED_CLAY; 7",
                "43; &7Pastel Light Gray; STAINED_CLAY; 8",
                "44; &7Pastel White; STAINED_CLAY; 0"
        });

        config.addDefault(STATS_POINTS_DRAWER_PER_RIGHT_WORD, 2);

        if (isFirstTime()) {
            config.addDefault(STATS_POINTS_GUESSER+".1", 10);
            config.addDefault(STATS_POINTS_GUESSER+".2", 8);
            config.addDefault(STATS_POINTS_GUESSER+".3", 6);
            config.addDefault(STATS_POINTS_GUESSER+".4", 4);
        }

        config.addDefault(STATS_POINTS_GUESSER_OTHER, 2);

        config.addDefault(PLAYER_LEVELING+".0-250.format", "&7Finger Painter");
        config.addDefault(PLAYER_LEVELING+".250-1000.format", "&6Crayon Set");
        config.addDefault(PLAYER_LEVELING+".1000-2500.format", "&dPaint Brush");
        config.addDefault(PLAYER_LEVELING+".2500-5000.format", "&bRaphael");
        config.addDefault(PLAYER_LEVELING+".5000-7500.format", "&5Kandinsky");
        config.addDefault(PLAYER_LEVELING+".7500-12500.format", "&aRembrandt");
        config.addDefault(PLAYER_LEVELING+".12500-20000.format", "&cManet");
        config.addDefault(PLAYER_LEVELING+".20000-40000.format", "&9Warhol");
        config.addDefault(PLAYER_LEVELING+".40000-70000.format", "&5Dali");
        config.addDefault(PLAYER_LEVELING+".70000-100000.format", "&6&lMonet");
        config.addDefault(PLAYER_LEVELING+".100000-150000.format", "&b&lMondrian");
        config.addDefault(PLAYER_LEVELING+".150000-200000.format", "&3&lLichenstein");
        config.addDefault(PLAYER_LEVELING+".200000-250000.format", "&e&lMichelangelo");
        config.addDefault(PLAYER_LEVELING+".250000-300000.format", "&e&lLeonardo da Vinci");
        config.addDefault(PLAYER_LEVELING+".other.format", "&a&lVincent van Gogh");
        config.options().copyDefaults(true);
        save();
    }

    public void saveItem(String path, String material, boolean enchanted, int slot, String cmd) {
        saveItem(path, material, enchanted, slot);
        getConfig().addDefault(path+".command", cmd);
    }

    public void saveItem(String path, String material, boolean enchanted, int slot) {
        saveItem(path, material, enchanted);
        getConfig().addDefault(path+".slot", slot);
    }

    public void saveItem(String path, String material, boolean enchanted) {
        getConfig().addDefault(path+".material", material);
        getConfig().addDefault(path+".enchanted", enchanted);
    }

    public ItemStack getItem(String path) {
        path = path+".";
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(XMaterial.matchXMaterial(getString(path + "material")).get().parseItem()));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (getBoolean(path+"enchanted")) {
            itemMeta.addEnchant(Enchantment.LUCK, 0, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (DrawIt.getMessagesData().getString(path+"display-name") == null) {
            DrawIt.getMessagesData().set(path+"display-name", "&7Display name not set");
        }
        if (DrawIt.getMessagesData().getStringList(path+"lore").isEmpty()) {
            DrawIt.getMessagesData().set(path+"lore", new String[]{"","&7Lore not set"});
        }
        itemMeta.setDisplayName(TextUtil.colorize(DrawIt.getMessagesData().getString(path+"display-name")));
        itemMeta.setLore(TextUtil.colorize(DrawIt.getMessagesData().getStringList(path+"lore")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getLobbyItem(String subPath) {
        String path = "lobby-items."+subPath;
        ItemStack itemStack = getItem(path);
        NBTItem nbti = new NBTItem(itemStack);
        nbti.setString("name", "lobby-item");
        nbti.setInteger("slot", getInt(path+".slot"));
        nbti.setString("command", getString(path+".command"));
        return nbti.getItem();
    }

    public ItemStack getDrawerTool(DrawerTool drawerTool) {
        String path = "drawer-tools."+drawerTool.getPath();
        ItemStack itemStack = getItem(path);
        NBTItem nbti = new NBTItem(itemStack);
        nbti.setString("name", "drawer-tool");
        nbti.setString("type", drawerTool.getPath());
        nbti.setBoolean("isPickable", !drawerTool.equals(DrawerTool.BURN_CANVAS));
        nbti.setInteger("slot", getInt(path+".slot"));
        return nbti.getItem();
    }

    public List<ItemStack> getGameMenuItems() {
        List<ItemStack> items = new ArrayList<>();
        if (getConfig().getConfigurationSection(GAMES_MENU_ITEMS).getKeys(false).isEmpty()) return Collections.emptyList();
        for (String s : getConfig().getConfigurationSection(GAMES_MENU_ITEMS).getKeys(false)) {
            ItemStack itemStack = getItem(GAMES_MENU_ITEMS+"."+s);
            NBTItem nbti = new NBTItem(itemStack);
            nbti.setString("name", "game-menu");
            nbti.setInteger("slot", getInt(GAMES_MENU_ITEMS+"."+s+".slot"));
            nbti.setString("command", getString(GAMES_MENU_ITEMS+"."+s+".command"));
            items.add(nbti.getItem());
        }
        return items;
    }

    public List<ItemStack> getSpectatorItems() {
        List<ItemStack> items = new ArrayList<>();
        if (getConfig().getConfigurationSection(SPECTATE_ITEMS).getKeys(false).isEmpty()) return Collections.emptyList();
        for (String s : getConfig().getConfigurationSection(SPECTATE_ITEMS).getKeys(false)) {
            ItemStack itemStack = getItem(SPECTATE_ITEMS+"."+s);
            NBTItem nbti = new NBTItem(itemStack);
            nbti.setString("name", "spectate-item");
            nbti.setInteger("slot", getInt(SPECTATE_ITEMS+"."+s+".slot"));
            nbti.setString("command", getString(SPECTATE_ITEMS+"."+s+".command"));
            items.add(nbti.getItem());
        }
        return items;
    }

    public List<ItemStack> getWaitingItems() {
        List<ItemStack> items = new ArrayList<>();
        if (getConfig().getConfigurationSection(WAITING_ITEMS).getKeys(false).isEmpty()) return Collections.emptyList();
        for (String s : getConfig().getConfigurationSection(WAITING_ITEMS).getKeys(false)) {
            ItemStack itemStack = getItem(WAITING_ITEMS+"."+s);
            NBTItem nbti = new NBTItem(itemStack);
            nbti.setString("name", "waiting-item");
            nbti.setInteger("slot", getInt(WAITING_ITEMS+"."+s+".slot"));
            nbti.setString("command", getString(WAITING_ITEMS+"."+s+".command"));
            items.add(nbti.getItem());
        }
        return items;
    }

    public ItemStack getItem(String path, Game game) {
        ItemStack item = DrawIt.getConfigData().getItem(path);
        ItemMeta itemMeta = item.getItemMeta();
        if (game != null) {
            List<String> newLore = new ArrayList<>();
            for (String s : itemMeta.getLore()) {
                newLore.add(s
                        .replace("{in}", String.valueOf(game.getPlayers().size()))
                        .replace("{max}", String.valueOf(game.getMaxPlayers()))
                        .replace("{state}", GameState.getName(game.getGameState())));
            }
            itemMeta.setDisplayName(itemMeta.getDisplayName().replace("{game}", game.getDisplayName()));
            itemMeta.setLore(TextUtil.colorize(newLore));
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public List<ItemStack> getSpectateMenuItems() {
        List<ItemStack> items = new ArrayList<>();
        for (String s : getConfig().getConfigurationSection(SPECTATE_MENU_ITEMS).getKeys(false)) {
            ItemStack itemStack = getItem(SPECTATE_MENU_ITEMS+"."+s);
            NBTItem nbti = new NBTItem(itemStack);
            nbti.setString("name", "spectate-menu");
            nbti.setInteger("slot", getInt(SPECTATE_MENU_ITEMS+"."+s+".slot"));
            nbti.setString("command", getString(SPECTATE_MENU_ITEMS+"."+s+".command"));
            items.add(nbti.getItem());
        }
        return items;
    }

    public List<ItemStack> getLobbyItems() {
        List<ItemStack> items = new ArrayList<>();
        for (String path : getConfig().getConfigurationSection("lobby-items").getKeys(false)) {
            items.add(getLobbyItem(path));
        }
        return items;
    }

    public List<String> getMySqlInfo() {
        List<String> mysqlInfo = new ArrayList<>();
        mysqlInfo.add(getString("mysql.host"));
        mysqlInfo.add(getString("mysql.port"));
        mysqlInfo.add(getString("mysql.database"));
        mysqlInfo.add(getString("mysql.username"));
        mysqlInfo.add(getString("mysql.password"));
        return mysqlInfo;
    }

    public boolean isMySql() {
        return getBoolean("mysql.enabled");
    }

    public String getPointFormat(Player player) {
        PlayerData playerData = DrawIt.getPlayerData(player);
        int points = playerData.getData(PlayerDataType.POINTS);
        for (String st : getConfig().getConfigurationSection(PLAYER_LEVELING).getKeys(false)) {
            if (st.split("-").length == 2) {
                int min = Integer.parseInt(st.split("-")[0]);
                int max = Integer.parseInt(st.split("-")[1]);
                if (points >= min && points <= max) {
                    return TextUtil.colorize(getConfig().getString(PLAYER_LEVELING+"."+st+".format"));
                }
            }else if (st.equals("other")) {
                return TextUtil.colorize(getConfig().getString(PLAYER_LEVELING+".other.format"));
            }
        }
        return null;
    }

    public static String CHAT_FORMAT = "use-chat-format";

    public static String GAMES_MENU_SETTINGS_SIZE = "games-menu.settings.size";
    public static String GAMES_MENU_SETTINGS_SLOTS = "games-menu.settings.slots";
    public static String GAMES_MENU_SETTINGS_WAITING = "games-menu.settings.waiting";
    public static String GAMES_MENU_SETTINGS_STARTING = "games-menu.settings.starting";
    public static String GAMES_MENU_SETTINGS_PLAYING = "games-menu.settings.playing";
    public static String GAMES_MENU_SETTINGS_WAITING_SLOTS = "games-menu.settings.waiting-slots";
    public static String GAMES_MENU_SETTINGS_SHOW_PLAYINGS = "games-menu.settings.show-playings";
    public static String GAMES_MENU_ITEMS = "games-menu.items";

    public static String SPECTATE_MENU_SIZE = "spectate-menu.settings.size";
    public static String SPECTATE_MENU_SLOTS = "spectate-menu.settings.slots";
    public static String SPECTATE_MENU_GAME = "spectate-menu.settings.game";
    public static String SPECTATE_MENU_ITEMS = "spectate-menu.items";

    public static String LOBBY_ITEMS = "lobby-items";
    public static String WAITING_ITEMS = "waiting-items";
    public static String SPECTATE_ITEMS = "spectate-items";

    public static String COLOR_PICKER = "color-picker";
    public static String LOBBY_LOCATION = "lobby-location";
    public static String LOBBY_SERVER = "lobby-server";

    public static String COUNTDOWN_STARTING = "countdowns.starting";
    public static String COUNTDOWN_WORD_CHOOSE = "countdowns.word-choose";
    public static String COUNTDOWN_PER_ROUND = "countdowns.per-round";
    public static String COUNTDOWN_AFTER_ROUND = "countdowns.after-round";
    public static String COUNTDOWN_RESTART = "countdowns.restart";

    public static String SOUND_UNDER_5 = "sounds.starting-under-5";
    public static String SOUND_DRAWER_WORD_CHOOSE = "sounds.word-choose";
    public static String SOUND_SPRAY_CANVAS = "sounds.spray-canvas";
    public static String SOUND_LETTER_EXPLAIN = "sounds.letter-explain";
    public static String SOUND_WORD_GUESS = "sounds.word-guess";
    public static String SOUND_GAME_OVER = "sounds.game-over";
    public static String SOUND_COLOR_PICK = "sounds.color-pick";
    public static String SOUND_LESS_TIME = "sounds.less-time";

    public static String STATS_POINTS_DRAWER_PER_RIGHT_WORD = "stats.points.drawer.per-right-word";
    public static String STATS_POINTS_GUESSER = "stats.points.guesser";
    public static String STATS_POINTS_GUESSER_OTHER = "stats.points.guesser.other";

    public static String PLAYER_LEVELING = "player-leveling";

}
