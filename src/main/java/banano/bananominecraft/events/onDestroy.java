package banano.bananominecraft.events;
import banano.bananominecraft.PayToPlace;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class onDestroy implements Listener {
    @EventHandler
    public void onDestroy(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        Location b_lock = block.getLocation();
        if(b_lock.getBlockY() == 0){
            player.kickPlayer("Don't break the bottom layer!");

        }
    }
}
