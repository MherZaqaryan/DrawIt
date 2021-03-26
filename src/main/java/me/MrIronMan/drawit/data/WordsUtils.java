package me.MrIronMan.drawit.data;

import me.MrIronMan.drawit.DrawIt;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class WordsUtils {

    public static FileConfiguration getData() {
        return DrawIt.getInstance().getWordsData().getConfig();
    }

    public static List<String> WORDS_LIST = getData().getStringList("Words");

    public void sort() {
        List<String> words = new ArrayList<>(WordsUtils.WORDS_LIST);
        Set<String> sortedSet = new HashSet<>(words);
        List<String> sortedList = new ArrayList<>(sortedSet);
        sortedList.sort(Comparator.naturalOrder());
        WordsUtils.getData().set("Words", sortedList);
        DrawIt.getInstance().getWordsData().saveConfig();
    }

}
