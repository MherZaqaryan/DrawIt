package me.MrIronMan.drawit.utility;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.MessagesData;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextUtil {

    public static String colorize(String s) {
        return s == null ? " " : ChatColor.translateAlternateColorCodes('&', s.replace("{prefix}", prefix()));
    }

    public static String strip(String s) {
        return s == null ? " " : ChatColor.stripColor(s);
    }

    public static List<String> colorize(List<String> s) {
        List<String> newList = new ArrayList<>();
        s.forEach(st -> newList.add(colorize(st)));
        return newList;
    }

    public static String[] colorize(String[] lines) {
        return colorize(Arrays.asList(lines.clone())).toArray(new String[0]);
    }

    public static String prefix() {
        return DrawIt.getMessagesData().getString(MessagesData.PREFIX);
    }

    public static String getByPlaceholders(String text, Player player) {
        if (player == null || !DrawIt.getInstance().isPlaceholderAPI()) return colorize(text);
        return colorize(PlaceholderAPI.setPlaceholders(player, text));
    }

    public static List<String> getByPlaceholders(List<String> list, Player player) {
        List<String> changedList = new ArrayList<>();
        list.forEach(s -> changedList.add(getByPlaceholders(s, player)));
        return changedList;
    }

    public static String[] getByPlaceholders(String[] array, Player player) {
        return getByPlaceholders(Arrays.asList(array), player).toArray(new String[0]);
    }

}
