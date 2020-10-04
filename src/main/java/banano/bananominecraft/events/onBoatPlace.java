package banano.bananominecraft.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public class onBoatPlace implements Listener {

    @EventHandler
    public void onBoatPlace(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Material[] blacklist = {Material.BIRCH_BOAT, Material.ACACIA_BOAT, Material.DARK_OAK_BOAT, Material.JUNGLE_BOAT, Material.OAK_BOAT, Material.SPRUCE_BOAT};
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(Arrays.asList(blacklist).contains(player.getItemInHand().getType())){
            event.setCancelled(true);
                player.sendMessage("No boats!");
        }


        }

    }
}
