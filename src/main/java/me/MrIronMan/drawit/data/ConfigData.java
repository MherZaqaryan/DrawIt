package me.MrIronMan.drawit.data;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.game.utility.DrawerTool;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ConfigData extends DataManager{

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

        if (isFirstTime()) {
            config.addDefault("lobby-items.game-selector.material", "CHEST");
            config.addDefault("lobby-items.game-selector.slot", 0);
            config.addDefault("lobby-items.game-selector.enchanted", false);
            config.addDefault("lobby-items.game-selector.command", "drawit gui");

            config.addDefault("lobby-items.return-to-lobby.material", "ARROW");
            config.addDefault("lobby-items.return-to-lobby.slot", 8);
            config.addDefault("lobby-items.return-to-lobby.enchanted", false);
            config.addDefault("lobby-items.return-to-lobby.command", "drawit leave");

            config.options().copyDefaults(true);
            save();
        }

        config.addDefault("drawer-tools.thin-brush.material", "WOOD_SWORD");
        config.addDefault("drawer-tools.thin-brush.enchanted", false);
        config.addDefault("drawer-tools.thin-brush.slot", 0);

        config.addDefault("drawer-tools.thick-brush.material", "DIAMOND_SWORD");
        config.addDefault("drawer-tools.thick-brush.enchanted", false);
        config.addDefault("drawer-tools.thick-brush.slot", 1);

        config.addDefault("drawer-tools.spray-canvas.material", "SHEARS");
        config.addDefault("drawer-tools.spray-canvas.enchanted", false);
        config.addDefault("drawer-tools.spray-canvas.slot", 2);

        config.addDefault("drawer-tools.fill-can.material", "BUCKET");
        config.addDefault("drawer-tools.fill-can.enchanted", false);
        config.addDefault("drawer-tools.fill-can.slot", 3);

        config.addDefault("drawer-tools.burn-canvas.material", "BLAZE_POWDER");
        config.addDefault("drawer-tools.burn-canvas.enchanted", false);
        config.addDefault("drawer-tools.burn-canvas.slot", 8);

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

        config.addDefault("stats.tokens.per-right-guess", 2);
        config.addDefault("stats.tokens.victory", 40);
        config.addDefault("stats.drawer-points.per-right-word", 2);
        config.addDefault("stats.guesser-points.1", 10);
        config.addDefault("stats.guesser-points.2", 8);
        config.addDefault("stats.guesser-points.3", 6);
        config.addDefault("stats.guesser-points.4", 4);
        config.addDefault("stats.guesser-points.other", 10);

        config.options().copyDefaults(true);
        save();
    }

    public ItemStack getLobbyItem(String subPath) {
        String path = "lobby-items."+subPath+".";
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
        NBTItem nbti = new NBTItem(itemStack);
        nbti.setString("name", "lobby-item");
        nbti.setInteger("slot", getInt(path+"slot"));
        nbti.setString("command", getString(path+"command"));
        return nbti.getItem();
    }

    public ItemStack getDrawerTool(DrawerTool drawerTool) {
        String path = "drawer-tools."+drawerTool.getPath()+".";
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(XMaterial.matchXMaterial(getString(path + "material")).get().parseItem()));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (getBoolean(path)) {
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
        NBTItem nbti = new NBTItem(itemStack);
        nbti.setString("name", "drawer-tool");
        nbti.setString("type", drawerTool.getPath());
        nbti.setBoolean("isPickable", !drawerTool.equals(DrawerTool.BURN_CANVAS));
        nbti.setInteger("slot", getInt(path+"slot"));
        return nbti.getItem();
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

    public static String COLOR_PICKER = "color-picker";
    public static String LOBBY_LOCATION = "lobby-location";
    public static String LOBBY_SERVER = "lobby-server";

}
