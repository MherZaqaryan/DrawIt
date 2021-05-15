package me.MrIronMan.drawit.utility;

import com.cryptomorin.xseries.XMaterial;
import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.data.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class OtherUtils {

    public static Set<Block> getBlocks(Location loc1, Location loc2) {
        Set<Block> blocks = new HashSet<>();

        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));
        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));
        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public static Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }

    public static HashMap<Integer, ItemStack> getColorPicker() {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        for (String s : DrawIt.getConfigData().getStringList(ConfigData.COLOR_PICKER+".items")) {
            String[] args = s.split("; ");
            if (args.length == 3) {
                ItemStack itemStack = Objects.requireNonNull(XMaterial.matchXMaterial(args[2].toUpperCase()).get().parseItem());
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(TextUtil.colorize(args[1]));
                itemStack.setItemMeta(itemMeta);
                items.put(Integer.parseInt(args[0]), itemStack);
            }
        }
        return items;
    }

    public static List<String> getWords(int n) {
        List<String> wordsList = new ArrayList<>(DrawIt.getWordsData().getWords());
        List<String> newWordsList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int rand = random.nextInt(wordsList.size());
            String word = wordsList.get(rand);
            newWordsList.add(word);
            wordsList.remove(word);
        }
        return newWordsList;
    }

    public static String formatTime(int timer) {
        String s = ":";
        int minutes = (int) (Math.floor((float) timer / 60));
        int seconds = timer % 60;
        if (minutes < 10 && seconds < 10) {
            return "0" + minutes + s + "0" + seconds;
        }
        else if (minutes < 10) {
            return "0" + minutes + s + seconds;
        }
        else if (seconds < 10) {
            return minutes + s + "0" + seconds;
        }
        else {
            return minutes + s + seconds;
        }
    }

    public static Location readLocation(String loc) {
        if (loc == null) return null;
        String[] args = loc.split(", ");
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);
        if (args.length == 4) {
            World world = Bukkit.getWorld(args[3]);
            return new Location(world, x, y, z);
        }
        else if (args.length == 6) {
            float yaw = Float.parseFloat(args[3]);
            float pitch = Float.parseFloat(args[4]);
            World world = Bukkit.getWorld(args[5]);
            return new Location(world, x, y, z, yaw, pitch);
        }
        else {
            DrawIt.getInstance().getLogger().warning("Cannot load location with meta data: " + loc);
            return null;
        }
    }

    public static String writeLocation(Location loc, boolean advanced) {
        if (loc == null) return "N/A";
        String regex = ", ";
        DecimalFormat df = new DecimalFormat("##.#");
        String x = df.format(loc.getX());
        String y = df.format(loc.getY());
        String z = df.format(loc.getZ());
        String yaw = df.format(loc.getYaw());
        String pitch = df.format(loc.getPitch());
        String world = loc.getWorld().getName();
        return advanced ? x + regex + y + regex + z + regex + yaw + regex + pitch + regex + world
                : x + regex + y + regex + z + regex + world;
    }

    public static void returnLobby(Player player) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(DrawIt.getInstance(), "BungeeCord");
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(ConfigData.LOBBY_SERVER);
        }
        catch (IOException ex) {
            DrawIt.getInstance().getLogger().warning("Cant connect to lobby server!");
        }
        player.sendPluginMessage(DrawIt.getInstance(), "BungeeCord", b.toByteArray());
    }


    public static boolean isOnline(String p) {
        boolean online = false;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getName().equalsIgnoreCase(p)) continue;
            online = true;
            break;
        }
        return online;
    }

    public static int getWordsCount() {
        return DrawIt.getWordsData().getStringList("words").size();
    }

}
