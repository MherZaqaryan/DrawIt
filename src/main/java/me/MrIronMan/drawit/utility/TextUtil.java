package me.MrIronMan.drawit.utility;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.MessagesData;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextUtil {

    public static String colorize(String s) {
        if (s == null) return "";
        return ChatColor.translateAlternateColorCodes('&', s.replace("{prefix}", prefix()));
    }

    public static List<String> colorize(List<String> s) {
        List<String> newList = new ArrayList<>();
        for (String str : s) {
            newList.add(colorize(str));
        }
        return newList;
    }

    public static String[] colorize(String[] lines) {
        return colorize(Arrays.asList(lines.clone())).toArray(new String[0]);
    }

    public static String prefix() {
        return DrawIt.getMessagesData().getString(MessagesData.PREFIX);
    }

}
