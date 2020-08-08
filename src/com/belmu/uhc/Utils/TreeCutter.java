package com.belmu.uhc.Utils;

import com.belmu.uhc.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.material.Tree;

import java.util.*;

public class TreeCutter {

    private List<String> comparisonBlockArray = new ArrayList<>();
    private List<String> comparisonBlockArrayLeaves = new ArrayList<>();

    private List<Block> blocks = new ArrayList<>();

    public TreeCutter(Block startBlock) {

        runLoop(startBlock, startBlock.getX(), startBlock.getZ());
        int blocksSize = blocks.size();

        new Timer().scheduleAtFixedRate(new TimerTask() {

            int index = 0;
            @Override
            public void run() {

                if (!Main.getInstance().isEnabled()) {
                    cancel();
                    return;
                }

                if (index >= blocksSize)
                    cancel();
                else
                    try {

                        Block block = blocks.get(index);
                        Bukkit.getScheduler().runTask(Main.getInstance(), block::breakNaturally);
                        index += 1;

                    } catch (Exception e) {

                        cancel();

                    }

            }
        }, 0, 1);

        popLeaves(startBlock);
    }

    private void runLoop(Block b1, final int x1, final int z1) {

        for (int x = -2; x <= 2; x++) {

            for (int y = -2; y <= 2; y++) {

                for (int z = -2; z <= 1; z++) {

                    if (x == 0 && y == 0 && z == 0)
                        continue;

                    Block b2 = b1.getRelative(x, y, z);
                    String s = b2.getX() + ":" + b2.getY() + ":" + b2.getZ();

                    if ((b2.getType() == Material.LEAVES || b2.getType() == Material.LEAVES_2)

                            && !comparisonBlockArrayLeaves.contains(s))
                        comparisonBlockArrayLeaves.add(s);

                    if (b2.getType() != Material.LOG && b2.getType() != Material.LOG_2)

                        continue;
                    int searchSquareSize = 25;

                    if (b2.getX() > x1 + searchSquareSize || b2.getX() < x1 - searchSquareSize
                            || b2.getZ() > z1 + searchSquareSize || b2.getZ() < z1 - searchSquareSize)

                        break;

                    if (!comparisonBlockArray.contains(s)) {

                        comparisonBlockArray.add(s);
                        blocks.add(b2);

                        this.runLoop(b2, x1, z1);

                    }

                }

            }

        }

    }

    private void popLeaves(Block block) {

        if(block.getType() == Material.LOG || block.getType() == Material.LOG_2) {

            for (int y = -6; y < 12 + 1; y++) {

                for (int x = -5; x < 5 + 1; x++) {

                    for (int z = -5; z < 5 + 1; z++) {

                        Block target = block.getRelative(x, y, z);

                        if (target.getType() == Material.LEAVES || target.getType() == Material.LEAVES_2) {

                            Location loc = treeSize(leafType(target));

                            int fX = loc.getBlockX();
                            int fY = loc.getBlockY();
                            int fZ = loc.getBlockZ();

                            if(target.getType() == Material.LEAVES) {

                                for (int mX = -fX; mX < fX + 1; mX++) {

                                    for (int mY = -fY; mY < fY + 1; mY++) {

                                        for (int mZ = -fZ; mZ < fZ + 1; mZ++) {

                                            Block finalTarget = block.getRelative(mX, mY, mZ);

                                            if (finalTarget.getType() == Material.LEAVES) {

                                                if (leafType(finalTarget) == TreeSpecies.GENERIC) {

                                                    UsefulMethods.dropTreeApple(target);

                                                }

                                                finalTarget.breakNaturally();

                                            }

                                        }

                                    }

                                }

                            } else if(target.getType() == Material.LEAVES_2) {

                                for (int sx = -7; sx < 6 + 1; sx++) {

                                    for (int sy = -10; sy < 12 + 1; sy++) {

                                        for (int sz = -7; sz < 6 + 1; sz++) {

                                            Block finalTarget = block.getRelative(sx, sy, sz);

                                            if (finalTarget.getType() == Material.LEAVES_2) {

                                                if (leafType(finalTarget) == TreeSpecies.DARK_OAK) {

                                                    UsefulMethods.dropTreeApple(target);

                                                }

                                                finalTarget.breakNaturally();

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

    }

    private TreeSpecies leafType(Block block) {

        if(block.getType() == Material.LEAVES) {

            Tree w = (Tree) block.getState().getData();
            return w.getSpecies();

        } else if(block.getType() == Material.LEAVES_2) {

            return TreeSpecies.DARK_OAK;

        } else {

            return null;

        }

    }

    private Location treeSize(TreeSpecies specie) {

        World world = Bukkit.getWorld("world");

        if(specie == TreeSpecies.GENERIC) {

            return new Location(world, 3, 8, 3);

        } else if(specie == TreeSpecies.REDWOOD) {

            return new Location(world, 4, 13, 3);

        } else if(specie == TreeSpecies.ACACIA) {

            return new Location(world, 5, 8, 5);

        } else if(specie == TreeSpecies.BIRCH) {

            return new Location(world, 2, 13, 2);

        } else if(specie == TreeSpecies.DARK_OAK) {

            return new Location(world, 3, 12, 3);

        }

        return new Location(world, 0, 0, 0);

    }

}
