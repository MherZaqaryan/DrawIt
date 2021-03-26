package me.MrIronMan.drawit.game.utility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class FloodFill {

    private Set<Block> blockList;

    private Location location;
    private Material material;
    private int data;
    private Cuboid board;
    private Block block;

    public FloodFill(Cuboid board, Location location, Material material, int data) {
        this.blockList = new HashSet<>();
        this.location = location;
        this.material = material;
        this.data = data;
        this.board = board;
        this.block = location.getBlock();
        fillGrid(location);
    }

    public void fillGrid(Location loc) {
        if (blockList.contains(loc.getBlock()) || !board.isIn(block)) return;
        if (loc.getBlock().getType() == block.getType() && loc.getBlock().getData() == block.getData()) {
            blockList.add(loc.getBlock());
            if (loc.clone().add(1, 0, 0).getBlock().getType().equals(Material.AIR) ||
            (loc.clone().subtract(1, 0, 0).getBlock().getType().equals(Material.AIR))) {
                fillGrid(loc.clone().add(0, 0, 1));
                fillGrid(loc.clone().subtract(0, 0, 1));
            }else {
                fillGrid(loc.clone().add(1, 0, 0));
                fillGrid(loc.clone().subtract(1, 0, 0));
            }
            fillGrid(loc.clone().add(0, 1, 0));
            fillGrid(loc.clone().subtract(0, 1, 0));
        }
    }

    public void setBlocks() {
        for (Block b : blockList) {
            b.setTypeIdAndData(material.getId(), (byte) data, true);
        }
    }

}