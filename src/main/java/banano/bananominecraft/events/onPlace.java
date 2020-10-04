package banano.bananominecraft.events;
import banano.bananominecraft.PayToPlace;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.block.Block;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;


public class onPlace implements Listener {
    private Plugin plugin = banano.bananominecraft.PayToPlace.getPlugin(PayToPlace.class);


    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Block block = event.getBlock();
            Player player = event.getPlayer();

            Location b_loc = block.getLocation();
            Location lastB_loc = getLastBlockCoords(player, block);

            Location p_loc = player.getLocation();
            Location lastP_loc = getLastPlayerCoords(player);

            Long lastPlaceTime = getLastPlaceTime(player);
            Long currentTime = System.currentTimeMillis();

            Material[] blacklist = {Material.BIRCH_BOAT, Material.ACACIA_BOAT, Material.DARK_OAK_BOAT, Material.JUNGLE_BOAT, Material.OAK_BOAT, Material.SPRUCE_BOAT, Material.WATER_BUCKET, Material.WATER, Material.LAVA, Material.LAVA_BUCKET, Material.REDSTONE_TORCH, Material.REDSTONE_WALL_TORCH, Material.REDSTONE_WIRE, Material.REDSTONE, Material.MINECART, Material.CHEST_MINECART, Material.FURNACE_MINECART, Material.COMMAND_BLOCK_MINECART, Material.HOPPER_MINECART, Material.TNT_MINECART, Material.HOPPER};

            int moveCount = getMoveCount(player);

            Random rand = new Random();
            int n = rand.nextInt(1000);
            if (n == 9) {
                player.setWalkSpeed(0);
                player.setAllowFlight(false);
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage("AntiBot protection: Please reconnect to the server");
            } else {
                if (lastB_loc.equals(b_loc)) {
                    player.sendMessage("Don't just place blocks in the same place!");
                }

                if (block.getType() == Material.SCAFFOLDING) {
                    player.sendMessage("You're not being paid to build scaffolding towers!");
                }

                if (Arrays.asList(blacklist).contains(block.getType())) {
                    player.sendMessage(block.getType() + " is on the blacklist");
                    block.setType(Material.AIR);
                } else if (lastP_loc.toVector().equals(p_loc.toVector()) && moveCount == 3) {
                    player.sendMessage("C'mon, move!");
                } else if (lastP_loc.toVector().equals(p_loc.toVector()) && moveCount < 3) {
                    moveCount++;
                    setMoveCount(player, moveCount);
                } else if (lastP_loc.toVector() != p_loc.toVector()) {
                    moveCount = 0;
                    setMoveCount(player, moveCount);
                    countBlock(player);
                } else if (lastPlaceTime + 40 > currentTime) {
                    warnUser(player);
                } else {
                    countBlock(player);
                }
                setLastBlockCoords(player, b_loc.toVector());
                setLastPlayerCoords(player, p_loc.toVector());
                setLastPlaceTime(player, currentTime);
            }
        });

        }



    private void setMoveCount(Player player, int moveCount){
        plugin.getConfig().set("Users." + player.getUniqueId() + ".moveCount", moveCount);
        plugin.saveConfig();
    }

    private int getMoveCount(Player player){
        return plugin.getConfig().getInt("Users." + player.getUniqueId() + ".moveCount");
    }


    private Long getLastPlaceTime(Player player){
        return plugin.getConfig().getLong("Users." + player.getUniqueId() + ".TimePlaced");
    }

    private void setLastPlaceTime(Player player, Long currentTime){
        plugin.getConfig().set("Users." + player.getUniqueId() + ".TimePlaced", currentTime);
        plugin.saveConfig();
    }


    private Location getLastBlockCoords(Player player, Block block) {
        org.bukkit.util.Vector lastBlockCoords = plugin.getConfig().getVector("Users." + player.getUniqueId() + ".lastBlockCoords");
        if (lastBlockCoords == null){
            return block.getLocation();
        }
        return lastBlockCoords.toLocation(player.getWorld());
    }

    private void setLastBlockCoords(Player player, org.bukkit.util.Vector lastBlockCoords) {
        plugin.getConfig().set("Users." + player.getUniqueId() + ".lastBlockCoords", lastBlockCoords);
        plugin.saveConfig();
    }

    private Location getLastPlayerCoords(Player player) {
        org.bukkit.util.Vector lastPlayerCoords = plugin.getConfig().getVector("Users." + player.getUniqueId() + ".lastPlayerCoords");
        if (lastPlayerCoords == null){
            return player.getLocation();
        }
        return lastPlayerCoords.toLocation(player.getWorld());
    }

    private void setLastPlayerCoords(Player player, org.bukkit.util.Vector lastPlayerCoords) {
        plugin.getConfig().set("Users." + player.getUniqueId() + ".lastPlayerCoords", lastPlayerCoords);
        plugin.saveConfig();
    }

    private int getCount(Player player) {
        return plugin.getConfig().getInt("Users." + player.getUniqueId() + ".Blocks_Placed");
    }

    private void setCount(Player player, int count) {
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Blocks_Placed", count);
        plugin.saveConfig();
    }

    private int getWarnings(Player player) {
        return plugin.getConfig().getInt("Users." + player.getUniqueId() + ".Warnings");
    }

    private void setWarnings(Player player, int warnings) {
        plugin.getConfig().set("Users." + player.getUniqueId() + ".Warnings", warnings);
        plugin.saveConfig();

    }

    private void countBlock(Player player) {
        int count = getCount(player);
        count++;
        setCount(player, count);

        if (count % 10 == 0) {
            player.sendMessage("You have placed " + count + " blocks total");
        }
        if (count % 1 == 0) {
            int payment = 1;

            EconomyResponse r = PayToPlace.econ.depositPlayer(player, payment);
            if (r.transactionSuccess()) {
                player.sendMessage("You have placed another 1 block, here's " + payment + " Banano");
            } else {
                player.sendMessage("You were not paid for some reason. Check server balance");
            }
        }
    }

    private void warnUser(Player player) {
        int warnings = getWarnings(player);
        warnings++;
        player.sendMessage("Warning: " + warnings);
        player.sendMessage("You're placing blocks fast...");
        System.out.println(player.getName() + " is placing blocks very fast... Maybe investigate");

        if (warnings == 10) {
            player.kickPlayer("Slow down with the placing, Mr.Speedy");
            System.out.println(player.getName() + " is placing blocks very fast... Maybe investigate");
            warnings = 0;

        }

        setWarnings(player, warnings);

    }
}