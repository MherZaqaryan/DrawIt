package me.MrIronMan.drawit.game.utility;

import java.util.*;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class Cuboid {

    private Location loc1, loc2;

    public Cuboid(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();
        int topBlockX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int bottomBlockX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int topBlockY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int bottomBlockY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int topBlockZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        int bottomBlockZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
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

    public boolean isIn(Location loc) {
        return getBlocks().contains(loc.getBlock());
    }

    public boolean isIn(Block block) {
        return getBlocks().contains(block);
    }

    public void burn(ItemStack itemStack) {
        for (Block block : getBlocks()) {
            byte data = (byte) itemStack.getDurability();
            block.setTypeIdAndData(itemStack.getTypeId(), data, false);
        }
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public boolean isClean() {
        boolean isClean = true;
        for (Block b : getBlocks()) {
            if (!(b.getType().equals(Material.WOOL) && b.getData() == (byte) 0)) {
                isClean = false;
                break;
            }
        }
        return isClean;
    }

}
