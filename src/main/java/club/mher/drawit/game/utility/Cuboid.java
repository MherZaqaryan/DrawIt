package club.mher.drawit.game.utility;

import club.mher.drawit.utility.BlockUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
        return isIn(loc.getBlock());
    }

    public boolean isIn(Block block) {
        return getBlocks().contains(block);
    }

    public void burn(ItemStack itemStack) {
        for (Block block : getBlocks()) {
            BlockUtil.setBlock(block, itemStack);
        }
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public boolean isClean(ItemStack itemStack) {
        boolean isClean = true;
        for (Block b : getBlocks()) {
            if (!(b.getType().equals(itemStack.getType()) && b.getData() == itemStack.getDurability())) {
                isClean = false;
                break;
            }
        }
        return isClean;
    }

}
