package me.MrIronMan.drawit.data;

import com.cryptomorin.xseries.XMaterial;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ConfigUtils {

    public static FileConfiguration getData() {
        return DrawIt.getInstance().getConfigData().getConfig();
    }

    public static List<String> COLOR_PICKER = getData().getStringList("color-picker");

    public static String LOBBY_LOCATION = getData().getString("lobby-location");

    public static Boolean MYSQL_ENABLED = getData().getBoolean("mysql.enable");
    public static String MYSQL_HOST = getData().getString("mysql.host");
    public static String MYSQL_PORT = getData().getString("mysql.port");
    public static String MYSQL_DATABASE = getData().getString("mysql.database");
    public static String MYSQL_USERNAME = getData().getString("mysql.username");
    public static String MYSQL_PASSWORD = getData().getString("mysql.password");

    public static String LOBBY_SERVER = getData().getString("lobby-server");

    // Lobby Items

    public static ItemStack getLobbyItem(String subPath) {
        String path = "lobby-items." + subPath + ".";
        ItemStack itemStack = new ItemStack(XMaterial.matchXMaterial(getData().getString(path+"material")).get().parseItem());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(TextUtil.colorize(MessagesUtils.getLobbyItemName(subPath)));
        itemMeta.setLore(TextUtil.colorize(MessagesUtils.getLobbyItemLore(subPath)));
        if (getData().getBoolean(path+"enchanted")) {
            itemMeta.addEnchant(Enchantment.LUCK, 0, true);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static int getLobbyItemSlot(String subPath) {
        String path = "lobby-items." + subPath + ".slot";
        return getData().getInt(path);
    }

    public static String getLobbyItemCommand(String subPath) {
        String path = "lobby-items." + subPath + ".command";
        return getData().getString(path);
    }

    public static List<Map.Entry<Integer, ItemStack>> getLobbyItems() {
        HashMap<Integer, ItemStack> lobbyItemsMap = new HashMap<>();
        for (String s : getData().getConfigurationSection("lobby-items").getKeys(false)) {
            lobbyItemsMap.put(getLobbyItemSlot(s), getLobbyItem(s));
        }
        return new ArrayList<>(lobbyItemsMap.entrySet());
    }

    public static List<Map.Entry<ItemStack, String>> getLobbyItemsPath() {
        HashMap<ItemStack, String> lobbyItemsMap = new HashMap<>();
        for (String s : getData().getConfigurationSection("lobby-items").getKeys(false)) {
            lobbyItemsMap.put(getLobbyItem(s), s);
        }
        return new ArrayList<>(lobbyItemsMap.entrySet());
    }

    // Drawer Tools Material & Slot

    public static String THIN_BRUSH_MAT = getData().getString("drawer-tools.thin-brush.material");
    public static int THIN_BRUSH_SLOT = getData().getInt("drawer-tools.thin-brush.slot");

    public static String THICK_BRUSH_MAT = getData().getString("drawer-tools.thick-brush.material");
    public static int THICK_BRUSH_SLOT = getData().getInt("drawer-tools.thick-brush.slot");

    public static String SPRAY_CAN_MAT = getData().getString("drawer-tools.spray-canvas.material");
    public static int SPRAY_CAN_SLOT = getData().getInt("drawer-tools.spray-canvas.slot");

    public static String FILL_CAN_MAT = getData().getString("drawer-tools.fill-can.material");
    public static int FILL_CAN_SLOT = getData().getInt("drawer-tools.fill-can.slot");

    public static String BURN_CANVAS_MAT = getData().getString("drawer-tools.burn-canvas.material");
    public static int BURN_CANVAS_SLOT = getData().getInt("drawer-tools.burn-canvas.slot");


}
