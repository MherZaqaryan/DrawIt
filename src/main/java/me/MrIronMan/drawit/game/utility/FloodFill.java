package me.MrIronMan.drawit.game.utility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class FloodFill {

    private Set<Block> blockList;

    private Material material;
    private int data;
    private Cuboid board;
    private Block block;

    public FloodFill(Cuboid board, Block block, Material material, int data) {
        this.blockList = new HashSet<>();
        this.material = material;
        this.data = data;
        this.board = board;
        this.block = block;
        fillGrid(block);
    }

    public void fillGrid(Block b) {
        if (blockList.contains(b) || !board.isIn(block)) return;
        if (b.getType() == block.getType() && b.getData() == block.getData() && board.isIn(b)) {
            Location loc = b.getLocation();
            blockList.add(b);
            if (loc.clone().add(1, 0, 0).getBlock().getType().equals(Material.AIR) ||
            (loc.clone().subtract(1, 0, 0).getBlock().getType().equals(Material.AIR))) {
                fillGrid(loc.clone().add(0, 0, 1).getBlock());
                fillGrid(loc.clone().subtract(0, 0, 1).getBlock());
            }else {
                fillGrid(loc.clone().add(1, 0, 0).getBlock());
                fillGrid(loc.clone().subtract(1, 0, 0).getBlock());
            }
            fillGrid(loc.clone().add(0, 1, 0).getBlock());
            fillGrid(loc.clone().subtract(0, 1, 0).getBlock());
        }
    }

    public void setBlocks() {
        for (Block b : blockList) {
            if (board.isIn(b)) {
                b.setTypeIdAndData(material.getId(), (byte) data, true);
            }
        }
    }

}