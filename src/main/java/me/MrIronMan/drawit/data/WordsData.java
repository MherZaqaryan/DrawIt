package me.MrIronMan.drawit.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class WordsData extends DataManager{

    public WordsData(Plugin plugin, String dir, String name) {
        super(plugin, dir, name);
        YamlConfiguration words = getConfig();
        words.addDefault("words", new String[]{
                "Angry Birds",
                "Apple",
                "Backpack",
                "Barbie",
                "Bat",
                "Battery",
                "Book",
                "Bread",
                "Bucket",
                "Cannon",
                "Coin",
                "Cookie",
                "Crocodile",
                "Curtains",
                "Desert",
                "Diamond",
                "Drum",
                "Earphones",
                "Eggs",
                "Emerald",
                "Feather",
                "Flag",
                "Forest",
                "Furnace",
                "Galaxy",
                "Garlic",
                "Glove",
                "Goldfish",
                "Grass",
                "Hand",
                "Handcuffs",
                "Headset",
                "Ice",
                "Instagram",
                "Iphone",
                "Iron Man",
                "Jail",
                "Jupiter",
                "Key",
                "Laptop",
                "Lemon",
                "Lips",
                "Melon",
                "Metal",
                "Mojang",
                "Monster",
                "Mosquito",
                "Moustache",
                "Muscle",
                "Mushroom",
                "Octopus",
                "Oreo",
                "Palm Tree",
                "Panda",
                "Paper",
                "Pear",
                "Pearl",
                "Penguin",
                "Phone",
                "Pig",
                "Polaroid",
                "Pumpkin",
                "Queen",
                "Sandcastle",
                "Shark",
                "Shirt",
                "Sink",
                "Sky",
                "Snapchat",
                "Snow",
                "Squid",
                "Staircase",
                "Star",
                "Target",
                "Titanic",
                "Tnt",
                "Toilet",
                "Tree",
                "Unicycle",
                "Voice",
                "Voldemort",
                "Water",
                "Wolf",
                "Worm",
                "Zombie"
        });
        save();
    }

    public List<String> getWords() {
        return getStringList("words");
    }

//    public void sort() {
//        List<String> words = new ArrayList<>(getWords());
//        Set<String> sortedSet = new HashSet<>(words);
//        List<String> sortedList = new ArrayList<>(sortedSet);
//        sortedList.sort(Comparator.naturalOrder());
//        set("words", sortedList);
//        save();
//    }

}
